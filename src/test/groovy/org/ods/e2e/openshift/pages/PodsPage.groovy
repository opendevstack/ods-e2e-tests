package org.ods.e2e.openshift.pages

import geb.Page

class PodsPage extends Page {

    static url = '/console/project/'

    /**
     * Adapt the url to get to the pods page
     * /console/project/PROJECT/browse/pods
     * @param args The project, f.e. 'test-cd'
     * @return The url to the Pods page
     */
    String convertToPath(Object[] args) {
        def project = args[0].toString().toLowerCase()
        args ? "$project/browse/pods" : ''
    }

    static at = { browser.currentUrl.endsWith('browse/pods') }

    static content = {
        podsTr(wait: true, required: true) { $("table > tbody > tr") }
    }

    def getPods() {
        waitFor {
            podsTr.$('td')
        }
        podsTr.collect { tr ->
            [
                    name  : tr.$("td", 'data-title': 'Name').$("a").text(),
                    status: tr.$("td > div.status > span").text()
            ]
        }
    }
}
