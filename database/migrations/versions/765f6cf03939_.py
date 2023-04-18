"""empty message

Revision ID: 765f6cf03939
Revises: ad4ab9bf4924
Create Date: 2023-04-09 23:14:32.517147

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = '765f6cf03939'
down_revision = 'ad4ab9bf4924'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    with op.batch_alter_table('medical_centres', schema=None) as batch_op:
        batch_op.add_column(sa.Column('latitude', sa.Double(precision=10, asdecimal=4), nullable=True))
        batch_op.add_column(sa.Column('longitude', sa.Double(precision=10, asdecimal=4), nullable=True))

    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    with op.batch_alter_table('medical_centres', schema=None) as batch_op:
        batch_op.drop_column('longitude')
        batch_op.drop_column('latitude')

    # ### end Alembic commands ###