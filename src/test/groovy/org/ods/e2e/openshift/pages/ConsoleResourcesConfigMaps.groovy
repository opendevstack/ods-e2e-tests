package org.ods.e2e.openshift.pages

import geb.Page

class ConsoleResourcesConfigMaps extends Page {

    def project

    static at = { browser.currentUrl.contains("/console/project/$project/browse/config-maps") }

    static content = {
        configMapRows(wait: true, required: true) { $('table tr.ng-scope') }
    }

    /**
     * Adapt the url to get to the repository page
     * https://openshift-url/console/project/PROJECT/browse/config-maps
     * @param args must contain 1 args
     *        projectName - Name of the project
     */
    String convertToPath(Object[] args) {
        project = args[0]
        args ? "/console/project/$project/browse/config-maps/" : ''
    }

    /**
     * Get the configmaps that exists in the project
     * @return
     */
    def getConfigMaps() {
        configMapRows.collect { it ->
            [name: it.find('td > a', 0).text(),
             date: it.find('td > span', 0).text()]
        }
    }
}
