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
        layoutSwitcherButton(required: true, wait: true) { $("#layout-switcher-button") }
        layoutSwitcherOptionDetail(required: true, wait: true) { $("#layout-switcher-options > div > ul > li:nth-child(1) > a") }
        layoutSwitcherOptionList(required: true, wait: true) { $("#layout-switcher-options > div > ul > li:nth-child(2) > a") }
    }

    /**
     * Find a Issue by id, type and status
     * expected args:
     * projectName
     * issueId
     * issueType
     * issueStatus
     */
    def findIssue(Map args) {
        def queryString = "project = $args.projectName "
        queryString += args.issueId ? " and issue = $args.issueId" : ""
        queryString += args.issueType ? " and issuetype = $args.issueType" : ""
        queryString += args.status ? " and status = $args.status" : ""

        searchTextArea = queryString
        print "?????? findIssue: $queryString"
        searchButton.click()
    }

    def findDocumentChapters(Map args) {
        def queryString = "project = $args.projectName AND issuetype in ('Documentation Chapter', Story) AND labels = Doc:CSD ORDER BY priority DESC, issuekey DESC"
        searchTextArea = queryString
        print "?????? findDocumentChapters: $queryString"
        searchButton.click()
    }

    // Find a story by its summary
    def findStoryBySummary(projectName, summary) {
        searchTextArea = "project = $projectName and type = Story and summary ~ '$summary' ORDER BY created DESC"
        searchButton.click()
        sleep(1000)
    }

    // Get all the subtasks that we can see in a issuePage
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
    // GEt the latest subtask
    def getLatestSubtask() {
        subsTaskIssues.last().getAttribute("data-issukey")
    }

    // Switch the layout to List View
    def switchLayoutToList() {
        layoutSwitcherButton.click()
        layoutSwitcherOptionList.click()
    }

    // Switch the layout to Detail View
    def switchLayoutToDetail() {
        layoutSwitcherButton.click()
        layoutSwitcherOptionDetail.click()
    }

}
