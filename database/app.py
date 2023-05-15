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

from os import path
from pydub import AudioSegment

src = "ZrC - XLR8 [Snippet - 808 note change] (165bpm).mp3"
dst = "XLR8 - test.wav"

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

@app.route('/record', methods=["POST"])
def record():
    try:
        cnx = psycopg2.connect(user='respalyzer', password='pa$$w0rd', host='localhost', database='respalyzer')
        cursor = cnx.cursor()
        content = request.json

        # does the audio conversion by file name
        audio = AudioSegment.from_mp3(src)
        audio.export(dst, format="wav")
        
        recording = dst #content['recording']
        #reading = #content['reading']
        #date_recorded = content['date_recorded']
        cursor.execute(f"INSERT INTO public.recordings (recording, recording, date_recorded, user_id) VALUES ('{recording}','{recording}', NOW(), (SELECT MAX(user_id) FROM \"user\"))")
        # what should actually be there but I'm putting recording twice (in place of reading too) for testing
        #f"INSERT INTO public.recordings (recording, reading, date_recorded, user_id) VALUES ('{recording}','{reading}', NOW(), (SELECT MAX(user_id) FROM \"user\"))"        
        model = pickle.load(open('../model.pkl','rb'))
        
        
        resampled_audio = resample_audio(recording)

        upperCutoffFreq = 3000
        cutoffFrequencies = [80, upperCutoffFreq]
        highPassCoeffs = signal.firwin(401, cutoffFrequencies, fs=gSampleRate, pass_zero="bandpass")

        normalized_audio = normalizeVolume(applyHighpass(resampled_audio, highPassCoeffs))
        cleaned_audio = applyLogCompressor(normalized_audio, 30)
        with tempfile.NamedTemporaryFile(suffix='.wav', delete=False) as temp:
            temp_filename = temp.name
            soundfile.write(temp_filename, cleaned_audio, gSampleRate)

            features = np.array(audio_features(temp_filename))
            features_reshaped = np.reshape(features, (1, 193, 1))
            predictions = model.predict(features_reshaped)
            outcome, percentage = process_predictions(predictions)
        os.remove(temp_filename)
        
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
    return outcome, percentage

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
    
@app.route('/recordings/<user_id>', methods=['GET'])
def get_recordings(user_id):
    try:
        with psycopg2.connect(user='respalyzer', password='pa$$w0rd', host='localhost', database='respalyzer') as cnx:
            cursor = cnx.cursor()
            query = "SELECT recording, reading, date_recorded FROM public.recordings WHERE user_id = %s"
            cursor.execute(query, (user_id,))
            recordings = []
            for recording, reading, date_recorded in cursor:
                record = {
                    'recording': recording,
                    'reading': reading,
                    'date_recorded': date_recorded
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
