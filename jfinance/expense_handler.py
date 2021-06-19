import datetime
import json
import tornado.web
import jfinance
from .category_mapper import CategoryMapper
from .expense_mapper import ExpenseMapper
from .user_mapper import UserMapper


def get_expense_object(user_id):
    expense_list = []
    expenses = jfinance.sql_data.get_expenses(user_id)
    for exp in expenses:
        # print(f"DB:- {exp.map()}")
        expense_list.append(exp.map())
    return expense_list


def get_balance_object(user: UserMapper):
    expenses = jfinance.sql_data.get_expenses(user.id)
    transactions = jfinance.sql_data.get_transactions(user.id, user.budget_date)

    expense_list = []
    for exp in expenses:
        # print(f"DB:- {exp.map()}")
        expense_list.append(exp.map())

    if user.budget_initial and len(user.budget_initial) > 1:
        for exp in expense_list:
            for init_exp in json.loads(user.budget_initial):
                if init_exp['id'] == exp['id']:
                    exp['amount'] += init_exp['amount']

    for exp in expense_list:
        # print(f"{exp['id']}, {exp['amount']}")
        for t in transactions:
            if t.expense_id == exp['id']:
                exp['amount'] -= t.amount

    return expense_list


def init_next_month(user: UserMapper):
    expense_list = get_balance_object(user)
    budget_initital = []
    for exp in expense_list:
        if exp['amount'] != 0:
            budget_initital.append({'id': exp['id'], 'amount': exp['amount']})

    # print(f"{budget_initital}")
    user.budget_initial = json.dumps(budget_initital)
    user.budget_date = datetime.datetime.utcnow()
    print(f"Initialize next month: {user}")
    jfinance.sql_session.commit()


class ExpenseHandler(jfinance.BaseHandler):
    @tornado.web.authenticated
    def get(self):
        user_id = int(self.get_secure_cookie("user_id"))
        calculate_balance = self.get_argument("balance", default="")
        user = jfinance.sql_data.get_user(user_id)

        if len(calculate_balance) and calculate_balance == "true":
            expenses = get_balance_object(user)
        else:
            expenses = get_expense_object(user_id)

        json_obj = {'user_id': user_id, 'income': user.budget_amount, 'expenses': expenses}
        # print(json_obj)
        self.write(json.dumps(json_obj))

    @tornado.web.authenticated
    def post(self):
        user_id = int(self.get_secure_cookie("user_id"))
        user = jfinance.sql_data.get_user(user_id)
        arg_dict = tornado.escape.json_decode(self.request.body)
        # print(f"POST[{user_id}] args: {arg_dict}")

        for key, item in arg_dict.items():
            if key == "next_month":
                init_next_month(user)
                json_obj = {'user_id': user_id, 'next_month': True}
                self.write(json.dumps(json_obj))
                return

            elif key == "income":
                print(f"Update income: {item}")
                user.budget_amount = float(item)
            else:
                amount = 0
                if 'amount' in item and len(item['amount']) > 0:
                    amount = float(item['amount'])
                if 'c' in key:
                    cat_id = key[1:]
                    cat = jfinance.sql_data.get_category(cat_id)
                    if cat is None:
                        cat = CategoryMapper(user_id=user_id, label=item['label'])
                        print(f"Create C {item['label']} -> {cat}")
                        jfinance.sql_session.add(cat)
                    else:
                        print(f"Update C {item['label']} -> {cat}")
                        cat.label = item['label']

                    for sub_key, sub_item in item['expenses'].items():
                        amount = 0
                        if len(sub_item['amount']) > 0:
                            amount = float(sub_item['amount'])
                        if sub_key == 'n':
                            if len(sub_item['label']) > 2:
                                exp = ExpenseMapper(user_id=user_id, label=sub_item['label'],
                                                    amount=amount,
                                                    category=cat)
                                jfinance.sql_session.add(exp)
                                print(f"Create E{sub_key} -> {exp}")
                        else:
                            exp = jfinance.sql_data.get_expense(sub_key)
                            if exp is None:
                                exp = ExpenseMapper(user_id=user_id, label=sub_item['label'],
                                                    amount=amount,
                                                    category=cat)
                                jfinance.sql_session.add(exp)
                                print(f"Created sE{sub_key} -> {exp}")
                            else:
                                print(f"Update sE{sub_key} -> {exp}")
                                exp.label = sub_item['label']
                                exp.amount = amount

                else:
                    if key == 'n':
                        if len(item['label']) > 2:
                            if amount > 0:
                                exp = ExpenseMapper(user_id=user_id, label=item['label'], amount=amount)
                                jfinance.sql_session.add(exp)
                                print(f"Create {item['label']} -> {exp} ")
                            else:
                                cat = CategoryMapper(user_id=user_id, label=item['label'])
                                print(f"Create C {item['label']} -> {cat}")
                                jfinance.sql_session.add(cat)
                                exp = ExpenseMapper(user_id=user_id, label="Sub Expense",
                                                    amount=0,
                                                    category=cat)
                                jfinance.sql_session.add(exp)

                    else:
                        exp = jfinance.sql_data.get_expense(key)
                        if exp is None:
                            exp = ExpenseMapper(user_id=user_id, label=item['label'], amount=amount)
                            jfinance.sql_session.add(exp)
                            print(f"Created E{key} -> {exp}")
                        else:
                            print(f"Update E{key} -> {exp}")
                            exp.label = item['label']
                            exp.amount = amount

        jfinance.sql_session.commit()

        expense_obj = get_expense_object(user_id)
        json_obj = {'user_id': user_id, 'income': user.budget_amount, 'expenses': expense_obj}
        self.write(json.dumps(json_obj))
