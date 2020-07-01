package org.ods.e2e.jira.modules

class MitigationCreationFormModule  extends IssueCreationFormModule {
    def fields

    def createIssue(mitigation, spec) {
        summary = mitigation.summary ?: ""
        descriptionTextLink.click()
        description = mitigation.description ?: ""

        issueCreateButton.click()
    }

}
