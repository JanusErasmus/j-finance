import logging
import sqlite3
import pprint
import sys

pp = pprint.PrettyPrinter(indent=2)
logger = logging
conn = sqlite3.connect('./jfin_data/jfin.db')

class Transaction:
    def __init__(self, date, category, description, amount):
        self.date = date
        self.category = category
        self.description = description
        self.amount = amount

    def __repr__(self):
        return str(self.__dict__)

def get_categories():
    categories = []
    cursor = conn.execute("SELECT category FROM categories")

    for row in cursor:
        for cat in row:
            categories.append(cat)


    logger.debug(f"{categories}")
                
    return categories

def get_transactions(user_id):
    transactions = []
    cursor = conn.execute(f"select date, category, desc, amount from transactions "
            f"left join users, categories on transactions.user_id = users.id and "
           f"categories.id = transactions.cat_id where users.id={user_id}")

    for row in cursor:
        t = Transaction(row[0], row[1], row[2], row[3])
        transactions.append(t)

                
    return transactions