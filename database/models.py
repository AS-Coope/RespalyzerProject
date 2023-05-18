from . import db
from datetime import datetime

class User(db.Model):
    __tablename__ = 'user'

    user_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    name = db.Column(db.String(200))
    age = db.Column(db.Integer)
    gender = db.Column(db.String(6))
    weight = db.Column(db.Integer)
    height = db.Column(db.String(30))
    

    def __init__(self, name, age, gender, weight, height):
        self.name = name
        self.age = age
        self.gender = gender
        self.weight = weight
        self.height = height

    def __repr__(self):
        return '<User %r>' % (self.name)

class Medical_Centre(db.Model):
    __tablename__ = 'medical_centres'

    centre_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    name = db.Column(db.String(200))
    location = db.Column(db.String(200))
    geotag = db.Column(db.String(200))
    contact_number = db.Column(db.String(12))
    contact_number2 = db.Column(db.String(12))
    latitude = db.Column(db.Float(10,4))
    longitude = db.Column(db.Float(10,4))

    def __init__(self, name, location, geotag, contact_number, contact_number2, latitude, longitude):
        self.name = name
        self.location = location
        self.geotag = geotag
        self.contact_number = contact_number
        self.contact_number2 = contact_number2
        self.latitude = latitude
        self.longitude = longitude

    def __repr__(self):
        return '<Medical Centre %r>' % (self.name)

class Recording(db.Model):
    __tablename__ = 'recordings'

    recording_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    user_id = db.Column(db.Integer)
    recording = db.Column(db.String(200))
    reading = db.Column(db.String(200))
    date_recorded = db.Column(db.DateTime(), default=datetime.utcnow)
    disease_id = db.Column(db.Integer,)
    likelihood = db.Column(db.Float(10,2))

    def __init__(self, recording, user_id, reading, disease_id, likelihood):
        self.recording = recording
        self.user_id = user_id
        self.reading = reading
        self.date_recorded = datetime.utcnow()
        self.disease_id = disease_id
        self.likelihood = likelihood
        

    def __repr__(self):
        return '<Recording %r>' % (self.recording)

class Existing_Condition(db.Model):
    __tablename__ = 'existing conditions'

    condition_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    user_id = db.Column(db.Integer)
    condition = db.Column(db.String(200))

    def __init__(self, condition, user_id):
        self.condition = condition
        self.user_id = user_id

    def __repr__(self):
        return '<Existing_Condition %r>' % (self.condition)

class Emergency_Contact(db.Model):
    __tablename__ = 'emergency contact'

    contact_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    user_id = db.Column(db.Integer)
    contact = db.Column(db.String(200))
    contact_number = db.Column(db.String(11))
    
    

    def __init__(self, contact, contact_num, user_id):
        self.contact = contact
        self.contact_num = contact_num
        self.user_id = user_id

    def __repr__(self):
        return '<Emergency_Contact %r>' % (self.contact)

class Disease(db.Model):
    __tablename__ = 'diseases'

    disease_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    name = db.Column(db.String(200))
    description = db.Column(db.String(3000))
    causes = db.Column(db.String(200))
    symptoms = db.Column(db.String(500))
    treatment = db.Column(db.String(500))


    def __init__(self, name, description, causes, symptoms, treatment):
        self.name = name
        self.description = description
        self.causes = causes
        self.symptoms = symptoms
        self.treatment = treatment

    def __repr__(self):
        return '<Disease %r>' % (self.name)