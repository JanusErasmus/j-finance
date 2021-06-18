import datetime
from .sql_base import Base
from sqlalchemy import Column, Integer, String, DateTime, Float


class UserMapper(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True)
    name = Column(String)
    budget_date = Column(DateTime, default=datetime.datetime.utcnow)
    budget_amount = Column(Float)

    def __repr__(self):

        return f"<User[{self.id}]({self.user_id}:" \
               f" '{self.name}'" \
               f" '{self.budget_date}'" \
               f" {self.budget_amount}" \
               f")>"

    def map(self):
        return {
            'id': self.id,
            'name': self.label,
            'date': self.budget_date,
            'amount': self.budget_amount
        }
