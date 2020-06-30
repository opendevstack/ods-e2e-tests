package org.ods.e2e.jira.pages

import geb.Page
import org.ods.e2e.jira.modules.NavigationBarModule

class ProjectPage extends BasePage {

    static url = "/projects"

    /**
     * Adapt the url to get to the project page
     * https://jira-url/browse/ISSUE
     * @param args must contain 1 arg, projectKey
     */
    String convertToPath(Object[] args) {
        def project = args[0].toString().toUpperCase()
        args ? "/$project/summary/" : ""
    }
    static at = { browser.currentUrl.contains('summary')  }

    static content = {
        navigationBar(wait: true, required: false) { module(NavigationBarModule) }
        loginForm(wait: true, required: false) { module LoginModule }
    }
}
