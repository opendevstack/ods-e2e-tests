package org.ods.e2e.jira.pages

import geb.Page
import org.ods.e2e.jira.modules.CreateSubtaskDialogModule
import org.ods.e2e.jira.modules.IssueMenuModule

class IssuesPage extends BasePage {

    static url = "/issues"

    //static at = { $("div.issue-navigator") }

    static content = {
        searchForm { $('#content > div.navigator-container.navigator-sidebar-collapsed > div.navigator-body > div > form') }
        activateAdvancedSearchLink(required: false) { $('a.switcher-item.active', 'data-id': 'basic') }
        searchTextArea(required: true, wait: true) { $('#advanced-search') }
        searchButton(required: true, wait: true) { $('div.search-options-container > button') }
        issueMenu { module(new IssueMenuModule(driver: driver)) }
        createSubtaskDialog(wait: true) { module(new CreateSubtaskDialogModule(driver: driver)) }
        subsTaskView(required: true, wait: true) { $('#view-subtasks') }
        subsTaskTable(required: true, wait: true) { subsTaskView.$('#issuetable') }
        subsTaskIssues(required: true, wait: true) { subsTaskTable.$('tr') }
        layoutSwitcherButton(required: true, wait: true) { $('#layout-switcher-button') }
        layoutSwitcherOptionDetail(required: true, wait: true) { $('#layout-switcher-options > div > ul > li:nth-child(1) > a') }
        layoutSwitcherOptionList(required: true, wait: true) { $('#layout-switcher-options > div > ul > li:nth-child(2) > a') }
        issuesTable { $('#issuetable') }
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
        switchLayoutToList()
        switchToAdvancedSearch()
        def queryString = "project = $args.projectName"
        queryString += args.issueId ? " and issue = $args.issueId" : ''
        queryString += args.issueType ? " and issuetype = $args.issueType" : ''
        queryString += args.status ? " and status = $args.status" : ''

        searchTextArea = queryString
        println "?????? findIssue: $queryString"
        searchButton.click()
    }

    /**
     * Find document chapters based in some parameters
     * projectName: Key of the project like VTATL2
     * document: Type of document f.e. CSD
     */
    def findDocumentChapters(Map args) {
        def queryString = "project = $args.projectName AND issuetype in ('Documentation Chapter', Story) " +
                "AND labels = Doc:$args.document " +
                "ORDER BY 'EDP Heading Number' ASC"
        searchTextArea = queryString
        println "?????? findDocumentChapters: $queryString"
        searchButton.click()
    }

    /**
     * Find a issue by its summary
     * projectName: Key of the project like VTATL2
     * summary: summary of the project
     */
    def findStoryBySummary(projectName, summary) {
        searchTextArea = "project = $projectName and type = Story and summary ~ '$summary' ORDER BY created DESC"
        searchButton.click()
        sleep(1000)
    }

    /**
     * Get all the subtasks that we can see in a issuePage
     * projectName: Key of the project like VTATL2
     * summary: summary of the project
     * Note: getSubtasks and getIssuesList looks the same but have different structure so different css selector
     */
    def getSubtasks() {
        subsTaskIssues.
                collectEntries {
                    [it.getAttribute('data-issuekey'),
                     [
                             key    : it.getAttribute('data-issuekey'),
                             summary: it.$('td.stsummary > a').text(),
                             status : it.$('td.status > span').text(),
                     ]
                    ]
                }
    }
    /**
     * Get the latest subtask
     */
    def getLatestSubtask() {
        subsTaskIssues.last().getAttribute('data-issukey')
    }

    /**
     * Switch the layout to List View
     */
    def switchLayoutToList() {
        layoutSwitcherButton.click()
        layoutSwitcherOptionList.click()
        waitFor { issuesTable }
    }

    /**
     * Switch the layout to Detail View
     * @return
     */
    def switchLayoutToDetail() {
        layoutSwitcherButton.click()
        layoutSwitcherOptionDetail.click()
    }

    /**
     * Switch search mode to advanced, it not already selected.
     */
    def switchToAdvancedSearch() {
        if (activateAdvancedSearchLink.size() == 1) {
            activateAdvancedSearchLink.click()
        }
    }

    /**
     * Get the list of issues in the ListView
     * @return
     */
    Map getIssuesList() {
        waitFor { issuesTable }
        issuesTable.$('tr.issuerow').collectEntries {
            [it.getAttribute('data-issuekey'),
             [
                     key    : it.getAttribute('data-issuekey'),
                     summary: it.$('td.summary > p > a').text(),
                     status : it.$('td.status > span').text(),
             ]
            ]
        }
    }
}
