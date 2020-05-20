package jenkins.pages

import geb.Page

class JenkinsConsolePage extends Page{
    static url = "/"
    static at = { title.contains('Jenkins')}
    //static content = { loginButton { $("a.btn.btn-lg.btn-primary")} }
}
