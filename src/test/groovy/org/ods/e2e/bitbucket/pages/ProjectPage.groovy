package org.ods.e2e.bitbucket.pages

import geb.Page

class ProjectPage extends Page {
    static url = "/projects"

    /**
     * Adapt the url to get to the project page
     * https://bitbucket-url/projects/PROJECT
     * @param args must contain 1 arg, projectKey
     */
    String convertToPath(Object[] args) {
        def project = args[0].toString().toUpperCase()
        args ? "/$project/" : ''
    }

    static content = {
        repositoriesSpan(wait: true, required: true) { $('span.repository-name') }
    }

    def getRepositoriesInfo() {
        return repositoriesSpan.collect {
            [name: it.$('span').text(),
             url : it.$('a').getAttribute('href')]
        }
    }

}
