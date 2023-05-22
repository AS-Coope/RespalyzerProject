"""
Flask Documentation:     https://flask.palletsprojects.com/
Jinja2 Documentation:    https://jinja.palletsprojects.com/
Werkzeug Documentation:  https://werkzeug.palletsprojects.com/
This file contains the routes for your application.
"""

from database import app, db
from flask import request, make_response, flash, jsonify
from .models import User, Medical_Centre, Recording, Existing_Condition, Emergency_Contact, Disease

import pickle
import librosa
import soundfile
import tempfile
import numpy as np
from scipy import signal

from werkzeug.utils import secure_filename
import os
import psycopg2
import keras

# these libraries deal with mp3 to wav conversion
from os import path
from pydub import AudioSegment


src = "file.mp3"
dst = "testFile.wav"

# this part deals with the conversion
# use the following 2 commented lines to deal with audio conversion to mp3 (use this in the function where relevant)
# src should be the mp3 file coming in, and destination will be whatever you want the file to be named as done above
#audio = AudioSegment.from_mp3(src)
#audio.export(dst, format="wav")

gSampleRate = 7000


###
# Routing for your application.
###

@app.route('/', methods=["GET"])
def server():
    return "Connected to server"

@app.route('/register', methods=["POST"])
def register():
    try:
        cnx = psycopg2.connect(user='respalyzer', password='pa$$w0rd', host='localhost', database='respalyzer')
        cursor = cnx.cursor()
        content = request.json
        name = content['name']
        age = content['age']
        gender = content['gender']
        weight = content['weight']
        height = content['height']
        emergency_contact = content['contact']
        emergency_contact_number = content['contact_number']
        condition = content['condition']
        cursor.execute(f"INSERT INTO public.user (name, age, gender, weight, height) VALUES ('{name}',{age},'{gender}',{weight},{height})")
        cursor.execute(f"INSERT INTO public.\"emergency contact\" (contact, user_id, contact_number) VALUES ('{emergency_contact}', (SELECT MAX(user_id) FROM \"user\"), '{emergency_contact_number}')")
        cursor.execute(f"INSERT INTO public.\"existing conditions\" (condition, user_id) VALUES ('{condition}', (SELECT MAX(user_id) FROM \"user\"))")
        cnx.commit()
        cursor.close()
        cnx.close()
        return make_response({"success" : "User added"}, 201)
    except Exception as e:
        return make_response({'error': str(e)}, 400)

@app.route('/record', methods=["GET", "POST"])
def record():
    try:
        cnx = psycopg2.connect(user='respalyzer', password='pa$$w0rd', host='localhost', database='respalyzer')
        cursor = cnx.cursor()

        # Get the uploaded file
        print(request.files)
        file = request.files['recording']
        print(request.files)

        # Save the file to a temporary location
        temp_filename = tempfile.mktemp(suffix='.wav', dir='audio')
        file.save(temp_filename)

        # Load the trained AI model
        model = pickle.load(open('../model.pkl', 'rb'))

        # Preprocess the recording
        resampled_audio = resample_audio(temp_filename)

        # Rest of the preprocessing steps...
        upperCutoffFreq = 3000
        cutoffFrequencies = [80, upperCutoffFreq]
        highPassCoeffs = signal.firwin(401, cutoffFrequencies, fs=gSampleRate, pass_zero="bandpass")

        normalized_audio = normalizeVolume(applyHighpass(resampled_audio, highPassCoeffs))
        cleaned_audio = applyLogCompressor(normalized_audio, 30)

        with tempfile.NamedTemporaryFile(suffix='.wav', delete=False) as temp:
            temp_filename = temp.name
            soundfile.write(temp_filename, cleaned_audio, gSampleRate)

            # Extract features from the audio
            features = np.array(audio_features(temp_filename))
            features_reshaped = np.reshape(features, (1, 193, 1))

            # Make predictions using the AI model
            predictions = model.predict(features_reshaped)
            outcome, percentage, largest_index = process_predictions(predictions)
            likelihood = round((percentage * 100), 2)
            print(outcome, likelihood, largest_index)

            # Remove the temporary audio file
            #os.remove(temp_filename)

        cursor.execute(f"INSERT INTO public.recordings (recording, reading, date_recorded, user_id, disease_id, likelihood) VALUES ('{temp_filename}','{outcome}', NOW(), (SELECT MAX(user_id) FROM \"user\"), {largest_index}, {likelihood})")
        cnx.commit()
        cursor.close()
        cnx.close()
        return make_response({"success" : "Recording added"}, 201)
    except Exception as e:
        return make_response({'error': str(e)}, 400)

def resample_audio(filename):
    audioBuffer, nativeSampleRate = librosa.load(filename, dtype=np.float32, mono=True, sr=None)

    if nativeSampleRate == gSampleRate:
        print("Resample Ran")
        return audioBuffer
    else:
        duration = len(audioBuffer) / nativeSampleRate 
        nTargetSamples = int(duration * gSampleRate)
        timeXSource = np.linspace(0, duration, len(audioBuffer), dtype=np.float32)
        timeX = np.linspace(0, duration, nTargetSamples, dtype=np.float32)
        resampledBuffer = np.interp(timeX, timeXSource, audioBuffer)
        print("Resample Ran")
        return resampledBuffer

