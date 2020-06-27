package org.ods.e2e.jira.modules

import geb.Module

class IssueCreationDialogModule extends Module {

    def fields

    static content = {
        dialog(required: true, wait:true) { $('#create-issue-dialog') }
        issueTypeSelectorModule(required: true, wait: true) { module IssueTypeSelectorModule }
        storyCreationFormModule(required: false, wait: true) { module(new StoryCreationFormModule(fields: fields)) }
    }
}
