import datetime
from .sql_base import Base
from sqlalchemy import Column, Integer, String, ForeignKey, Float, DateTime
from sqlalchemy.orm import relationship
from .expense_mapper import ExpenseMapper
from .user_mapper import UserMapper


class TransactionMapper(Base):
    __tablename__ = 'transactions'

    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('users.id'))
    user = relationship(UserMapper)
    description = Column(String)
    date = Column(DateTime, default=datetime.datetime.utcnow)
    expense_id = Column(Integer, ForeignKey('expenses.id'))
    expense = relationship(ExpenseMapper)
    amount = Column(Float)

    def __repr__(self):
        return f"<Transaction[{self.id}]({self.user_id}:" \
               f" '{self.description}'" \
               f" '{self.expense.label}'" \
               f" {self.amount}" \
               f")>"

    def map(self):
        return {
            'id': self.id,
            'desc': self.description,
            'expense': self.expense.map(),
            'amount': self.amount
        }
