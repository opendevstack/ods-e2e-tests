package org.ods.e2e.bitbucket.pages

import geb.Page
import groovy.json.JsonSlurper

class CommitsPage extends Page {
    static url = "https://bitbucket-dev.biscrum.com/projects"

    /**
     * Adapt the url to get to the commits page
     * https://bitbucket-url/projects/PROJECT/repos/REPOSITORY/browse
     * @param args must contain 2 args, projectName and repositoryName
     */
    String convertToPath(Object[] args) {
        def project = args[0].toString().toUpperCase()
        def repository = args[1].toString().toLowerCase()
        args ? "/$project/repos/$repository/commits" : ''
    }

    static at = { browser.currentUrl.contains('commits') }

    static content = {
        commitsTable { $("#commits-table") }
        commitsRows { $("#commits-table tbody tr") }
    }

    /**
     * Gets the visible commits in the page.
     */
    def getCommits() {
        def jsonSlurper = new JsonSlurper()
        commitsRows.collect {
            jsonSlurper.parseText(it.getAttribute('data-commit-json'))
        }
    }
}
