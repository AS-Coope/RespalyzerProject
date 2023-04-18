"""empty message

Revision ID: ff93519f5fd7
Revises: 0d2764cc998c
Create Date: 2023-04-18 00:08:55.069903

"""
from alembic import op
import sqlalchemy as sa
from sqlalchemy.dialects import mysql

# revision identifiers, used by Alembic.
revision = 'ff93519f5fd7'
down_revision = '0d2764cc998c'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.create_table('medical_centres',
    sa.Column('centre_id', sa.Integer(), autoincrement=True, nullable=False),
    sa.Column('name', sa.String(length=200), nullable=True),
    sa.Column('location', sa.String(length=200), nullable=True),
    sa.Column('geotag', sa.String(length=200), nullable=True),
    sa.Column('contact_number', sa.String(length=12), nullable=True),
    sa.Column('contact_number2', sa.String(length=12), nullable=True),
    sa.Column('latitude', sa.Float(precision=10, asdecimal=4), nullable=True),
    sa.Column('longitude', sa.Float(precision=10, asdecimal=4), nullable=True),
    sa.PrimaryKeyConstraint('centre_id')
    )
    op.create_table('user',
    sa.Column('user_id', sa.Integer(), autoincrement=True, nullable=False),
    sa.Column('name', sa.String(length=200), nullable=True),
    sa.Column('age', sa.Integer(), nullable=True),
    sa.Column('gender', sa.String(length=6), nullable=True),
    sa.Column('weight', sa.Integer(), nullable=True),
    sa.Column('height', sa.String(length=30), nullable=True),
    sa.PrimaryKeyConstraint('user_id')
    )
    op.create_table('emergency contact',
    sa.Column('contact_id', sa.Integer(), autoincrement=True, nullable=False),
    sa.Column('user_id', sa.Integer(), nullable=True),
    sa.Column('contact', sa.String(length=200), nullable=True),
    sa.Column('contact_number', sa.String(), nullable=True),
    sa.ForeignKeyConstraint(['user_id'], ['user.user_id'], ),
    sa.PrimaryKeyConstraint('contact_id')
    )
    op.create_table('existing conditions',
    sa.Column('condition_id', sa.Integer(), autoincrement=True, nullable=False),
    sa.Column('user_id', sa.Integer(), nullable=True),
    sa.Column('condition', sa.String(length=200), nullable=True),
    sa.ForeignKeyConstraint(['user_id'], ['user.user_id'], ),
    sa.PrimaryKeyConstraint('condition_id')
    )
    op.create_table('recordings',
    sa.Column('recording_id', sa.Integer(), autoincrement=True, nullable=False),
    sa.Column('user_id', sa.Integer(), nullable=True),
    sa.Column('recording', sa.String(length=200), nullable=True),
    sa.Column('reading', sa.String(length=200), nullable=True),
    sa.Column('date_recorded', sa.DateTime(), nullable=True),
    sa.ForeignKeyConstraint(['user_id'], ['user.user_id'], ),
    sa.PrimaryKeyConstraint('recording_id')
    )
    op.drop_table('Medical Centres')
    op.drop_table('User')
    op.drop_table('Existing Conditions')
    op.drop_table('Recordings')
    op.drop_table('Emergency Contact')
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.create_table('Emergency Contact',
    sa.Column('contact_id', mysql.INTEGER(), autoincrement=True, nullable=False),
    sa.Column('contact', mysql.VARCHAR(length=200), nullable=True),
    sa.Column('contact_number', mysql.VARCHAR(length=10), nullable=True),
    sa.Column('user_id', mysql.INTEGER(), autoincrement=False, nullable=True),
    sa.ForeignKeyConstraint(['user_id'], ['User.user_id'], name='Emergency Contact_ibfk_1'),
    sa.PrimaryKeyConstraint('contact_id'),
    mysql_default_charset='utf8mb3',
    mysql_engine='InnoDB'
    )
    op.create_table('Recordings',
    sa.Column('recording_id', mysql.INTEGER(), autoincrement=True, nullable=False),
    sa.Column('recording', mysql.VARCHAR(length=200), nullable=True),
    sa.Column('reading', mysql.VARCHAR(length=200), nullable=True),
    sa.Column('date_recorded', mysql.DATETIME(), nullable=True),
    sa.Column('user_id', mysql.INTEGER(), autoincrement=False, nullable=True),
    sa.ForeignKeyConstraint(['user_id'], ['User.user_id'], name='Recordings_ibfk_1'),
    sa.PrimaryKeyConstraint('recording_id'),
    mysql_default_charset='utf8mb3',
    mysql_engine='InnoDB'
    )
    op.create_table('Existing Conditions',
    sa.Column('condition_id', mysql.INTEGER(), autoincrement=True, nullable=False),
    sa.Column('condition', mysql.VARCHAR(length=200), nullable=True),
    sa.Column('user_id', mysql.INTEGER(), autoincrement=False, nullable=True),
    sa.ForeignKeyConstraint(['user_id'], ['User.user_id'], name='Existing Conditions_ibfk_1'),
    sa.PrimaryKeyConstraint('condition_id'),
    mysql_default_charset='utf8mb3',
    mysql_engine='InnoDB'
    )
    op.create_table('User',
    sa.Column('user_id', mysql.INTEGER(), autoincrement=True, nullable=False),
    sa.Column('name', mysql.VARCHAR(length=200), nullable=True),
    sa.Column('age', mysql.INTEGER(), autoincrement=False, nullable=True),
    sa.Column('gender', mysql.VARCHAR(length=6), nullable=True),
    sa.Column('weight', mysql.INTEGER(), autoincrement=False, nullable=True),
    sa.Column('height', mysql.VARCHAR(length=30), nullable=True),
    sa.PrimaryKeyConstraint('user_id'),
    mysql_default_charset='utf8mb3',
    mysql_engine='InnoDB'
    )
    op.create_table('Medical Centres',
    sa.Column('centre_id', mysql.INTEGER(), autoincrement=True, nullable=False),
    sa.Column('name', mysql.VARCHAR(length=200), nullable=True),
    sa.Column('location', mysql.VARCHAR(length=200), nullable=True),
    sa.PrimaryKeyConstraint('centre_id'),
    mysql_default_charset='utf8mb3',
    mysql_engine='InnoDB'
    )
    op.drop_table('recordings')
    op.drop_table('existing conditions')
    op.drop_table('emergency contact')
    op.drop_table('user')
    op.drop_table('medical_centres')
    # ### end Alembic commands ###
