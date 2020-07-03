package org.ods.e2e.jira.pages

class JiraProjectPropertiesEditAction extends IssueCreationDialogBasePage {

    // Needed param:
    // projectKey
    // propertyId
    static url = '/secure/JiraProjectPropertiesEditAction.jspa'

    static content = {
        propertyId { $('#propertyId') }
        propertyKey { $('#propertyKey') }
        propertyValue { $('#propertyValue') }
        saveButton { $('#JiraProjectPropertiesAddEditForm > div.aui-buttons > input') }
    }
}
