package jira.pages

import geb.Page

class ProjectPage extends Page {

    static url = "/browse/"

    static content = {
        loginForm { module LoginModule }
    }
}
