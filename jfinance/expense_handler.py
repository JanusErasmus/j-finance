import tornado.web
import jfinance
import json


class ExpenseHandler(jfinance.BaseHandler):
    @tornado.web.authenticated
    def get(self):
        user_id = int(self.get_secure_cookie("user_id"))
        expenses = jfinance.sql_data.get_expenses(user_id)
        json_obj = {'user_id': user_id, 'expenses': {}}
        for e in expenses:
            json_obj['expenses'].append(e.map())

        json_obj['expenses'] = {'1': {'label': "Sakgeld", 'amount': 5300, 'expenses': None},
                                '2': {'label': "Noodsaaklik", 'amount': 140, 'expenses': {
                                    '3': {'label': "OUTsurance", 'amount': 140, 'expenses': None},
                                    '4': {'label': "Profmed", 'amount': 250, 'expenses': None},
                                    '5': {'label': "PPS", 'amount': 360, 'expenses': None}}
                                }}
        print(json_obj)
        self.write(json.dumps(json_obj))

    @tornado.web.authenticated
    def post(self):
        user_id = int(self.get_secure_cookie("user_id"))
        arg_dict = tornado.escape.json_decode(self.request.body)
        print(f"POST[{user_id}] args: {arg_dict}")

        json_obj = {'user_id': user_id, 'expenses': {}}
        json_obj['expenses'] = {'1': {'label': "Sakgeld", 'amount': 5300, 'expenses': None},
                                '2': {'label': "Noodsaaklik", 'amount': 140, 'expenses': {
                                    '3': {'label': "OUTsurance", 'amount': 140, 'expenses': None},
                                    '4': {'label': "Profmed", 'amount': 250, 'expenses': None},
                                    '5': {'label': "PPS", 'amount': 360, 'expenses': None}}
                                }}
        self.write(json.dumps(json_obj))
