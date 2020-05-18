package jira.pages

import geb.Page
import jira.modules.LoginModule

class DashboardPage extends Page {
    static url = "/"
    static at = { title.startsWith("System Dashboard")}

    static content = {
        loginForm { module LoginModule }
    }
}