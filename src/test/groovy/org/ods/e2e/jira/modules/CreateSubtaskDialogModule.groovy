package org.ods.e2e.jira.modules

import geb.Module
import org.ods.e2e.jira.JiraBaseSpec
import org.ods.e2e.util.SpecHelper
import org.openqa.selenium.By

class CreateSubtaskDialogModule extends Module {

    def driver
    def fields = []

    static groupPosition = [GxPRelevanceGroup           : 4,
                            SeverityOfImpactGroup       : 5,
                            ProbabilityOfDetectionGroup : 6,
                            ProbabilityOfOccurrenceGroup: 7,
    ]
    static GxPRelevanceGroupTypes = [None            : 2,
                                     Relevant        : 3,
                                     NotRelevantEqual: 4,
                                     NotRelevantLess : 5,
                                     NotRelevantZero : 6,
    ]
    static GxPRelevanceGroupTypesStrings = GxPRelevanceGroupTypes.collectEntries { e -> [(e.value): e.key] }

    static SeverityOfImpactTypes = [None  : 2,
                                    Low   : 3,
                                    Medium: 4,
                                    High  : 5,
    ]
    static SeverityOfImpactTypesStrings = SeverityOfImpactTypes.collectEntries { e -> [(e.value): e.key] }

    static ProbabilityOfDetectionTypes = [None        : 2,
                                          Immediate   : 3,
                                          BeforeImpact: 4,
                                          AfterImpact : 5,
    ]
    static ProbabilityOfDetectionTypesStrings = ProbabilityOfDetectionTypes.collectEntries { e -> [(e.value): e.key] }

    static ProbabilityOfOccurrenceTypes = [None  : 3,
                                           High  : 4,
                                           Low   : 5,
                                           Medium: 6,
    ]
    static ProbabilityOfOccurrenceTypesStrings = ProbabilityOfOccurrenceTypes.collectEntries { e -> [(e.value): e.key] }

    static content = {
        createSubtaskDialogForm { $("#create-subtask-dialog") }
        summaryInput(wait: true) { createSubtaskDialogForm.$("#summary") }
        descriptionEditor(wait: true) { createSubtaskDialogForm.$("#description-wiki-edit > textarea") }
        descriptionTextLink(wait: true) { createSubtaskDialogForm.$("li", 'data-mode': 'source') }
        riskComment(wait: true) { createSubtaskDialogForm.$("textarea#" + SpecHelper.getFieldId(fields, "Risk Assessment", "Risk Comment")) }
        gxPRelevanceGroup(wait: true) {
            createSubtaskDialogForm.$("fieldset:nth-child($groupPosition.GxPRelevanceGroup)")
        }
        severityOfImpactGroup(wait: true) {
            createSubtaskDialogForm.$("fieldset:nth-child($groupPosition.SeverityOfImpactGroup)")
        }
        probabilityOfDetectionGroup(wait: true) {
            createSubtaskDialogForm.$("fieldset:nth-child($groupPosition.ProbabilityOfDetectionGroup)")
        }
        //probabilityOfOccurrenceGroup(wait: true) {
        //    createSubtaskDialogForm.$("fieldset:nth-child($groupPosition.ProbabilityOfOccurrenceGroup)")
        //}
        createSubmitButton(wait: true) { createSubtaskDialogForm.$("#create-issue-submit") }
    }

    def selectRadioButtonsGroup(group, radioButton) {
        def selector = "fieldset:nth-child($group) > div:nth-child($radioButton) > label"
        def element = driver.findElement(By.cssSelector(selector))
        JiraBaseSpec.js.executeScript("arguments[0].scrollIntoView();", element)
        element.click()
    }

    def selectGxPRelevanceForRisk(relevance) {
        selectRadioButtonsGroup(groupPosition.GxPRelevanceGroup, relevance)
    }

    def selectSeverityOfImpact(severity) {
        selectRadioButtonsGroup(groupPosition.SeverityOfImpactGroup, severity)
    }

    def selectProbabilityOfDetection(probability) {
        selectRadioButtonsGroup(groupPosition.ProbabilityOfDetectionGroup, probability)
    }

    def selectProbabilityOfOccurrence(probability) {
        selectRadioButtonsGroup(groupPosition.ProbabilityOfOccurrenceGroup, probability)
    }

    def fillRiskSubtask(data, prefix = '') {
        summaryInput = data.summaryInput
        descriptionTextLink.click()
        descriptionEditor = data.descriptionEditor
        riskComment = data.riskComment
        browser.report(prefix + 'fill_data_1')
        selectGxPRelevanceForRisk(data.gxPRelevance)
        selectSeverityOfImpact(data.severityOfImpact)
        selectProbabilityOfDetection(data.probabilityOfDetection)
        //if (probabilityOfOccurrenceGroup.size()) selectProbabilityOfOccurrence(data.probabilityOfOccurrence)
        browser.report(prefix + 'fill_data_2')
    }
}