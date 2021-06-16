from sqlalchemy.orm import sessionmaker
from sqlalchemy import create_engine
from .sql_base import Base
from .category_mapper import CategoryMapper


engine = create_engine('sqlite:///jfin.db', echo=False)
Base.metadata.create_all(engine)
session_class = sessionmaker(bind=engine)
sql_session = session_class()


def add_category(user_id: int, label: str):
    cat = CategoryMapper(user_id=user_id, label=label)
    sql_session.add(cat)
    print(f"Adding {cat}")


def get_categories(user_id: int):
    return sql_session.query(CategoryMapper).filter(CategoryMapper.user_id == user_id).all()
