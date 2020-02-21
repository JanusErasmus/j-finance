import logging
import sqlite3
import pprint
import sys

pp = pprint.PrettyPrinter(indent=2)
logger = logging.getLogger(__name__)
logger.setLevel(logging.DEBUG)

conn = sqlite3.connect('./jfin_data/jfin.db')

class Transaction:
    def __init__(self, date, category, description, amount):
        self.date = date
        self.category = category
        self.description = description
        self.amount = amount

    def __repr__(self):
        return str(self.__dict__)

class Category:
    def __init__(self, id, name, sum):
        self.id = id
        self.name = name
        self.sum = sum
        
    def __repr__(self):
        return str(self.__dict__)

def get_categories(obj=False):
    categories = []

    if obj:
        cursor = conn.execute("SELECT id, category FROM categories")
        for row in cursor:
            # entry = {}
            # entry['id'] = row[0]
            # entry['name'] = row[1]
            # categories.append(entry)
            categories.append(Category(row[0], row[1], 0))
    else:
        cursor = conn.execute("SELECT category FROM categories")
        for row in cursor:
            categories.append(row[0])

    return categories

def add_category(category):
    cursor = conn.execute(f"INSERT INTO categories (category) values(\'{category}\')")
    pp.pprint(cursor)
    conn.commit()

def get_transactions(user_id):
    transactions = []
    cursor = conn.execute(f"select date, category, desc, amount from transactions "
            f"left join users, categories on transactions.user_id = users.id and "
           f"categories.id = transactions.cat_id where users.id={user_id}")

    for row in cursor:
        t = Transaction(row[0], row[1], row[2], row[3])
        transactions.append(t)

    return transactions

def get_summary(user_id):
    logger.debug("Get summary")
    summary = []
    cat_objs = get_categories(obj=True)

    for cat in cat_objs:
        # pp.pprint(id)
        name = [c.name for c in cat_objs if c.id == cat.id][0]
    
        cursor = conn.execute(f"select cat_id, category, sum(amount) from transactions "
                f"left join categories on "
                f"categories.id = transactions.cat_id where user_id={user_id} and transactions.cat_id={cat.id}")

        for row in cursor:
            if row[0] is not None:
                # pp.pprint(row[0])
                c = Category(row[0], row[1], row[2])
            else:
                c = Category(-1, name, 0)
            summary.append(c)

    # pp.pprint(summary)
    return summary


def get_budget(user_id):
    budget = []
    cursor = conn.execute(f"select cat_id, category, amount from budget left join categories on categories.id = budget.cat_id where user_id={user_id}")
    for row in cursor:
        if row[0] is not None:
            # pp.pprint(row[0])
            c = Category(row[0], row[1], row[2])
            budget.append(c)
    
    return budget

def add_budget(user_id, category, amount):
    cursor = conn.execute(f"INSERT INTO categories (category) values(\'{category}\')")
    pp.pprint(cursor)
    conn.commit()

def update_category(user_id, category_index, amount):
    cursor = conn.execute(f"UPDATE budget SET amount={amount} WHERE user_id={user_id} and cat_id={category_index}")
    conn.commit()