import tornado.web
import jfinance


class TransactionsHandler(jfinance.BaseHandler):
    @tornado.web.authenticated
    def get(self):
        user_id = int(self.get_secure_cookie("user_id"))
        self.render('www/transactions.html')


class AddHandler(jfinance.BaseHandler):
    @tornado.web.authenticated
    def get(self):
        user_id = int(self.get_secure_cookie("user_id"))
        self.render('www/add.html')

    @tornado.web.authenticated
    def post(self):
        user_id = int(self.get_secure_cookie("user_id"))
        print(f"POST[{user_id}] args: {self.request.arguments}")
        self.render('www/index.html')
