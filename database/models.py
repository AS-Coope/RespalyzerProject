from . import db

class User(db.Model):
    __tablename__ = 'User'

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
    __tablename__ = 'Medical Centres'

    centre_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    name = db.Column(db.String(200))
    location = db.Column(db.String(200))
    geotag = db.Column(db.String(200))
    contact_number = db.Column(db.String())
    contact_number2 = db.Column(db.String())

    def __init__(self, name, location, geotag, contact_number):
        self.name = name
        self.location = location
        self.geotag = geotag
        self.contact_number = contact_number

    def __repr__(self):
        return '<Medical_Centre %r>' % (self.name)

class Recording(db.Model):
    __tablename__ = 'Recordings'

    recording_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    user_id = db.Column(db.Integer, db.ForeignKey('User.user_id'))
    recording = db.Column(db.String(200))
    reading = db.Column(db.String(200))
    date_recorded = db.Column(db.DateTime(0,))

    def __init__(self, recording, user_id, reading, date_recorded):
        self.recording = recording
        self.user_id = user_id
        self.reading = reading
        self.date_recorded = date_recorded

    def __repr__(self):
        return '<Recording %r>' % (self.recording)

class Existing_Condition(db.Model):
    __tablename__ = 'Existing Conditions'

    condition_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    user_id = db.Column(db.Integer, db.ForeignKey('User.user_id'))
    condition = db.Column(db.String(200))

    def __init__(self, condition, user_id):
        self.condition = condition
        self.user_id = user_id

    def __repr__(self):
        return '<Existing_Condition %r>' % (self.condition)

class Emergency_Contact(db.Model):
    __tablename__ = 'Emergency Contact'

    contact_id = db.Column(db.Integer, primary_key=True, autoincrement=True)
    user_id = db.Column(db.Integer, db.ForeignKey('User.user_id'))
    contact = db.Column(db.String(200))
    contact_number = db.Column(db.String())
    
    

    def __init__(self, contact, contact_num, user_id):
        self.contact = contact
        self.contact_num = contact_num
        self.user_id = user_id

    def __repr__(self):
        return '<Emergency_Contact %r>' % (self.contact)