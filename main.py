#!python
import tornado.web
import logging
import logging.config
import os
import ast 
import pprint
import signal
import copy
import json

# logging.config.fileConfig('logging.ini')
logging.basicConfig(level=logging.DEBUG)

import jfin_data
pp = pprint.PrettyPrinter(indent=2)


logger = logging.getLogger(__name__)

class BaseHandler(tornado.web.RequestHandler):
    def get_current_user(self):
        return self.get_secure_cookie("user")

class LoginHandler(BaseHandler):
    def get(self):
        incorrect = self.get_secure_cookie("incorrect")
        if incorrect and int(incorrect) > 20:
            self.write('<center>blocked</center>')
            return
        self.render('www/login.html', username=self.get_secure_cookie("user"))

    def post(self):
        incorrect = self.get_secure_cookie("incorrect")
        if incorrect and int(incorrect) > 20:
            self.write('<center>blocked</center>')
            return
        
        getusername = tornado.escape.xhtml_escape(self.get_argument("username"))
        getpassword = tornado.escape.xhtml_escape(self.get_argument("password"))
        if "janus" == getusername and "j$anus2564" == getpassword:
            user_id = 1
            self.set_secure_cookie("user", self.get_argument("username"))
            self.set_secure_cookie("incorrect", "0")         
            self.set_secure_cookie("user_id", str(user_id))
            self.redirect(self.get_argument("next", self.reverse_url("index"))) # self.redirect("/")
        elif "herman" == getusername and "h$erman" == getpassword:
            user_id = 2
            self.set_secure_cookie("user", self.get_argument("username"))
            self.set_secure_cookie("incorrect", "0")
            self.set_secure_cookie("user_id", str(user_id))
            self.redirect("/")
        elif "teddy" == getusername and "t$eddy" == getpassword:
            user_id = 3
            self.set_secure_cookie("user", self.get_argument("username"))
            self.set_secure_cookie("incorrect", "0")
            self.set_secure_cookie("user_id", str(user_id))
            self.redirect("/")
        else:
            incorrect = self.get_secure_cookie("incorrect") or 0
            increased = str(int(incorrect)+1)
            self.set_secure_cookie("incorrect", increased)
            self.write("""<center>
                            Something Wrong With Your Data (%s)<br />
                            <a href="/">Go Home</a>
                          </center>""" % increased)


class LogoutHandler(BaseHandler):
    def get(self):
        self.clear_cookie("user")
        self.redirect("/")


class MainHandler(BaseHandler):
    @tornado.web.authenticated
    def get(self):
        user_id = int(self.get_secure_cookie("user_id"))
        # logger.debug(f"User: {user_id}")
        self.render('www/index.html', username=self.get_secure_cookie("user"), categories=jfin_data.get_summary(user_id))


class AddHandler(BaseHandler):
    @tornado.web.authenticated
    def get(self):
        user_id = int(self.get_secure_cookie("user_id"))
        groups = jfin_data.get_groups(user_id)
        cats = jfin_data.get_categories(user_id)        
        self.render('www/add.html', username=self.get_secure_cookie("user"), groups=json.dumps(groups), categories=json.dumps(cats))

    @tornado.web.authenticated
    def post(self):
        user_id = int(self.get_secure_cookie("user_id"))
        # print(f"POST args: {self.request.arguments}")
        group = self.get_body_argument("group")
        category = self.get_body_argument("category")
        # desc = self.get_body_argument("description")
        amount = self.get_body_argument("amount")
        # # logger.debug(f"Adding: {cat_id}, {desc}, {amount}")
        jfin_data.add_transaction(user_id, group, category, " ", amount)
        self.render('www/index.html', username=self.get_secure_cookie("user"), categories=jfin_data.get_summary(user_id))

class TransactionsHandler(BaseHandler):
    @tornado.web.authenticated
    def get(self):
        user_id = int(self.get_secure_cookie("user_id"))
        self.render('www/transactions.html', username=self.get_secure_cookie("user"), transactions=jfin_data.get_transactions(user_id))


class CategoriesHandler(BaseHandler):
    @tornado.web.authenticated
    def get(self):
        user_id = int(self.get_secure_cookie("user_id"))
        self.render('www/categories.html',
            username=self.get_secure_cookie("user"),
            groups=jfin_data.get_groups(user_id),
            categories=jfin_data.get_categories(user_id))

    @tornado.web.authenticated
    def post(self):
        user_id = int(self.get_secure_cookie("user_id"))
        arg_dict = self.request.arguments
        print(f"POST args: {arg_dict}")
        key = list(arg_dict.keys())[0]
        print(f"POST key: {key}")

        if "add_group" in key:
            group = str(arg_dict['add_group'][0], 'utf-8')
            print(f"POST: Add G {group}")
            jfin_data.add_group(user_id, group)

        if "rm_group" in key:
            group = key.split('_')[2]
            print(f"POST: rm Group {group}")

        if "rm_cat" in key:
            cat = key.split('_')[2]
            print(f"POST: rm Category {cat}")
        
        if "add_cat" in key:
            group = str(arg_dict['add_cat_group'][0], 'utf-8')
            name = str(arg_dict['add_cat_name'][0], 'utf-8')
            print(f"POST: Add C {group}, {name}")
            jfin_data.add_category(user_id, name, group_name=group)
            # logger.debug(f"Added: {a}")

        self.render('www/categories.html',
            username=self.get_secure_cookie("user"),
            groups=jfin_data.get_groups(user_id),
            categories=jfin_data.get_categories(user_id))

