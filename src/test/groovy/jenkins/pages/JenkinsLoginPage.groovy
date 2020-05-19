package jenkins.pages

import geb.Page

class JenkinsLoginPage extends Page{
    static url = "/login"
    static at = { title.startsWith('Log in to Jenkins')}
    static content = { loginButton { $("a.btn.btn-lg.btn-primary")} }
}
