from .sql_base import Base
from sqlalchemy import Column, Integer, String, ForeignKey, Float
from sqlalchemy.orm import relationship
from .category_mapper import CategoryMapper


class ExpenseMapper(Base):
    __tablename__ = 'expenses'

    id = Column(Integer, primary_key=True)
    user_id = Column(Integer)
    label = Column(String)
    category_id = Column(Integer, ForeignKey('categories.id'))
    category = relationship(CategoryMapper)
    amount = Column(Float)

    def __repr__(self):
        return f"<Expense[{self.id}]({self.user_id}:" \
               f" '{self.label}'" \
               f" '{self.category.label}'" \
               f" {self.amount}" \
               f")>"

    def map(self):
        return {
            'user_id': self.user_id,
            'label': self.label,
            'category': self.category.label,
            'amount': self.amount
        }
