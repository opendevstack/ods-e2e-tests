package org.ods.e2e.jira.pages

class JiraProjectPropertiesActionPage extends IssueCreationDialogBasePage {

    // Need the param
    // projectKey
    static url = '/secure/JiraProjectPropertiesAction.jspa'

    static at = { browser.currentUrl.contains('JiraProjectPropertiesAction.jspa') }

    static content = {
        propertyRows { $('#jpp_configs > tbody > tr') }
    }

    def getProjectProperty(projectProperty) {
        if (projectProperty == null) {
            return null
        }

        def propertyRow = propertyRows.findAll {
            it -> it.$('td',0)?.text()?.toLowerCase() == projectProperty.toLowerCase()
        }

        return [
                id: propertyRow.$('td', 0).getAttribute('title').substring(propertyRow.$('td', 0).getAttribute('title').lastIndexOf('#') + 1),
                name: propertyRow.$('td', 0).text(),
                value: propertyRow.$('td', 1).text(),
        ]
    }
}
