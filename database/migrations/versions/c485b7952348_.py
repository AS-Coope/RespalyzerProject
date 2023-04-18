"""empty message

Revision ID: c485b7952348
Revises: 03792ff9ad27
Create Date: 2023-04-02 14:31:28.200152

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = 'c485b7952348'
down_revision = '03792ff9ad27'
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    with op.batch_alter_table('Emergency Contact', schema=None) as batch_op:
        batch_op.add_column(sa.Column('contact_num', sa.Integer(), nullable=True))
        batch_op.drop_column('contact_number')

    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    with op.batch_alter_table('Emergency Contact', schema=None) as batch_op:
        batch_op.add_column(sa.Column('contact_number', sa.VARCHAR(length=10), autoincrement=False, nullable=True))
        batch_op.drop_column('contact_num')

    # ### end Alembic commands ###