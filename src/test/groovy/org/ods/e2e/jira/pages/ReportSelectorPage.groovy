package org.ods.e2e.jira.pages

import geb.Page

class ReportSelectorPage extends Page {
    // https://jira-dev.biscrum.com/projects/NET5?selectedItem=com.atlassian.jira.jira-projects-plugin:report-page
    static url = '/projects'

    String convertToPath(Object[] args) {
        args ? "/" + args[0].toString() + "?selectedItem=com.atlassian.jira.jira-projects-plugin:report-page" : ""
    }

    static content = {
        riskAssesmentLink { $(".report-thumbnail-riskassessment")}
    }

}