class CityHandler(BaseHandler):
    _cities = [{'name': "Hendrina",
                'description': "Groot deel van Laerskool"
                },
               {'name': "Pretoria",
                'description': "Universiteit en werk"
                },
               {'name': "Johannesburg",
                'description': "Nee, nee. Wil nie soontoe gaan nie"
                },
               {'name': "Middelburg",
                'description': "Lekker koshuis lewe"
                },
               {'name': "Silverton",
                'description': "Dreimans bier is hier"
                },
               {'name': "Rietfontein",
                'description': "Ek bly nou hier"
                }]

    @tornado.web.authenticated
    def get(self):
        logger.debug(f"Init Cities")
        self.render('www/cities.html', username=self.get_secure_cookie("user"), cities=self._cities)


class BudgetHandler(BaseHandler):
    @tornado.web.authenticated
    def get(self):
        user_id = int(self.get_secure_cookie("user_id"))
        self.render('www/budget.html', username=self.get_secure_cookie("user"), budget=jfin_data.get_budget(user_id))

    @tornado.web.authenticated
    def post(self):
        user_id = int(self.get_secure_cookie("user_id"))
        arg_dict = self.request.arguments
        
        for category in self.request.arguments:
            index = category.split("_")[1]
            amount = float(arg_dict[category][0].decode('utf-8'))
            jfin_data.update_category(user_id, index, amount)

        self.render('www/budget.html', username=self.get_secure_cookie("user"), budget=jfin_data.get_budget(user_id))


def sig_keyboard_int(sig, frame):
    logger.warning('Caught signal: %s', sig)
    tornado.ioloop.IOLoop.instance().add_callback_from_signal(shutdown)


def shutdown():
    global httpServer
    logger.warning('shutting down')
    
    if httpServer is not None:
        logger.warning('Stopping http server')
        httpServer.stop()
    else:
        logger.error('http server None')

    tornado.ioloop.IOLoop.instance().stop()


def main():
    port = 8080
    logger.debug("Hello")
    
    global httpServer
    loc = os.path.dirname(os.path.realpath(__file__)) + "/www/"   # get the full path of where we are

    settings = {
        'cookie_secret': 'bZJc2sWbQLKos6GkHn/VB9oXwQt8S0R0kRvJ5/xJ89E=',
        'login_url': '/login',
        'static_path': os.path.join(loc, 'static'),
        'xsrf_cookies': False,
        #         'compiled_template_cache': False,
        #         'debug':True,
    }

    logger.debug(f"HTTP Server dir: {loc}")

    app = tornado.web.Application(
        handlers=[
            tornado.web.url(r"/login", LoginHandler),
            tornado.web.url(r"/logout", LogoutHandler),
            tornado.web.url(r"/", MainHandler, name="index"),
            tornado.web.url(r"/add", AddHandler, name="add"),
            tornado.web.url(r"/transactions", TransactionsHandler, name="trans"),
            tornado.web.url(r"/categories", CategoriesHandler, name="cats"),
            tornado.web.url(r"/budget", BudgetHandler, name="bud"),
            tornado.web.url(r"/cities", CityHandler, name="city"),

            (r"/(.*\.ico)",  tornado.web.StaticFileHandler, {"path": loc}),      # allow all .ico files (for favicon)
            (r"/(.*\.js)",   tornado.web.StaticFileHandler, {"path": loc}),      # allow all javascript files
            (r"/(.*\.css)",  tornado.web.StaticFileHandler, {"path": loc}),      # allow all stylesheets
            (r"/(.*\.png)",  tornado.web.StaticFileHandler, {"path": loc}),      # allow all png files
            (r"/(.*\.gif)",  tornado.web.StaticFileHandler, {"path": loc}),      # allow all gif files
            (r"/(.*\.jpg)",  tornado.web.StaticFileHandler, {"path": loc}),      # allow all jpegs
            (r"/(.*\.pdf)",  tornado.web.StaticFileHandler, {"path": loc}),      # allow pdf files (user manual)
            (r"/(.*\.html)", tornado.web.StaticFileHandler, {"path": loc})       # allow all html files
        ], **settings
    )
    httpServer = tornado.httpserver.HTTPServer(app)
    httpServer.listen(port)
    logger.debug(f"Web Server Started on Port: {port}")
    
    tornado.ioloop.IOLoop.instance().start()


if __name__ == "__main__":
    signal.signal(signal.SIGINT, sig_keyboard_int)       # create callback to capture keyboard interrupt
    main()
    # jfin_data.get_summary(1)
    logger.info("DONE")
