package jira.pages

import geb.Page

class ProjectPage extends Page {

    static url = "/projects"

    String convertToPath(Object[] args) {
        args ? "/" + args[0].toString() + "/summary/" : ""
    }
    static at = { $("#summary-subnav-title > span").text() == "Activity" }
    static content = {
        loginForm { module LoginModule }
    }
}
