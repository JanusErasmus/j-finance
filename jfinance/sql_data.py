from sqlalchemy.orm import sessionmaker
from sqlalchemy import create_engine
from sqlalchemy.exc import IntegrityError
from .sql_base import Base
from .category_mapper import CategoryMapper
from .expense_mapper import ExpenseMapper

engine = create_engine('sqlite:///jfin.db', echo=False)
Base.metadata.create_all(engine)
session_class = sessionmaker(bind=engine)
sql_session = session_class()


def commit():
    try:
        sql_session.commit()
    except IntegrityError:
        sql_session.rollback()
        print("SQL error, could NOT commit")


def add_expense(user_id: int, label: str, category: CategoryMapper, amount: float):
    expense = ExpenseMapper(user_id=user_id,
                            label=label,
                            category=category,
                            amount=amount)
    sql_session.add(expense)
    print(f"Adding {expense}")
    commit()


def add_category(user_id: int, label: str):
    cat = CategoryMapper(user_id=user_id, label=label)
    sql_session.add(cat)

    print(f"Adding {cat}")
    commit()


def get_categories(user_id: int):
    return sql_session.query(CategoryMapper).filter(CategoryMapper.user_id == user_id).all()


def get_category(cat_id: int):
    return sql_session.query(CategoryMapper).filter(CategoryMapper.id == cat_id).first()


def get_expenses(user_id: int):
    return sql_session.query(ExpenseMapper).filter(ExpenseMapper.user_id == user_id).all()
