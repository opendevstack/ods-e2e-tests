package org.ods.e2e.jenkins.pages


import org.ods.e2e.util.BaseSpec

class JenkinsJobConfigurePage extends JenkinsJobFolderPage {

    static url = '/job/'
    public static final String FIND_PARAMETERS_NAME_AND_VALUE = "td.setting-main > input.setting-input"
    public static Integer PARAMETERS_LINE = 72

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
        thisProjectIsParameterised(required: false, wait: [5,1]) { Integer lineNumber -> $("#main-panel > div > div > div > form > table > tbody > tr:nth-child(${String.valueOf(lineNumber-3)}) label", text: ~/This project is param.*/, dynamic: true) }
        paramsTable(required: true) { Integer lineNumber  ->  $("#main-panel > div > div > div > form > table > tbody > tr:nth-child(${String.valueOf(lineNumber)}) > td:nth-child(2)", dynamic: true) }
        saveButton(required: true) { $("button", text:'Save') }
        addParameter(required: true) { $("button", text: 'Add Parameter') }
        addStringParameter(required: false) { $("a", text: 'String Parameter')}
    }

    def findLineForParameters(){
        if( thisProjectIsParameterised(72).size() > 0 ){
            PARAMETERS_LINE = 72
        }
        else if( thisProjectIsParameterised(42).size() > 0 ){
            PARAMETERS_LINE = 42
        } else {
            PARAMETERS_LINE = 72
        }
    }

    def existParam(paramName) {
        if (paramsTable(PARAMETERS_LINE).find(FIND_PARAMETERS_NAME_AND_VALUE, value: contains(paramName))) {
            println "Param $paramName exists with value: " +
                    paramsTable(PARAMETERS_LINE).find(FIND_PARAMETERS_NAME_AND_VALUE, value: contains(paramName))
                            .parent().parent().nextAll().find(FIND_PARAMETERS_NAME_AND_VALUE)[0].value()
            return true
        }
        println "Param $paramName not exists"
        return false
    }

    def setParameterValue(paramName, value) {
        if (existParam(paramName)) {
            paramsTable(PARAMETERS_LINE).find("td.setting-main > input", value: contains(paramName))
                    .parent().parent().nextAll().find(FIND_PARAMETERS_NAME_AND_VALUE)[0].value(value)
        } else {
            createParameterValue(paramName, value)
        }
    }

    def createParameterValue(paramName, value) {
        // We use the last parameter 'configItem' to be capable of use the menu button
        BaseSpec.js.executeScript("arguments[0].scrollIntoView();",
                paramsTable(PARAMETERS_LINE).find(FIND_PARAMETERS_NAME_AND_VALUE, value: contains('configItem')).firstElement())
        sleep(1000)
        addParameter.click()
        sleep(1000)
        addStringParameter.click()
        sleep(1000)

        // Set values in new fields
        def paramInputs = paramsTable(PARAMETERS_LINE).$(FIND_PARAMETERS_NAME_AND_VALUE)
        paramInputs[paramInputs.size()-2].value(paramName)
        paramInputs[paramInputs.size()-1].value(value)
        sleep(1000)
    }

    def setRePromoteValue(value) {
        findLineForParameters()
        setParameterValue('rePromote', value)
        saveButton.click()
    }
}