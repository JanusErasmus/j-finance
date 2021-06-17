import tornado.web
import jfinance
import json
from .category_mapper import CategoryMapper
from .expense_mapper import ExpenseMapper


def parse_expenses(expense_obj):
    expenses = []
    for key, item in expense_obj.items():
        print(f"SUB: {key} : {item}")
        if key == 'n' and len(item['label']) > 2:
            expenses.append({'label': item['label'], 'amount': item['amount']})

    if len(expenses) == 0:
        return None

    return expenses


def get_expense_object(user_id):
    expense_list = []
    expenses = jfinance.sql_data.get_expenses(user_id)
    for exp in expenses:
        # print(f"DB:- {exp.map()}")
        expense_list.append(exp.map())
    return expense_list


class ExpenseHandler(jfinance.BaseHandler):
    @tornado.web.authenticated
    def get(self):
        user_id = int(self.get_secure_cookie("user_id"))
        json_obj = {'user_id': user_id, 'expenses': get_expense_object(user_id)}
        # print(json_obj)
        self.write(json.dumps(json_obj))

    @tornado.web.authenticated
    def post(self):
        user_id = int(self.get_secure_cookie("user_id"))
        arg_dict = tornado.escape.json_decode(self.request.body)
        # print(f"POST[{user_id}] args: {arg_dict}")

        for key, item in arg_dict.items():
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

        expense_obj = get_expense_object(user_id)
        json_obj = {'user_id': user_id, 'expenses': expense_obj}
        jfinance.sql_session.commit()
        self.write(json.dumps(json_obj))
