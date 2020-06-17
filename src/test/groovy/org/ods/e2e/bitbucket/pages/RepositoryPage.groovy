package org.ods.e2e.bitbucket.pages

import geb.Page

class RepositoryPage extends Page {
    def project
    def repository

    static url = "/projects"

    static at = { browser.currentUrl.contains("$project/repos/$repository/browse") }

    /**
     * Adapt the url to get to the repository page
     * https://bitbucket-url/projects/PROJECT/repos/REPOSITORY/browse
     * @param args must contain 2 args, projectName and repositoryName
     */
    String convertToPath(Object[] args) {
        def file = ""
        def params = ""
        args.eachWithIndex { Object entry, int i ->
            if (i == 0) {
                project = entry.toString().toUpperCase()
            } else if (i == 1) {
                repository = entry
            } else {
                if (entry instanceof String) {
                    file += file + "/$entry"
                } else if (entry instanceof LinkedHashMap) {
                    entry.each { it ->
                        params += "$it.key=$it.value"
                    }
                }
            }
        }
        args ? "/$project/repos/$repository/browse$file?$params" : ''
    }

    static content = {
        commitsLink { $("#repository-nav-commits") }
        fileRows { $("tr.file.file-row") }
    }

    def getFiles() {
        fileRows.collect { item ->
            [name       : item.$('td.item-name > a').text() ,
             description: item.$('td.message > a').text()
            ]
        }
    }
}
