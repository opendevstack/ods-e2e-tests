package jira.pages

import geb.Page
import jira.modules.CreateSubtaskDialogModule
import jira.modules.IssueMenuModule
import jira.modules.NavigationBarModule

class IssuePage extends Page {
    static url = '/browse'

    String convertToPath(Object[] args) {
        args ? '/' + args*.toString().join('/') : ""
    }

    static at = { $("#issue-content.issue-edit-form") }
    static content = {
        navigationBar { module(NavigationBarModule) }
        issueMenu { module(new IssueMenuModule(driver: driver)) }
        createSubtaskDialog(wait: true) { module(new CreateSubtaskDialogModule(driver: driver)) }
        subsTaskView(required: true, wait: true) { $("#view-subtasks") }
        subsTaskTable(required: true, wait: true) { subsTaskView.$("#issuetable") }
        subsTaskIssues(required: true, wait: true) { subsTaskTable.$("tr") }
        riskprioritynumber(wait: true) {
            $("div").find {
                it.getAttribute('data-fieldtypecompletekey') == 'org.opendevstack.jira.plugins.projecttemplate:field.risk.riskprioritynumber'
            }
        }
        riskpriority(wait: true) {
            $("div").find {
                it.getAttribute('data-fieldtype') == 'field.risk.riskpriority'
            }
        }
    }
}
