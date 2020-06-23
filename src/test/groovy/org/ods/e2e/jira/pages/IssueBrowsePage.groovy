package org.ods.e2e.jira.pages

import geb.Page
import org.ods.e2e.jira.modules.CreateLinkDialogModule
import org.ods.e2e.jira.modules.CreateSubtaskDialogModule
import org.ods.e2e.jira.modules.IssueMenuModule
import org.ods.e2e.jira.modules.NavigationBarModule
import org.ods.e2e.util.SpecHelper

class IssueBrowsePage extends BasePage {
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
        navigationBar { module(NavigationBarModule) }
        issueMenu { module(new IssueMenuModule(driver: driver, issue: selectedIssue)) }
        createSubtaskDialog(wait: true) { module(new CreateSubtaskDialogModule(driver: driver)) }
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
        edpContentEditor(wait: true, required: true) { $("#" + SpecHelper.getFieldId(fields, "Documentation Chapter", "EDP Content") + "-val") }
        edpHeadingNumber(wait: true, required: true) { $("#" + SpecHelper.getFieldId(fields, "Documentation Chapter", "EDP Heading Number") + "-val") }
        edpContent(wait: true, required: true) { $("#" + SpecHelper.getFieldId(fields, "Documentation Chapter", "EDP Content") + "-wiki-edit > textarea") }
        edpContentSubmitButton { $("#" + SpecHelper.getFieldId(fields, "Documentation Chapter", "EDP Content") + "-form button.submit") }
    }

    def addLinkToIssue(linkType, issueLinked) {
        issueMenu.clickLink()
        createLinkDialog.linkTypeSelector = linkType
        createLinkDialog.issueKeysTextArea = issueLinked
        createLinkDialog.linkButton.click()
        sleep(2000)
    }

}
