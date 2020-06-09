package org.ods.e2e.openshift.pages

import geb.Page

class PodsPage extends Page {

    static url = '/console/project/'
    /**
     * Generate a path with this format:
     * /console/project/PROJECT/browse/pods
     * @param args The project, f.e. 'test-cd'
     * @return The url to the Pods page
     */
    String convertToPath(Object[] args) {
        args ? args[0] + '/browse/pods' : ''
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
