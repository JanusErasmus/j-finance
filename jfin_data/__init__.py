import logging
import sqlite3
import pprint
import sys

pp = pprint.PrettyPrinter(indent=2)
logger = logging.getLogger(__name__)

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


def get_budget(user_id):
    budget = []
    cursor = conn.execute(f"select cat_id, category, amount from budget left join categories on categories.id = budget.cat_id where budget.user_id={user_id} "
                f"order by cat_id ")
    for row in cursor:
        if row[0] is not None:
            # pp.pprint(row[0])
            c = Category(row[0], row[1], row[2])
            budget.append(c)
    
    return budget


def add_budget(user_id, category_id, amount):
    cursor = conn.execute(f"INSERT INTO budget (user_id, cat_id, amount) values({user_id},\'{category_id}\', {amount})")
    pp.pprint(cursor)
    conn.commit()


def update_category(user_id, category_index, amount):
    cursor = conn.execute(f"UPDATE budget SET amount={amount} WHERE user_id={user_id} and cat_id={category_index}")
    conn.commit()


def get_categories(user_id, obj=False):
    categories = []

    if obj:
        cursor = conn.execute(f"SELECT id, category FROM categories where user_id={user_id}")
        for row in cursor:
            # entry = {}
            # entry['id'] = row[0]
            # entry['name'] = row[1]
            # categories.append(entry)
            categories.append(Category(row[0], row[1], 0))
    else:
        cursor = conn.execute(f"SELECT category FROM categories where user_id={user_id}")
        for row in cursor:
            categories.append(row[0])

    return categories

def add_category(user_id, category):
    conn.execute(f"INSERT INTO categories (user_id, category) values({user_id}, \'{category}\')")
    cursor = conn.execute(f"select id from categories where category=\'{category}\' and user_id={user_id}") #SELECT id from categories where category={category}")
    conn.commit()
    for row in cursor:
        logger.debug(row[0])
        add_budget(user_id, row[0], 0)

def get_transactions(user_id):
    transactions = []
    cursor = conn.execute(f"select date, category, desc, amount from transactions "
            f"left join users, categories on transactions.user_id = users.id and "
            f"categories.id = transactions.cat_id where users.id={user_id} "
            f"order by date desc")

    for row in cursor:
        t = Transaction(row[0], row[1], row[2], row[3])
        transactions.append(t)

    return transactions

def get_summary(user_id):
    summary = []
    budget = get_budget(user_id)
    # pp.pprint(budget)
    cat_objs = get_categories(user_id, obj=True)

    for cat in cat_objs:           
        cursor = conn.execute(f"select cat_id, category, sum(amount) from transactions "
                f"left join categories on "
                f"categories.id = transactions.cat_id where transactions.user_id={user_id} and transactions.cat_id={cat.id} "
                f"order by transactions.cat_id ")

        for row in cursor:
            if row[0] is not None:
                # pp.pprint(row[0])
                c = Category(row[0], row[1], row[2])
            else:
                c = Category(cat.id, cat.name, 0)
            summary.append(c)

    # pp.pprint(budget)
    for cat in summary:
        available = 0
        if len(budget) > 0:
            for c in budget:
                # logger.debug(f"try: {c.name}  {c.id} == {cat.id}")
                if c.id == cat.id:
                    available = c.sum
                    # logger.debug(f"Use: {cat.name}: {available}")
                    break
                
        cat.sum = available - cat.sum
        # logger.debug(f"Available for {cat.id}: {cat.sum}")
    # pp.pprint(summary)


    # Use python dictionary key to group sub-categories
    summary_dict = {}
    for cat in summary:
        lst = cat.name.split("_")
        if len(lst) > 1:
            # Check if key for main category was created
            if lst[0] in summary_dict:
                cat.name = lst[1]
                summary_dict[lst[0]].append(cat)
            else:
                summary_dict[lst[0]] = []
                cat.name = lst[1]
                summary_dict[lst[0]].append(cat)
    # pp.pprint(summary_dict)

    
    # Create dictionary used by index.js
    summary_js = []
    for main in summary_dict:
        total = 0
        main_obj = {}
        main_obj['name'] = main
        summary_js.append(main_obj)
        main_obj['cats'] = []
        for sub in summary_dict[main]:
            sub_obj = {}
            sub_obj['name'] = sub.name
            sub_obj['amount'] = sub.sum
            total += sub.sum
            main_obj['cats'].append(sub_obj)
            # pp.pprint(sub_obj)

        main_obj['amount'] = total
    # pp.pprint(summary_js)
    return summary_js


def add_transaction(user_id, category_id, description, amount):    
    cursor = conn.execute(f"INSERT INTO transactions (user_id, cat_id, desc, amount) values({user_id},\'{category_id}\', \'{description}\', {amount})")
    # pp.pprint(cursor)
    conn.commit()