package jira

import jira.modules.CreateSubtaskDialogModule
import jira.pages.*

class JiraReleaseManagerSpec extends JiraBaseSpec {
    def currentStory
    def currentStoryKey
    def projectSummary

    // TEST CASES TEST GROUP 02
    // CHECK THE CORRECTNESS OF CALCULATION – RISK ASSESSMENT WITHOUT PROBABILITY OF OCCURRENCE
    def "RT_02_001"() {
        // STEP 1 Log in as team member who has rights to the project.
        given: "Log in as team member who has rights to the project"
        to DashboardPage
        loginForm.doLoginProcess()

        expect: "We can login in Jira"
        at DashboardPage

        when: "visit project page"
        to ProjectPage, projectName
        projectSummary = $("div.project-summary-section.project-description > p").text()

        then: "Login in the project is successful."
        at ProjectPage
        report('Step_1_login')

        // STEP 2 - Create a Jira sub-task type Risk Assessment “Risk_High_wo_PoO” to Story1
        when: "Visit issues page"
        to IssuesPage
        sleep(1000)
        if (!searchTextArea) {
            activateAdvancedSearchLink.click()
        }

        then:
        assert searchTextArea

        when: "Search fo the Story with summary 'Story 1'"
        findStoryBySummary(projectName, 'Story 1')

        then: "Story 1 exists"
        assert $("li", title: "Story 1")

        when: "Create a subtask for the Story 1: Open sub task creation form"
        currentStory = $("li", title: "Story 1")
        currentStoryKey = currentStory.getAttribute("data-key")
        currentStory.click()
        waitFor { issueMenu.moreMenu }
        issueMenu.clickCreateSubtask()

        then: "the create subtask dialog is displayed"
        assert createSubtaskDialog
        report('Step_2_window with information to specify')

        // STEP 3:
        // Add following information:
        // - Risk description
        // - GxP relevance: Relevant
        // - Severity of Impact: Low, Medium, High → choose High
        // - Probability of Detection: Immediate, Before, Impact, After Impact → choose After Impact
        // Add a comment to the risk.
        when: "Fill the data"
        def raData1 = [storyKey               : currentStoryKey,
                       summaryInput           : 'Risk_High_wo_PoO',
                       descriptionEditor      : 'Risk_High_wo_PoO',
                       riskComment            : 'Must be tested',
                       gxPRelevance           : CreateSubtaskDialogModule.GxPRelevanceGroupTypes.Relevant,
                       severityOfImpact       : CreateSubtaskDialogModule.SeverityOfImpactTypes.High,
                       probabilityOfDetection : CreateSubtaskDialogModule.ProbabilityOfDetectionTypes.AfterImpact,
                       probabilityOfOccurrence: CreateSubtaskDialogModule.ProbabilityOfOccurrenceTypes.None,]


        createSubtaskDialog.fillRiskSubtask(raData1, 'RT_02_001_Step3_Story1')

        // STEP 4
        and: "Click on create"
        createSubtaskDialog.createSubmitButton.click()
        sleep(1000)
        report('Step_4_Open_Create_Subtask_for_Story_1')

        // STEP 5
        and: "Check if the Risk Priority Number (RPN) and Risk Priority is automatically calculated"
        raData1.key = subsTaskIssues.last().getAttribute("data-issuekey")
        to IssuePage, raData1.key

        then: "Risk priority number must be 18 and risk priority HIGH"
        assert riskprioritynumber.text() == '18'
        assert riskpriority.text() == 'HIGH'
        report('Step_5_Story_1_risk_priority_number and risk_priority')

        // STEP 6
        // Create a Jira sub-task type Risk Assessment “Risk_Medium_wo_PoO” to Story2
        when:
        to IssuesPage
        sleep(1000)
        findStoryBySummary(projectName, 'Story 2')
        then: "Story 2 exists"
        assert $("li", title: "Story 2")

        when: "Create a subtask for the Story 2: Open sub task creation form"
        currentStory = $("li", title: "Story 2")
        currentStoryKey = currentStory.getAttribute("data-key")
        currentStory.click()
        waitFor { issueMenu.moreMenu }
        issueMenu.clickCreateSubtask()

        then: "the create subtask dialog is displayed"
        assert createSubtaskDialog
        report('Step_7_window with information to specify')

        // STEP 7
        when: "Fill the data"
        def raData2 = [storyKey               : currentStoryKey,
                       summaryInput           : 'Risk_Medium_wo_PoO',
                       descriptionEditor      : 'Risk_Medium_wo_PoO',
                       riskComment            : 'Story 2 comment: Must be tested',
                       gxPRelevance           : CreateSubtaskDialogModule.GxPRelevanceGroupTypes.NotRelevantLess,
                       severityOfImpact       : CreateSubtaskDialogModule.SeverityOfImpactTypes.Medium,
                       probabilityOfDetection : CreateSubtaskDialogModule.ProbabilityOfDetectionTypes.BeforeImpact,
                       probabilityOfOccurrence: CreateSubtaskDialogModule.ProbabilityOfOccurrenceTypes.None,]

        createSubtaskDialog.fillRiskSubtask(raData2, 'RT_02_001_Step7_Story2')

        // STEP 8
        and: "Click create"
        createSubtaskDialog.createSubmitButton.click()
        sleep(5000)
        report('Step_8_Open_Create_Subtask_for_Story_2')

        // STEP 9 Check if the Risk Priority Number (RPN) and Risk Priority is automatically calculated
        and: "Check if the Risk Priority Number (RPN) and Risk Priority is automatically calculated"
        raData2.key = subsTaskIssues.last().getAttribute("data-issuekey")
        to IssuePage, raData2.key

        then: "Risk priority number must be 4 and risk priority MEDIUM"
        assert riskprioritynumber.text() == '4'
        assert riskpriority.text() == 'MEDIUM'
        report('Step_9_Story_2_risk_priority_number and risk_priority')

        // STEP 10
        // Create a Jira sub-task type Risk Assessment “Risk_Low_wo_PoO” to Story3
        when:
        to IssuesPage
        sleep(1000)
        findStoryBySummary(projectName, 'Story 3')
        then: "Story 3 exists"
        assert $("li", title: "Story 3")

        when: "Create a subtask for the Story 3: Open sub task creation form"
        currentStory = $("li", title: "Story 3")
        currentStoryKey = currentStory.getAttribute("data-key")
        currentStory.click()
        waitFor { issueMenu.moreMenu }
        issueMenu.clickCreateSubtask()

        then: "the create subtask dialog is displayed"
        assert createSubtaskDialog
        report('Step_10_Open_Create_Subtask_for_Story_3')

        // STEP 11
        when: "Fill the data"
        def raData3 = [storyKey               : currentStoryKey,
                       summaryInput           : 'Risk_Low_wo_PoO',
                       descriptionEditor      : 'Risk_Low_wo_PoO',
                       riskComment            : 'Story 3 comment: Could be tested',
                       gxPRelevance           : CreateSubtaskDialogModule.GxPRelevanceGroupTypes.NotRelevantLess,
                       severityOfImpact       : CreateSubtaskDialogModule.SeverityOfImpactTypes.Low,
                       probabilityOfDetection : CreateSubtaskDialogModule.ProbabilityOfDetectionTypes.Immediate,
                       probabilityOfOccurrence: CreateSubtaskDialogModule.ProbabilityOfOccurrenceTypes.None,]

        createSubtaskDialog.fillRiskSubtask(raData3, 'RT_02_001_Step11_Story3')

        // STEP 13
        and: "Click create"
        createSubtaskDialog.createSubmitButton.click()
        sleep(5000)

        // STEP 12 Check if the Risk Priority Number (RPN) and Risk Priority is automatically calculated
        and: "Check if the Risk Priority Number (RPN) and Risk Priority is automatically calculated"
        raData3.key = subsTaskIssues.last().getAttribute("data-issuekey")

        to IssuePage, raData3.key

        then: "Risk priority number must be 1 and risk priority LOW"
        assert riskprioritynumber.text() == '1'
        assert riskpriority.text() == 'LOW'
        report('Step_12_Story_3_risk_priority_number and risk_priority')

        // STEP 14
        // Check the Risk Assessment Report.
        when: "Visit the Report Selector Page"
        to ReportSelectorPage, projectName
        report('Report select page')
        riskAssesmentLink.click()

        and: "Select the current project"
        $("form").projectId = projectSummary
        $("#next_submit").click()

        then: "In the report page"
        at RiskAssementReportPage
        Map rar = getRiskAssesmentReport()
        assert rar.containsKey(raData1.key)
        def ra1 = rar.get(raData1.key)
        assert CreateSubtaskDialogModule.GxPRelevanceGroupTypesStrings[raData1.gxPRelevance].toLowerCase() == ra1.gxPRelevance.replaceAll("\\s","").replaceAll("/","").toLowerCase()
        assert CreateSubtaskDialogModule.SeverityOfImpactTypesStrings[raData1.severityOfImpact].toLowerCase() == ra1.severityOfImpact.replaceAll("\\s","").toLowerCase()
        assert CreateSubtaskDialogModule.ProbabilityOfDetectionTypesStrings[raData1.probabilityOfDetection].toLowerCase()== ra1.probabilityOfDetection.replaceAll("\\s","").toLowerCase()

        and:
        assert rar.containsKey(raData2.key)
        def ra2 = rar.get(raData2.key)
        assert CreateSubtaskDialogModule.GxPRelevanceGroupTypesStrings[raData2.gxPRelevance].toLowerCase() == ra2.gxPRelevance.replaceAll("\\s","").replaceAll("/","").toLowerCase()
        assert CreateSubtaskDialogModule.SeverityOfImpactTypesStrings[raData2.severityOfImpact].toLowerCase() == ra2.severityOfImpact.replaceAll("\\s","").toLowerCase()
        assert CreateSubtaskDialogModule.ProbabilityOfDetectionTypesStrings[raData2.probabilityOfDetection].toLowerCase() == ra2.probabilityOfDetection.replaceAll("\\s","").toLowerCase()

        and:
        assert rar.containsKey(raData3.key)
        def ra3 = rar.get(raData3.key)
        assert CreateSubtaskDialogModule.GxPRelevanceGroupTypesStrings[raData3.gxPRelevance].toLowerCase() == ra3.gxPRelevance.replaceAll("\\s","").replaceAll("/","").toLowerCase()
        assert CreateSubtaskDialogModule.SeverityOfImpactTypesStrings[raData3.severityOfImpact].toLowerCase() == ra3.severityOfImpact.replaceAll("\\s","").toLowerCase()
        assert CreateSubtaskDialogModule.ProbabilityOfDetectionTypesStrings[raData3.probabilityOfDetection].toLowerCase() == ra3.probabilityOfDetection.replaceAll("\\s","").toLowerCase()

        report('Step_14_Risk_Assesment')

    }
}
