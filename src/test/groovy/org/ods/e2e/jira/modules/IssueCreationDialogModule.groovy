package org.ods.e2e.jira.modules

import geb.Module

class IssueCreationDialogModule extends Module {
    def driver
    def fields

    static content = {
        dialog(required: true, wait: true) { $('#create-issue-dialog') }
        issueTypeSelectorModule(required: true, wait: true) { module(new IssueTypeSelectorModule(driver: driver)) }
        storyCreationFormModule(required: false, wait: true) { module(new StoryCreationFormModule(fields: fields)) }
        technicalSpecificationFormModule(required: false, wait: true) { module(new TechnicalSpecificationFormModule(fields: fields)) }
        testCreationFormModule(required: false, wait: true) { module(new TestCreationFormModule(fields: fields)) }
    }
}
