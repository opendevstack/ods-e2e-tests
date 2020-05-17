package jira.pages

import geb.Page
import jira.modules.LoginModule

class DashboardPage extends Page {
    static url = "/secure/Dashboard.jspa"
    static at = { title ==  "System Dashboard - JIRA"}

    static content = {
        loginForm { module LoginModule }
    }
}