package org.ods.e2e.jira.modules

import org.ods.e2e.util.SpecHelper

class TestCreationFormModule extends IssueCreationFormModule {

    def fields

    static TestExecutionTypes = [
            Automated: 'Automated',
            Manual   : 'Manual',
    ]

    static TestLevels = [
            Acceptance  : 'Acceptance',
            Integration : 'Integration',
            Unit        : 'Unit',
            Installation: 'Installation',
    ]

    static content = {
        testExecutionType(wait: true, required: true) { $("#" + SpecHelper.getFieldId(fields, "Test", "Test Execution Type")) }
        testLevel(wait: true, required: true) { $("#" + SpecHelper.getFieldId(fields, "Test", "Test Level")) }
        testStepsEditRow(wait: true, required: true) { $("#teststep-table > tbody.aui-restfultable-create > tr") }
        testStepCell(wait: true, required: true) { testStepsEditRow.$("td:nth-child(3) > textarea") }
        testDataCell(wait: true, required: true) { testStepsEditRow.$("td:nth-child(4) > textarea") }
        testResultCell(wait: true, required: true) { testStepsEditRow.$("td:nth-child(5) > textarea") }
        addStepButton(wait: true, required: true) { testStepsEditRow.$("td.zephyr-aui-restfultable-operations > input") }
    }


    def createIssue(test, spec) {
        println test
        summary = test.summary ?: ""
        descriptionTextLink.click()
        description = test.description ?: ""
        testExecutionType = test.testExecutionType
        testLevel = test.testLevel
        test.steps.each { step ->
            testStepCell = step.value.step ?: ""
            testDataCell = step.value.data ?: ""
            testResultCell = step.value.result ?: ""
            addStepButton.click()
        }
        issueCreateButton.click()
    }
}
