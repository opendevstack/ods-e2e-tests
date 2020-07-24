package org.ods.e2e.jenkins.pages

import geb.Page

class JenkinsConsoleParametrizedBuildPage extends Page {

    static url = '/job/'
    /**
     * Adapt the url to get to the jenkins job/jobfolder page
     *     https://jenkins-url/job/$project$-cd/job/$job$/build?delay=0sec
     * @param args must contain 1 arg, folderKey
     */
    String convertToPath(Object[] args) {
        def project = args[0].toString().toLowerCase()
        def job = args[1].toString().toLowerCase()
        args ? "/$project-cd/job/$project-cd-$project-$job/build?delay=0sec" : ''
    }

    static content = {
        environmentInput {
            $('input', value: 'environment').siblings('input.setting-input').first()
        }
        changeDescriptionInput {
            $('input', value: 'changeDescription').siblings('input.setting-input').first()
        }
        releaseStatusJiraIssueKeyInput {
            $('input', value: 'releaseStatusJiraIssueKey').siblings('input.setting-input').first()
        }
        changeIdInput {
            $('input', value: 'changeId').siblings('input.setting-input').first()
        }
        versionInput {
            $('input', value: 'version').siblings('input.setting-input').first()
        }
        configItemInput {
            $('input', value: 'configItem').siblings('input.setting-input').first()
        }

        submitButton { $("table.parameters button") }
    }

    /**
     * Fill the form with the parameters
     * @param params Map of params with the format
     *        params = [environment:'dev',
     *                 changeDescription:'',
     *                 releaseStatusJiraIssueKey:'',
     *                 changeId:'',
     *                 version:'WIP',
     *                 configItem:'',]
     * @return
     */
    def fillData(params) {

        if (params.environment) environmentInput.value(params.environment)
        if (params.changeDescription) changeDescriptionInput.value(params.changeDescription)
        if (params.releaseStatusJiraIssueKey) releaseStatusJiraIssueKeyInput.value(params.releaseStatusJiraIssueKey)
        if (params.changeId) changeIdInput.value(params.changeId)
        if (params.version) versionInput.value(params.version)
        if (params.configItem) configItemInput.value(params.configItem)
    }
}
