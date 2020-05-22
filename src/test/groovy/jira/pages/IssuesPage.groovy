package jira.pages

import geb.Page
import jira.modules.CreateSubtaskDialogModule
import jira.modules.IssueMenuModule

class IssuesPage extends Page {

    static url = "/issues"

    //static at = { $("div.issue-navigator") }

    static content = {
        searchForm { $("#content > div.navigator-container.navigator-sidebar-collapsed > div.navigator-body > div > form") }
        activateAdvancedSearchLink(required: true) { $("div.search-options-container > div > a.switcher-item.active") }
        searchTextArea(required: true, wait: true) { $("#advanced-search") }
        searchButton(required: true, wait: true) { $("div.search-options-container > button") }
        issueMenu { module(new IssueMenuModule(driver: driver)) }
        createSubtaskDialog(wait: true) { module(new CreateSubtaskDialogModule(driver: driver)) }
    }
}
