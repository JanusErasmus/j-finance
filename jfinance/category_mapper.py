from .sql_base import Base
from sqlalchemy import Column, Integer, String


class CategoryMapper(Base):
    __tablename__ = 'categories'

    id = Column(Integer, primary_key=True)
    label = Column(String)
    user_id = Column(Integer)

    def __repr__(self):
        return f"<Category[{self.id}]({self.user_id}:" \
               f" '{self.label}'" \
               f")>"

    def map(self):
        return {
            'user_id': self.user_id,
            'label': self.label
        }
