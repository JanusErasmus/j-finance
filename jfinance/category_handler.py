import tornado.web
import jfinance


class CategoriesHandler(jfinance.BaseHandler):
    @tornado.web.authenticated
    def get(self):
        user_id = int(self.get_secure_cookie("user_id"))
        # jfinance.sql_data.add_category(user_id, "Poos")
        # categories = jfinance.sql_data.get_categories(user_id)
        self.render('www/categories.html')

    @tornado.web.authenticated
    def post(self):
        user_id = int(self.get_secure_cookie("user_id"))
        arg_dict = self.request.arguments
        print(f"POST[{user_id}] args: {arg_dict}")

        self.render('www/categories.html')
