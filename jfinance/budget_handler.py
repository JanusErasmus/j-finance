import tornado.web
import jfinance


class BudgetHandler(jfinance.BaseHandler):
    @tornado.web.authenticated
    def get(self):
        user_id = int(self.get_secure_cookie("user_id"))
        self.render('www/budget.html')

    @tornado.web.authenticated
    def post(self):
        user_id = int(self.get_secure_cookie("user_id"))
        arg_dict = self.request.arguments
        print(f"POST[{user_id}] args: {arg_dict}")

        self.render('www/budget.html')
