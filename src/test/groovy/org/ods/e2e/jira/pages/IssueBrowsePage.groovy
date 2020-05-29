package org.ods.e2e.jira.pages

import geb.Page
import org.ods.e2e.jira.modules.CreateSubtaskDialogModule
import org.ods.e2e.jira.modules.IssueMenuModule
import org.ods.e2e.jira.modules.NavigationBarModule

class IssueBrowsePage extends Page {
    static url = '/browse'
    static edpContentEditorId = 'customfield_12025-val'
    static edpHeadingNumberId = 'customfield_12024'
    static edpContentId = 'customfield_12025'

    String convertToPath(Object[] args) {
        args ? '/' + args*.toString().join('/') : ''
    }

    static at = { browser.currentUrl.contains('browse') }

    static content = {
        navigationBar { module(NavigationBarModule) }
        issueMenu { module(new IssueMenuModule(driver: driver)) }
        createSubtaskDialog(wait: true) { module(new CreateSubtaskDialogModule(driver: driver)) }
        subsTaskView(required: true, wait: true) { $('#view-subtasks') }
        subsTaskTable(required: true, wait: true) { subsTaskView.$('#issuetable') }
        subsTaskIssues(required: true, wait: true) { subsTaskTable.$('tr') }
        riskprioritynumber(wait: true) {
            $('div').find {
                it.getAttribute('data-fieldtypecompletekey') == 'org.opendevstack.jira.plugins.projecttemplate:field.risk.riskprioritynumber'
            }
        }
        riskpriority(wait: true) {
            $('div').find {
                it.getAttribute('data-fieldtype') == 'field.risk.riskpriority'
            }
        }
        statusVal(required: true, wait: true) { $('#status-val > span') }
        edpContentEditor(wait: true, required: true) { $("#$edpContentEditorId") }
        edpHeadingNumber { $("#$edpHeadingNumberId-val") }
        edpContent { $("#$edpContentId-wiki-edit > textarea") }
        edpContentSubmitButton { $("#customfield_12025-form button.submit") }
    }


}
