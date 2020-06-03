package org.ods.e2e.bitbucket.pages

import geb.Page

class ProjectPage extends Page {
    static url = "/projects"

    String convertToPath(Object[] args) {
        args ? '/' + args*.toString().join('/') : ''
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
