import tornado.web
import tornado.escape
import jfinance


class LoginHandler(jfinance.BaseHandler):
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
            self.redirect(self.get_argument("next", self.reverse_url("index")))  # self.redirect("/")
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
            increased = str(int(incorrect) + 1)
            self.set_secure_cookie("incorrect", increased)
            self.write("""<center>
                            Something Wrong With Your Data (%s)<br />
                            <a href="/">Go Home</a>
                          </center>""" % increased)


class LogoutHandler(jfinance.BaseHandler):
    def get(self):
        self.clear_cookie("user")
        self.redirect("/")


class MainHandler(jfinance.BaseHandler):

    @tornado.web.authenticated
    def get(self):
        user_id = int(self.get_secure_cookie("user_id"))

        # logger.debug(f"User: {user_id}")
        self.render('www/index.html')
