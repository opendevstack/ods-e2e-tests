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
        subsTaskView(required: true, wait: true) { $("#view-subtasks") }
        subsTaskTable(required: true, wait: true) { subsTaskView.$("#issuetable") }
        subsTaskIssues(required: true, wait: true) { subsTaskTable.$("tr") }
    }

    def findStoryBySummary(projectName, summary) {
        searchTextArea = "project = $projectName and type = Story and summary ~ '$summary' ORDER BY created DESC"
        searchButton.click()
        sleep(1000)
    }

    def getSubtasks() {
        subsTaskIssues.
                collectEntries {
                    [it.getAttribute("data-issuekey"),
                     [
                             key    : it.getAttribute("data-issuekey"),
                             summary: it.$("td.stsummary > a").text(),
                             status : it.$("td.status > span").text(),
                     ]
                    ]
                }
    }

    def getLatestSubtask(){
        subsTaskIssues.last().getAttribute("data-issukey")
    }
}
