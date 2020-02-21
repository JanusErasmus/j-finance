#!python
import tornado.web
import logging
import os
import ast 
import pprint

import jfin_data

pp = pprint.PrettyPrinter(indent=2)
logging.basicConfig(level=logging.DEBUG)

logger = logging.getLogger(__name__)
logger.setLevel(logging.DEBUG)


class MainHandler(tornado.web.RequestHandler):
    def get(self):
        self.render('./www/index.html', categories=jfin_data.get_summary(1))


class TransactionsHandler(tornado.web.RequestHandler):
    def get(self):
        self.render('./www/transactions.html', transactions=jfin_data.get_transactions(1))


class CategoriesHandler(tornado.web.RequestHandler):
    def get(self):
        self.render('./www/categories.html', categories=jfin_data.get_categories())

    def post(self):
        a = self.get_body_argument("category_edit")
        print(f"POST: {a}")
        jfin_data.add_category(1, a)
        logger.debug(f"Added: {a}")
        self.render('./www/categories.html', categories=jfin_data.get_categories())


class CityHandler(tornado.web.RequestHandler):
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

    def get(self):
        logger.debug(f"Init Cities")
        self.render('./www/cities.html', cities=self._cities)


class BudgetHandler(tornado.web.RequestHandler):
    def get(self):
        self.render('./www/budget.html', budget=jfin_data.get_budget(1))

    def post(self):
        arg_dict = self.request.arguments
        
        for category in self.request.arguments:
            index = category.split("_")[1]
            amount = float(arg_dict[category][0].decode('utf-8'))
            jfin_data.update_category(1, index, amount)

        self.render('./www/budget.html', budget=jfin_data.get_budget(1))

def main():
    port = 8080
    logger.debug("Hello")
    
    global httpServer
    loc = os.path.dirname(os.path.realpath(__file__)) + "/www/"   # get the pull path of where we are

    settings = {
        # 'cookie_secret': 'bZJc2sWbQLKos6GkHn/VB9oXwQt8S0R0kRvJ5/xJ89E=',
        # 'login_url': '/login',
        'static_path': os.path.join(loc, 'static'),
        'xsrf_cookies': False,
        #         'compiled_template_cache': False,
        #         'debug':True,
    }

    logger.debug(f"HTTP Server dir: {loc}")

    app = tornado.web.Application(
        handlers=[
            tornado.web.url(r"/", MainHandler, name="index"),
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
    main()
