from .sql_base import Base
from sqlalchemy import Column, Integer, String, ForeignKey, Float
from sqlalchemy.orm import relationship
from .category_mapper import CategoryMapper
from .user_mapper import UserMapper


class ExpenseMapper(Base):
    __tablename__ = 'expenses'

    id = Column(Integer, primary_key=True)
    user_id = Column(Integer, ForeignKey('users.id'))
    user = relationship(UserMapper)
    label = Column(String)
    category_id = Column(Integer, ForeignKey('categories.id'))
    category = relationship(CategoryMapper)
    amount = Column(Float)

    def __repr__(self):
        cat_label = ""
        if self.category is not None:
            cat_label = self.category.label

        return f"<Expense[{self.id}]({self.user_id}:" \
               f" {self.label}" \
               f" {cat_label}" \
               f" {self.amount}" \
               f")>"

    def map(self):
        category = None
        if self.category_id is not None:
            category = {'id': self.category_id, 'label': self.category.label}
        return {
            'id': self.id,
            'label': self.label,
            'category': category,
            'amount': self.amount
        }
