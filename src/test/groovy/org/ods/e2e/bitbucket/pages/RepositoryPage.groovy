package org.ods.e2e.bitbucket.pages

import geb.Page

class RepositoryPage extends Page {
    def project
    def repository

    static url = "https://bitbucket-dev.biscrum.com/projects"

    static at = { browser.currentUrl.contains("$project/repos/$repository/browse")}

    /**
     * Adapt the url to get to the repository page
     * https://bitbucket-url/projects/PROJECT/repos/REPOSITORY/browse
     * @param args must contain 2 args, projectName and repositoryName
     */
    String convertToPath(Object[] args) {
        project = args[0]
        repository = args[1]
        args ? '/' + args[0] + '/repos/' + args[1] + '/browse' : ''
    }

    static content = {
        commitsLink { $("#repository-nav-commits") }
    }
}
