package org.ods.e2e.jenkins.pages

import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptExecutor
import org.ods.e2e.util.BaseSpec
import org.openqa.selenium.By

class JenkinsJobConfigurePage extends JenkinsJobFolderPage {

    static url = '/job/'

    /**
     * Adapt the url to get to the jenkins job/jobfolder/job/subfolder/configure page
     *     https://jenkins-url.am.boehringer.com/job/e2et3-cd/job/e2t3-project/configure
     * @param args must contain 2 arg, folderKey and projectKey
     */
    String convertToPath(Object[] args) {
        def folder = args[0].toString().toLowerCase()
        def project = args[1].toString().toLowerCase()
        args ? "$folder-cd/job/$folder-cd-$project/configure" : ''
    }

    static content = {
        paramsTable(required: true) { $("#main-panel > div > div > div > form > table > tbody > tr:nth-child(72) > td:nth-child(2)") }
        saveButton(required: true) { $("button", text:'Save') }
        addParameter(required: true) { $("button", text: 'Add Parameter') }
        addStringParameter(required: false) { $("a", text: 'String Parameter')}
    }

    def existParam(paramName) {
        if (paramsTable.find("td.setting-main > input", value: contains(paramName))) {
            println "Param $paramName exists with value: " +
                    paramsTable.find("td.setting-main > input", value: contains(paramName))
                            .parent().parent().nextAll().find("td.setting-main > input")[0].value()
            return true
        }
        println "Param $paramName not exists"
        return false
    }

    def setParameterValue(paramName, value) {
        if (existParam(paramName)) {
            paramsTable.find("td.setting-main > input", value: contains(paramName))
                    .parent().parent().nextAll().find("td.setting-main > input")[0].value(value)
        } else {
            createParameterValue(paramName, value)
        }
    }

    def createParameterValue(paramName, value) {
        // We use the last parameter 'configItem' to be capable of use the menu button
        BaseSpec.js.executeScript("arguments[0].scrollIntoView();",
                paramsTable.find("td.setting-main > input", value: contains('configItem')).firstElement())
        sleep(1000)
        addParameter.click()
        sleep(1000)

        addStringParameter.click()
        sleep(1000)

        // Set values in new fields
        def paramInputs = $("td.setting-main > input")
        paramInputs[paramInputs.size()-19].value(paramName)
        paramInputs[paramInputs.size()-18].value(value)
        sleep(1000)
    }

    def setRePromoteValue(value) {
        setParameterValue('rePromote', value)
        saveButton.click()
    }

}