def normalizeVolume(npArr):
    minAmp, maxAmp = (np.amin(npArr), np.amax(npArr))
    maxEnv = max(abs(minAmp), abs(maxAmp))
    scale = 1.0 / maxEnv
    npArr *= scale
    print("Normalize Ran")
    return npArr

def applyLogCompressor(signal, gamma):
    sign = np.sign(signal)
    absSignal = 1 + np.abs(signal) * gamma
    logged = np.log(absSignal)
    scaled = logged * (1 / np.log(1.0 + gamma))
    print("Log Compressor Ran")
    return sign * scaled

def applyHighpass(npArr, highPassCoeffs):
    print("1 Ran")
    return signal.lfilter(highPassCoeffs, [1.0], npArr)

def audio_features(filename):
    sound, sample_rate = librosa.load(filename)
    stft = np.abs(librosa.stft(sound))  

    mfccs = np.mean(librosa.feature.mfcc(y=sound, sr=sample_rate, n_mfcc=40), axis=1)
    chroma = np.mean(librosa.feature.chroma_stft(S=stft, sr=sample_rate), axis=1)
    mel = np.mean(librosa.feature.melspectrogram(y=sound, sr=sample_rate), axis=1)
    contrast = np.mean(librosa.feature.spectral_contrast(S=stft, sr=sample_rate), axis=1)
    tonnetz = np.mean(librosa.feature.tonnetz(y=librosa.effects.harmonic(sound), sr=sample_rate), axis=1)

    concat = np.concatenate((mfccs, chroma, mel, contrast, tonnetz))
    print("2 ran")
    return concat

def process_predictions(predictions):
    readings = ["COPD", "Healthy", "URTI", "Bronchiectasis", "Pneumonia", "Bronchiolitis"]
    print(predictions)
    largest_index = np.argmax(predictions)
    print(largest_index)
    percentage = predictions[0][largest_index]
    print(percentage)
    outcome = readings[largest_index]
    print(outcome)
    print("3 ran")
    return outcome, percentage, largest_index

@app.route('/profile/<user_id>', methods=['GET'])
def get_profile(user_id):
    try:
        with psycopg2.connect(user='respalyzer', password='pa$$w0rd', host='localhost', database='respalyzer') as cnx:
            cursor = cnx.cursor()
            query = "SELECT name, age, gender, weight, height FROM public.user WHERE user_id = %s"
            cursor.execute(query, (user_id,))
            row = cursor.fetchone()
            profiles = []
            if row is not None:
                name, age, gender, weight, height = row
                record = {
                    'name': name,
                    'age': age,
                    'gender': gender,
                    'weight': weight,
                    'height': height
                }
                profiles.append(record)
                return jsonify({'profile': profiles}), 200
            else:
                return jsonify({'error': 'No recordings found'}), 404
    except psycopg2.Error as error:
        return jsonify({'error': f"An error has occurred: {error}"}), 500

@app.route('/profile/<user_id>', methods=['PUT'])
def update_profile(user_id):
    try:
        with psycopg2.connect(user='respalyzer', password='pa$$w0rd', host='localhost', database='respalyzer') as cnx:
            cursor = cnx.cursor()

            data = request.json
            updated_name = data.get('name')
            updated_age = data.get('age')
            updated_gender = data.get('gender')
            updated_weight = data.get('weight')
            updated_height = data.get('height')

            update_query = "UPDATE public.user SET name = %s, age = %s, gender = %s, weight = %s, height = %s WHERE user_id = %s"
            cursor.execute(update_query, (updated_name, updated_age, updated_gender, updated_weight, updated_height, user_id))
            cnx.commit()

            if cursor.rowcount > 0:
                return jsonify({'message': 'Profile updated successfully'}), 200
            else:
                return jsonify({'error': 'No user found with the provided user_id'}), 404
    except psycopg2.Error as error:
        return jsonify({'error': f"An error has occurred: {error}"}), 500



@app.route('/recordings/<user_id>', methods=['GET'])
def get_recordings(user_id):
    try:
        with psycopg2.connect(user='respalyzer', password='pa$$w0rd', host='localhost', database='respalyzer') as cnx:
            cursor = cnx.cursor()
            query = "SELECT recording, reading, date_recorded, disease_id FROM public.recordings WHERE user_id = %s"
            cursor.execute(query, (user_id,))
            recordings = []
            for recording, reading, date_recorded, disease_id in cursor:
                record = {
                    'recording': recording,
                    'reading': reading,
                    'date_recorded': date_recorded,
                    'disease_id': disease_id
                }
                recordings.append(record)
            return jsonify({'recordings': recordings}), 200
            #else:
                #return jsonify({'error': 'No recordings found'}), 404
    except psycopg2.Error as error:
        return jsonify({'error': f"An error has occurred: {error}"}), 500

