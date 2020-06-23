package org.ods.e2e.jira.pages

import geb.Page

class ReportSelectorPage extends BasePage {
    static url = '/projects'

    /**
     * Adapt the url to get to the report selector page
     * https://bitbucket-url/projects/PROJECT?selectedItem=com.atlassian.jira.jira-projects-plugin:report-page
     * @param args must contain 1 args, projectKey
     */
    String convertToPath(Object[] args) {
        def project = args[0].toString().toUpperCase()
        args ? "/$project?selectedItem=com.atlassian.jira.jira-projects-plugin:report-page" : ''
    }

    static content = {
        riskAssesmentLink { $(".report-thumbnail-riskassessment")}
    }

}
