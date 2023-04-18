"""
Flask Documentation:     https://flask.palletsprojects.com/
Jinja2 Documentation:    https://jinja.palletsprojects.com/
Werkzeug Documentation:  https://werkzeug.palletsprojects.com/
This file contains the routes for your application.
"""

from database import app, db
from flask import render_template, request, make_response, redirect, flash, redirect, send_from_directory, jsonify
from .models import User, Medical_Centre, Recording, Existing_Condition, Emergency_Contact, Disease

from werkzeug.utils import secure_filename
import os
import mysql.connector



###
# Routing for your application.
###

@app.route('/', methods=["GET"])
def server():
    return "Connected to server"

@app.route('/register', methods=["POST"])
def register():
    try:
        cnx = mysql.connector.connect(user='root', password='\KEs{azmr|6}<_}0', host='34.71.7.124', database='respalyzer')
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
        cursor.execute(f"INSERT INTO user (name, age, gender, weight, height) VALUES ('{name}','{age}','{gender}','{weight}','{height}')")
        cursor.execute(f"INSERT INTO `emergency contact` (contact, user_id, contact_number) VALUES ('{emergency_contact}', (SELECT MAX(user_id) FROM user), '{emergency_contact_number}')")
        cursor.execute(f"INSERT INTO `existing conditions` (`condition`, user_id) VALUES ('{condition}', (SELECT MAX(user_id) FROM user))")
        cnx.commit()
        cursor.close()
        cnx.close()
        return make_response({"success" : "User added"}, 201)
    except Exception as e:
        return make_response({'error': str(e)}, 400)

@app.route('/record', methods=["POST"])
def record():
    try:
        cnx = mysql.connector.connect(user='root', password='\KEs{azmr|6}<_}0', host='34.71.7.124', database='respalyzer')
        cursor = cnx.cursor()
        content = request.json
        recording = content['recording']
        reading = content['reading']
        #date_recorded = content['date_recorded']
        cursor.execute(f"INSERT INTO recordings (recording, reading, user_id) VALUES ('{recording}','{reading}', (SELECT MAX(user_id) FROM user))")
        #This is not finished
        cnx.commit()
        cursor.close()
        cnx.close()
        return make_response({"success" : "Recording added"}, 201)
    except Exception as e:
        return make_response({'error': str(e)}, 400)

@app.route('/profile/<user_id>', methods=['GET'])
def get_profile(user_id):
    try:
        with mysql.connector.connect(user='root', password='\KEs{azmr|6}<_}0', host='34.71.7.124', database='respalyzer') as cnx:
            cursor = cnx.cursor()
            query = "SELECT name, age, gender, weight, height FROM user WHERE user_id = %s"
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
    except mysql.connector.Error as error:
        return jsonify({'error': f"An error has occurred: {error}"}), 500
    
@app.route('/recordings/<user_id>', methods=['GET'])
def get_recordings(user_id):
    try:
        with mysql.connector.connect(user='root', password='\KEs{azmr|6}<_}0', host='34.71.7.124', database='respalyzer') as cnx:
            cursor = cnx.cursor()
            query = "SELECT recording, reading, date_recorded FROM recordings WHERE user_id = %s"
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
    except mysql.connector.Error as error:
        return jsonify({'error': f"An error has occurred: {error}"}), 500

@app.route('/contacts/<user_id>', methods=['GET'])
def get_contacts(user_id):
    try:
        cnx = mysql.connector.connect(user='root', password='\KEs{azmr|6}<_}0', host='34.71.7.124', database='respalyzer')
        cursor = cnx.cursor()
        query = "SELECT contact, contact_number FROM `emergency contact` WHERE user_id = %s"
        cursor.execute(query, (user_id,))
        contacts = []
        for contact, contact_number in cursor:
            record = {
                'contact': contact,
                'contact_number': contact_number
            }
            contacts.append(record)
        return jsonify({'contact': contacts}), 200
    except mysql.connector.Error as error:
        return jsonify({'error': f"An error has occurred: {error}"}), 500

@app.route('/diseases/<disease_id>', methods=['GET'])
def get_disease(disease_id):
    try:
        cnx = mysql.connector.connect(user='root', password='\KEs{azmr|6}<_}0', host='34.71.7.124', database='respalyzer')
        cursor = cnx.cursor()
        query = "SELECT name, description, causes, symptoms, treatment FROM `diseases` WHERE disease_id = %s"
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
    except mysql.connector.Error as error:
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