@app.route('/recording/<recording_id>', methods=['GET'])
def get_recording(recording_id):
    try:
        with psycopg2.connect(user='respalyzer', password='pa$$w0rd', host='localhost', database='respalyzer') as cnx:
            cursor = cnx.cursor()
            query = "SELECT recording, reading, date_recorded, disease_id, likelihood FROM public.recordings WHERE recording_id = %s"
            cursor.execute(query, (recording_id,))
            recordings = []
            for recording, reading, date_recorded, disease_id, likelihood in cursor:
                record = {
                    'recording': recording,
                    'reading': reading,
                    'date_recorded': date_recorded,
                    'disease_id': disease_id,
                    'likelihood': likelihood
                }
                recordings.append(record)
            return jsonify({'recordings': recordings}), 200
            #else:
                #return jsonify({'error': 'No recordings found'}), 404
    except psycopg2.Error as error:
        return jsonify({'error': f"An error has occurred: {error}"}), 500

@app.route('/contacts/<user_id>', methods=['GET'])
def get_contacts(user_id):
    try:
        cnx = psycopg2.connect(user='respalyzer', password='pa$$w0rd', host='localhost', database='respalyzer')
        cursor = cnx.cursor()
        query = "SELECT contact, contact_number FROM public.\"emergency contact\" WHERE user_id = %s"
        cursor.execute(query, (user_id,))
        contacts = []
        for contact, contact_number in cursor:
            record = {
                'contact': contact,
                'contact_number': contact_number
            }
            contacts.append(record)
        return jsonify({'contact': contacts}), 200
    except psycopg2.Error as error:
        return jsonify({'error': f"An error has occurred: {error}"}), 500

@app.route('/diseases/<disease_id>', methods=['GET'])
def get_disease(disease_id):
    try:
        cnx = psycopg2.connect(user='respalyzer', password='pa$$w0rd', host='localhost', database='respalyzer')
        cursor = cnx.cursor()
        query = "SELECT name, description, causes, symptoms, treatment FROM public.diseases WHERE disease_id = %s"
        cursor.execute(query, (disease_id,))
        diseases = []
        for name, description, causes, symptoms, treatment in cursor:
            disease = {
                'name': name,
                'description' : description,
                'causes' : causes,
                'symptoms': symptoms,
                'treatment': treatment
            }
            diseases.append(disease)
        return jsonify({'disease': diseases}), 200
    except psycopg2.Error as error:
        return jsonify({'error': f"An error has occurred: {error}"}), 500
    
@app.route('/medical_centres', methods=['GET'])
def get_centres():
    try:
        with psycopg2.connect(user='respalyzer', password='pa$$w0rd', host='localhost', database='respalyzer') as cnx:
            cursor = cnx.cursor()
            query = "SELECT centre_id, name, location, contact_number, contact_number2, latitude, longitude FROM public.medical_centres"
            cursor.execute(query)
            centres = []
            for centre_id, name, location, contact_number, contact_number2, latitude, longitude in cursor:
                centre = {
                    'centre_id': centre_id,
                    'name': name,
                    'location': location,
                    'contact': contact_number,
                    'contact2': contact_number2,
                    'latitude': latitude,
                    'longitude': longitude
                }
                centres.append(centre)
            return jsonify({'Medical Centres': centres}), 200
            #else:
                #return jsonify({'error': 'No recordings found'}), 404
    except psycopg2.Error as error:
        return jsonify({'error': f"An error has occurred: {error}"}), 500
    
@app.route('/medical_centres/<centre_id>', methods=['GET'])
def get_centre(centre_id):
    try:
        cnx = psycopg2.connect(user='respalyzer', password='pa$$w0rd', host='localhost', database='respalyzer')
        cursor = cnx.cursor()
        query = "SELECT centre_id, name, location, contact_number, contact_number2, latitude, longitude FROM public.medical_centres WHERE centre_id = %s"
        cursor.execute(query, (centre_id,))
        centres = []
        for centre_id, name, location, contact_number, contact_number2, latitude, longitude in cursor:
            centre = {
                'centre_id': centre_id,
                'name': name,
                'location': location,
                'contact': contact_number,
                'contact2': contact_number2,
                'latitude': latitude,
                'longitude': longitude
            }
            centres.append(centre)
        return jsonify({'Medical Centres': centres}), 200
    except psycopg2.Error as error:
        return jsonify({'error': f"An error has occurred: {error}"}), 500
###
# The functions below should be applicable to all Flask apps.
###

# Display Flask WTF errors as Flash messages
def flash_errors(form):
    for field, errors in form.errors.items():
        for error in errors:
            flash(u"Error in the %s field - %s" % (
                getattr(form, field).label.text,
                error
            ), 'danger')

@app.route('/<file_name>.txt')
def send_text_file(file_name):
    """Send your static text file."""
    file_dot_text = file_name + '.txt'
    return app.send_static_file(file_dot_text)

if __name__ == '__main__':
    app.run(host="0.0.0.0")
