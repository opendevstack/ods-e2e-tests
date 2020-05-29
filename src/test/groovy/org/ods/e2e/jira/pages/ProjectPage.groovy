package org.ods.e2e.jira.pages

import geb.Page
import org.ods.e2e.jira.modules.NavigationBarModule

class ProjectPage extends Page {

    static url = "/projects"

    String convertToPath(Object[] args) {
        args ? "/" + args[0].toString() + "/summary/" : ""
    }
    static at = { browser.currentUrl.contains('summary')  }

    static content = {
        navigationBar(wait: true, required: false) { module(NavigationBarModule) }
        loginForm(wait: true, required: false) { module LoginModule }
    }
}
