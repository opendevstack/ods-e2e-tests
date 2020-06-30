package org.ods.e2e.openshift.pages

import geb.Page

class ConsoleDeploymentsPage extends Page {

    static url = '/console/project/'

    /**
     * Adapt the url to get to the pods page
     * /console/project/$project/browse/deployments
     * @param args The project, f.e. 'test-cd'
     * @return The url to the Pods page
     */
    String convertToPath(Object[] args) {
        def project = args[0].toString().toLowerCase()
        args ? "$project/browse/deployments" : ''
    }

    static content = {
        deploymentsTr(wait: true, required: true) { $('table > tbody > tr', 'ng-if': 'dcName') }
    }

    def getDeployments() {
        waitFor {
            deploymentsTr
        }
        deploymentsTr.collect { tr ->
            [
                    name        : tr.$("td", 'data-title': 'Name').$("a").text(),
                    lastVersion : tr.$('td', 'data-title': 'Last Version').$('span > a').text(),
                    status      : tr.$("td > div.status > span").text(),
                    statusDetail: tr.$("td > div.status > span > span").text(),
                    created     : tr.$("td > span", 'am-time-ago': 'pod.metadata.creationTimestamp').text(),
                    trigger     : tr.$('td', 'data-title': 'Trigger').$('span > span > span > span').size() > 1 ?
                            tr.$('td', 'data-title': 'Trigger').$('span > span > span > span > span').text():
                            tr.$('td', 'data-title': 'Trigger').$('span > span > span > span').text()
            ]
        }
    }
}
