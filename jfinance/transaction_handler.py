import tornado.web
import jfinance


class TransactionsHandler(jfinance.BaseHandler):
    @tornado.web.authenticated
    def get(self):
        user_id = int(self.get_secure_cookie("user_id"))
        self.render('www/transactions.html')

