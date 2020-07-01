package org.ods.e2e.jira.pages

import org.ods.e2e.jira.modules.CreateLinkDialogModule
import org.ods.e2e.jira.modules.CreateSubtaskDialogModule
import org.ods.e2e.jira.modules.DocumentChapterDialogModule
import org.ods.e2e.jira.modules.IssueMenuModule

class IssueBrowsePage extends IssueCreationDialogBasePage {
    static url = '/browse'
    def selectedIssue = ''

    /**
     * Adapt the url to get to the issue page
     * https://jira-url/browse/ISSUE
     * @param args must contain 1 arg, issueKey
     */
    String convertToPath(Object[] args) {
        def issue = args[0].toString().toUpperCase()
        selectedIssue = issue
        args ? "/$issue" : ''
    }

    static at = { browser.currentUrl.contains('browse') }

    static content = {
        editIssueButton { $('#edit-issue') }
        issueMenu { module(new IssueMenuModule(driver: driver, issue: selectedIssue)) }
        createSubtaskDialog(wait: true) { module(new CreateSubtaskDialogModule(driver: driver, fields: fields)) }
        createLinkDialog(wait: true) { module CreateLinkDialogModule }
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
        documentChapterDialogModule(wait: true, required: true) { module(new DocumentChapterDialogModule(fields: fields, driver: driver))}
    }

    def addLinkToIssue(linkType, issueLinked) {
        issueMenu.clickLink()
        createLinkDialog.linkTypeSelector = linkType
        createLinkDialog.issueKeysTextArea = issueLinked
        createLinkDialog.linkButton.click()
        sleep(2000)
    }

}
