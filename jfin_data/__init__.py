import logging
import sqlite3
import pprint
import sys

pp = pprint.PrettyPrinter(indent=2)
logger = logging.getLogger(__name__)

conn = sqlite3.connect('./jfin_data/jfin.db')

class Transaction:
    def __init__(self, date, group, category, description, amount):
        self.date = date
        self.group = group
        self.category = category
        self.description = description
        self.amount = amount

    def __repr__(self):
        return str(self.__dict__)

class Category:
    def __init__(self, id, group, name, sum):
        self.id = id
        self.group = group
        self.name = name
        self.sum = sum
        
    def __repr__(self):
        return str(self.__dict__)


def get_budget(user_id):
    budget = []
    cursor = conn.execute(f"select cat_id, groups.name, category, amount from budget "
                f"left join categories, groups "
                f"on categories.id = budget.cat_id "
                f"and categories.group_id = groups.id "
                f"where budget.user_id={user_id} "
                f"order by groups.name")
    for row in cursor:
        if row[0] is not None:
            # pp.pprint(row)
            c = Category(row[0], row[1], row[2], row[3])
            budget.append(c)
    
    return budget


def add_budget(user_id, category_id, amount):
    cursor = conn.execute(f"INSERT INTO budget (user_id, cat_id, amount) values({user_id},\'{category_id}\', {amount})")
    pp.pprint(cursor)
    conn.commit()


def update_category(user_id, category_index, amount):
    cursor = conn.execute(f"UPDATE budget SET amount={amount} WHERE user_id={user_id} and cat_id={category_index}")
    conn.commit()

def get_groups(user_id, obj=False):
    groups = []

    if obj:
        cursor = conn.execute(f"SELECT id, name FROM groups where user_id={user_id}")
        for row in cursor:
            entry = {}
            entry['id'] = row[0]
            entry['name'] = row[1]
            groups.append(entry)
    else:
        cursor = conn.execute(f"SELECT id, name FROM groups where user_id={user_id}")
        for row in cursor:
            entry = {}
            entry['id'] = row[0]
            entry['name'] = row[1]
            groups.append(entry)
    return groups

def get_categories(user_id, obj=False):
    categories = []

    if obj:
        cursor = conn.execute(f"SELECT id, group_id, category FROM categories where user_id={user_id}")
        for row in cursor:
            # entry = {}
            # entry['id'] = row[0]
            # entry['name'] = row[1]
            # categories.append(entry)
            categories.append(Category(row[0], row[1], row[2], 0))
    else:
        cursor = conn.execute(f"SELECT categories.id, groups.name, category FROM categories left join groups on groups.id = group_id where categories.user_id={user_id}")
        for row in cursor:
            entry = {}
            entry['id'] = row[0]
            entry['group'] = row[1]
            entry['name'] = row[2]
            categories.append(entry)

    return categories

def add_category(user_id, category, group_id=None, group_name=None):
    if group_name is not None:
        cursor = conn.execute(f"SELECT id FROM groups where user_id={user_id} and name=\'{group_name}\'")
        for row in cursor:
            group_id = row[0]
            print(f"Goup ID is: {group_id}")

    if group_id is not None:
        conn.execute(f"INSERT INTO categories (user_id, group_id, category) values({user_id}, {group_id}, \'{category}\')")
        cursor = conn.execute(f"select id from categories where category=\'{category}\' and user_id={user_id}") #SELECT id from categories where category={category}")
        conn.commit()
    else:    
        conn.execute(f"INSERT INTO categories (user_id, category) values({user_id}, \'{category}\')")
        cursor = conn.execute(f"select id from categories where category=\'{category}\' and user_id={user_id}") #SELECT id from categories where category={category}")
        conn.commit()

    for row in cursor:
        logger.debug(row[0])
        add_budget(user_id, row[0], 0)

def add_group(user_id, group):
    conn.execute(f"INSERT INTO groups (user_id, name) values({user_id}, \'{group}\')")
    conn.commit()
    

def get_transactions(user_id):
    transactions = []
    cursor = conn.execute(f"select categories.id, category, groups.name from categories "
            f"left join groups on categories.group_id = groups.id")

    cats = {}
    for row in cursor:
        cats[row[0]] = {'name': row[1], 'group': row[2]}

    # pp.pprint(cats)

    cursor = conn.execute(f"select date, cat_id, desc, amount from transactions "
            f"left join users on transactions.user_id = users.id "
            f"where users.id={user_id} "
            f"order by date desc")

    for row in cursor:
        # pp.pprint(row)
        group = cats[row[1]]['group']
        category = cats[row[1]]['name']
        t = Transaction(row[0][:10], group, category, row[2], row[3])
        transactions.append(t)

    return transactions

def get_summary(user_id):
    summary = []

    # Get categories credit
    budget = get_budget(user_id)
    # pp.pprint(budget)

    # Query each categories transactions debit
    cat_objs = get_categories(user_id, obj=True)
    for cat in cat_objs:           
        cursor = conn.execute(f"select cat_id, group_id, category, sum(amount) from transactions "
                f"left join categories on "
                f"categories.id = transactions.cat_id where transactions.user_id={user_id} and transactions.cat_id={cat.id} "
                f"order by transactions.cat_id ")

        for row in cursor:
            if row[0] is not None:
                # pp.pprint(row[0])
                c = Category(row[0], row[1], row[2], row[3])
            else:
                c = Category(cat.id, cat.group, cat.name, 0)
            summary.append(c)

    # Calculate available amount for each category, and store in sum of that category
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
    # index.js use a list of category objects {'name': <>, 'amount': <>, 'cats':[<>,<>]},
    # where cats is a list optional category objects which can again have cats
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