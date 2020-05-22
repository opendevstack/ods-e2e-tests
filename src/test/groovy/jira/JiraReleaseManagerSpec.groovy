package jira

import jira.modules.CreateSubtaskDialogModule
import jira.pages.DashboardPage
import jira.pages.IssuesPage
import jira.pages.ProjectPage

class JiraReleaseManagerSpec extends JiraBaseSpec {
    def currentStory
    def currentStoryKey
    // TEST CASES TEST GROUP 02
    // CHECK THE CORRECTNESS OF CALCULATION – RISK ASSESSMENT WITHOUT PROBABILITY OF OCCURRENCE
    def "RT_02_001"() {
        // Step 1
        given: "Log in as team member who has rights to the project"


        to DashboardPage
        loginForm.doLoginProcess()

        expect: "We can login in Jira"
        at DashboardPage

        when: "visit project page"
        to ProjectPage, projectName

        then: "Login in the project is successful."
        at ProjectPage
        report()

        // Step 2 - Create a Jira sub-task type Risk Assessment “Risk_High_wo_PoO” to Story1
        when: "Visit issues page"
        to IssuesPage
        sleep(1000)
        if (!searchTextArea) {
            activateAdvancedSearchLink.click()
        }

        then:
        assert searchTextArea
        report()

        when: "Search fo the Story with summary 'Story 1'"
        searchTextArea = "project = $projectName and type = Story and summary ~ 'Story 1' ORDER BY created DESC"
        searchButton.click()
        sleep(1000)

        then: "Story 1 exists"
        assert $("li", title: "Story 1")
        report()

        when: "Create a subtask for the Story 1: Open sub task creation form"
        currentStory = $("li", title: "Story 1")
        currentStoryKey = currentStory.getAttribute("data-key")
        currentStory.click()
        waitFor { issueMenu.moreMenu }
        issueMenu.clickCreateSubtask()

        then: "the create subtask dialog is displayed"
        assert createSubtaskDialog
        report()

        // STEP 3
        when: "Fill the data"
        createSubtaskDialog.fillRiskSubtask(
                [summaryInput           : 'Risk_High_wo_PoO',
                 descriptionEditor      : 'Risk_High_wo_PoO',
                 riskComment            : 'Must be tested',
                 gxPRelevance           : CreateSubtaskDialogModule.GxPRelevanceGroupTypes.Relevant,
                 severityOfImpact       : CreateSubtaskDialogModule.SeverityOfImpactTypes.High,
                 probabilityOfDetection : CreateSubtaskDialogModule.ProbabilityOfDetectionTypes.AfterImpact,
                 probabilityOfOccurrence: CreateSubtaskDialogModule.ProbabilityOfOccurrenceTypes.None,]
        )
        report("Data filled")
        then:
        true

    }
}
