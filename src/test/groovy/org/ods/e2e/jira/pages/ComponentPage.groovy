package org.ods.e2e.jira.pages

import geb.Page

class ComponentPage extends Page {

    static url = "/projects"

    /**
     * Adapt the url to get to the component page
     * https://jira-url/projects/PROJECT?selectedItem=com.atlassian.jira.jira-projects-plugin:components-page
     * @param args must contain 1 arg, projectKey
     */
    String convertToPath(Object[] args) {
        def project = args[0].toString().toUpperCase()
        args ? "/$project?selectedItem=com.atlassian.jira.jira-projects-plugin:components-page" : ''
    }

    static content = {
        componentTable(wait: true, required: true) { $("#components-table") }
        componentTableNames(wait: true, required: true) { $(".components-table__name>div>a")}
    }

    def getComponents() {
        componentTableNames.collect { it.text().minus("Technology-") }
    }
    Boolean existComponent(String name) {
        components.contains(name)
    }
}
