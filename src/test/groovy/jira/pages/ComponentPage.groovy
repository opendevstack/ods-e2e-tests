package jira.pages

import geb.Page

class ComponentPage extends Page {

    static url = "/projects/"
    String convertToPath(Object[] args) {
        args ? "/" + args[0].toString() + "?selectedItem=com.atlassian.jira.jira-projects-plugin:components-page" : ""
    }

    static content = {
        
    }
}
