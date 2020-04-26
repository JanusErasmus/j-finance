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
        cursor = conn.execute(f"SELECT id, name FROM groups where user_id={user_id} order by name")
        for row in cursor:
            entry = {}
            entry['id'] = row[0]
            entry['name'] = row[1]
            groups.append(entry)
    else:
        cursor = conn.execute(f"SELECT id, name FROM groups where user_id={user_id} order by name")
        for row in cursor:
            entry = {}
            entry['id'] = row[0]
            entry['name'] = row[1]
            groups.append(entry)
    return groups

def get_categories(user_id, obj=False):
    categories = []

    if obj:
        cursor = conn.execute(f"SELECT categories.id, groups.name, category FROM categories left join groups on groups.id = group_id where categories.user_id={user_id} order by category")
        for row in cursor:
            # entry = {}
            # entry['id'] = row[0]
            # entry['name'] = row[1]
            # categories.append(entry)
            categories.append(Category(row[0], row[1], row[2], 0))
    else:
        cursor = conn.execute(f"SELECT categories.id, groups.name, category FROM categories left join groups on groups.id = group_id where categories.user_id={user_id} order by groups.name")
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
    cursor = conn.execute(f"select categories.id, category, groups.name from categories "
            f"left join groups on categories.group_id = groups.id")

    cats = {}
    for row in cursor:
        cats[row[0]] = {'name': row[1], 'group': row[2]}

    # pp.pprint(cats)

    cursor = conn.execute(f"select date, cat_id, desc, amount from transactions "
            f"where user_id={user_id} "
            f"order by date desc")

    transactions = []
    for row in cursor:
        pp.pprint(row)
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
        cursor = conn.execute(f"select cat_id, groups.name, category, sum(amount) from transactions "
                f"left join categories, groups on "
                f"categories.id = transactions.cat_id and groups.id = categories.group_id where transactions.user_id={user_id} and transactions.cat_id={cat.id} "
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

    # summary_js = {}
    # for cat in summary:
    #     if cat.group not in summary_js:
    #         summary_js[cat.group] = []
    #     summary_js[cat.group].append({'name': cat.name, 'amount': cat.sum})
    # pp.pprint(summary_js)
    
    return summary


def add_transaction(user_id, group, category, description, amount):

    category_id = None
    group_id = None

    # Get correct category_id from group and category pair
    cursor = conn.execute(f"SELECT id FROM groups WHERE name = \'{group}\' and user_id = \'{user_id}\'")
    for row in cursor:
        group_id = row[0]
    
    
    logger.debug(f"Found group ID for {group} = {group_id}")

    if group_id is not None:
        cursor = conn.execute(f"SELECT id FROM categories WHERE group_id = \'{group_id}\' and category = \'{category}\' and user_id = \'{user_id}\'")
        for row in cursor:
            category_id = row[0]
    
    if category_id is None:
        return

    logger.debug(f"Found category ID {category_id}")

    conn.execute(f"INSERT INTO transactions (user_id, cat_id, desc, amount) values({user_id},\'{category_id}\', \'{description}\', {amount})")    
    conn.commit()