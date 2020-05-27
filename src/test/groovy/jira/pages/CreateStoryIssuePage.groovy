package jira.pages

import geb.Page

class CreateStoryIssuePage extends Page {
    static gampTopicFieldId = 'customfield_11903'
    static reqAcceptanceCriteriaFieldId = 'customfield_12026'
    static funcSpecSummaryFieldId = 'customfield_11905'
    static funcSpecSummaryDescriptionFieldId = 'customfield_11906'
    static funcSpecAcceptanceCriteriaFieldId = 'customfield_11907'
    static configSpecSummaryFieldId = 'customfield_11908'
    static configSpecDescriptionFieldId = 'customfield_11909'

    static gampTopics = [
            notSelected             : 'Not selected',
            operationalRequirements : 'operational requirements',
            functionalRequirements  : 'functional requirements',
            dataRequirements        : 'data requirements',
            technicalRequirements   : 'technical requirements',
            interfaceRequirements   : 'interface requirements',
            environmentRequirements : 'environment requirements',
            performanceRequirements : 'performance requirements',
            availabilityRequirements: 'availability requirements',
            securityRequirements    : 'security requirements',
            maintenanceRequirements : 'maintenance requirements',
            regulatoryRequirements  : 'regulatory requirements',
            roles                   : 'roles',
            compatibility           : 'compatibility',
            proceduralConstraints   : 'procedural constraints',
            overarchingRequirements : 'overarching requirements',
    ]

    static at = { browser.currentUrl.contains('CreateIssue') && $("#issue-create-issue-type").text() == 'Story' }

    static content = {
        summary { $("#summary") }
        description { $ "#description" }
        componentsTextArea { $("#components-textarea") }
        componentsSelectIcon { $("#components-multi-select > .icon") }
        gampTopicSelect { $("#$gampTopicFieldId") }
        reqAcceptanceCriteria { $("#$reqAcceptanceCriteriaFieldId") }
        funcSpecSummary { $("#$funcSpecSummaryFieldId") }
        funcSpecSummaryDescription { $("#$funcSpecSummaryDescriptionFieldId") }
        funcSpecAcceptanceCriteria { $("#$funcSpecAcceptanceCriteriaFieldId") }
        configSpecSummary { $("#$configSpecSummaryFieldId") }
        configSpecDescription { $("#$configSpecDescriptionFieldId") }
        issueCreateButton { $("#issue-create-submit") }
    }


    def createIssue(story, spec) {
        summary = story.summary?:""
        description = story.description?:""
        componentsTextArea = story.component?:""
        spec.report()
        gampTopicSelect = story.gampTopic?:""
        reqAcceptanceCriteria = story.reqAcceptanceCriteria?:""
        spec.report()
        funcSpecSummary = story.funcSpecSummary?:""
        funcSpecSummaryDescription = story.funcSpecSummaryDescription?:""
        spec.report()
        funcSpecAcceptanceCriteria = story.funcSpecAcceptanceCriteria?:""
        configSpecSummary = story.configSpecSummary?:""
        spec.report()
        issueCreateButton.click()
    }

}
