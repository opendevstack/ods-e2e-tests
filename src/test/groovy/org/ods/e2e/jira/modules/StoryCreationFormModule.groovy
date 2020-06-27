package org.ods.e2e.jira.modules

import org.ods.e2e.util.SpecHelper

class StoryCreationFormModule extends IssueCreationFormModule {

    def fields

    static content = {
        navigationBar { module(NavigationBarModule) }
        gampTopicSelect { $("#" + SpecHelper.getFieldId(fields, "Story", "Gamp Topic")) }
        reqAcceptanceCriteria { $("#" + SpecHelper.getFieldId(fields, "Story", "Req Acceptance Criteria")) }
        funcSpecSummary { $("#" + SpecHelper.getFieldId(fields, "Story", "FuncSpec Summary")) }
        funcSpecSummaryDescription { $("#" + SpecHelper.getFieldId(fields, "Story", "FuncSpec Description")) }
        funcSpecAcceptanceCriteria { $("#" + SpecHelper.getFieldId(fields, "Story", "FuncSpec Acceptance Criteria")) }
        configSpecSummary { $("#" + SpecHelper.getFieldId(fields, "Story", "ConfigSpec Summary")) }
        configSpecDescription { $("#" + SpecHelper.getFieldId(fields, "Story", "FuncSpec Acceptance Criteria")) }
    }

    def createIssue(story, spec) {
        summary = story.summary ?: ""
        //descriptionTextLink.click()
        // WORKAROUND: the Text link selector doesn't work regarding template downloads
        $('li', 'data-mode': 'source').$('a')*.click()
        description = story.description ?: ""
        componentsTextArea = story.component ?: ""
        spec.report('Fill data 1')
        gampTopicSelect = story.gampTopic ?: ""
        reqAcceptanceCriteria = story.reqAcceptanceCriteria ?: ""
        spec.report('Fill data 1')
        funcSpecSummary = story.funcSpecSummary ?: ""
        funcSpecSummaryDescription = story.funcSpecSummaryDescription ?: ""
        spec.report('Fill data 1')
        funcSpecAcceptanceCriteria = story.funcSpecAcceptanceCriteria ?: ""
        configSpecSummary = story.configSpecSummary ?: ""
        spec.report('Fill data 1')
        issueCreateButton.click()
    }
}
