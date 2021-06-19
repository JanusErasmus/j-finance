import tornado.web
import tornado.httpserver
import logging.config
import os
import signal
import jfinance

realpath = os.path.dirname(os.path.realpath(__file__))
logging.config.fileConfig(realpath + "/logging.ini")
logger = logging.getLogger(__name__)
httpServer: tornado.httpserver.HTTPServer


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
    global httpServer
    port = 8080
    loc = os.path.dirname(os.path.realpath(__file__)) + "/jfinance/www/"  # get the full path of where we are

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
            tornado.web.url(r"/login", jfinance.LoginHandler),
            tornado.web.url(r"/logout", jfinance.LogoutHandler),
            tornado.web.url(r"/", jfinance.MainHandler, name="index"),
            tornado.web.url(r"/transaction", jfinance.TransactionHandler, name="trans"),
            tornado.web.url(r"/expenses", jfinance.ExpenseHandler, name="expense"),
            tornado.web.url(r"/budget", jfinance.BudgetHandler, name="bud"),

            (r"/(.*\.ico)", tornado.web.StaticFileHandler, {"path": loc}),  # allow all .ico files (for favicon)
            (r"/(.*\.js)", tornado.web.StaticFileHandler, {"path": loc}),  # allow all javascript files
            (r"/(.*\.css)", tornado.web.StaticFileHandler, {"path": loc}),  # allow all stylesheets
            (r"/(.*\.png)", tornado.web.StaticFileHandler, {"path": loc}),  # allow all png files
            (r"/(.*\.gif)", tornado.web.StaticFileHandler, {"path": loc}),  # allow all gif files
            (r"/(.*\.jpg)", tornado.web.StaticFileHandler, {"path": loc}),  # allow all jpegs
            (r"/(.*\.pdf)", tornado.web.StaticFileHandler, {"path": loc}),  # allow pdf files (user manual)
            (r"/(.*\.html)", tornado.web.StaticFileHandler, {"path": loc})  # allow all html files
        ], **settings
    )
    httpServer = tornado.httpserver.HTTPServer(app)
    httpServer.listen(port)
    logger.debug(f"Web Server Started on Port: {port}")

    tornado.ioloop.IOLoop.instance().start()


if __name__ == "__main__":
    signal.signal(signal.SIGINT, sig_keyboard_int)  # create callback to capture keyboard interrupt
    main()
    logger.info("DONE")
