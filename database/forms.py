from flask_wtf import FlaskForm
from wtforms import StringField, IntegerField, TextAreaField, SubmitField, SelectField
from wtforms.validators import DataRequired
from flask_wtf.file import FileField, FileRequired, FileAllowed

class UserForm(FlaskForm):
    name = StringField('Name', validators= [DataRequired()])
    gender = StringField('Gender', validators=[DataRequired()])
    age = IntegerField('Age', validators= [DataRequired()])
    height = IntegerField('Height', validators= [DataRequired()])
    weight = IntegerField('Weight', validators= [DataRequired()])
    submit = SubmitField('Add Property')