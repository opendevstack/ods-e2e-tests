package org.ods.e2e.jenkins.pages

import geb.Page

class JenkinsLoginPage extends Page {
    static url = "/"
    static at = { title.contains("Jenkins") }
    static content = { loginButton(wait: true, required: true) { $("a.btn.btn-lg.btn-primary") } }

}
