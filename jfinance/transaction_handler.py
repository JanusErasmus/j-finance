import tornado.web
import jfinance
from .transaction_mapper import TransactionMapper


class TransactionHandler(jfinance.BaseHandler):
    @tornado.web.authenticated
    def get(self):
        user_id = int(self.get_secure_cookie("user_id"))
        user = jfinance.sql_data.get_user(user_id)
        transactions = jfinance.sql_data.get_transactions(user_id, user.budget_date)
        trans_list = []
        for trans in transactions:
            trans_list.append(trans.map())
        self.render('www/transactions.html', date_string=user.date_string(), transactions=trans_list)

    @tornado.web.authenticated
    def post(self):
        user_id = int(self.get_secure_cookie("user_id"))
        user = jfinance.sql_data.get_user(user_id)
        # arg_dict = tornado.escape.json_decode(self.request.body)
        exp_id = self.get_argument("exp_id", default="")
        desc = self.get_argument("desc", default="")
        amount = self.get_argument("amount", default="")

        exp = None
        if len(exp_id) > 0:
            exp = jfinance.sql_data.get_expense(int(exp_id))
            if exp.category is not None:
                desc = exp.category.label

        if exp is not None:
            # print(f"T POST[{user_id}] trans: {exp_id} {desc} {amount}")
            trans = TransactionMapper(user_id=user_id, description=desc, expense=exp, amount=amount)
            print(f"Add {trans}")
            jfinance.sql_session.add(trans)
            jfinance.sql_session.commit()
            self.render('www/index.html', date_string=user.date_string())

