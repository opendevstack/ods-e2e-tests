package org.ods.e2e

import org.ods.e2e.jira.JiraBaseSpec
import org.ods.e2e.jira.modules.CreateLinkDialogModule
import org.ods.e2e.jira.modules.CreateSubtaskDialogModule
import org.ods.e2e.jira.pages.*

class  JiraReleaseManagerSpec extends JiraBaseSpec {
    def currentStory
    def currentStoryKey
    def projectSummary
    def static issues = [
            story1: [
                    summary                   : "Story 1: Test Creating Story 1",
                    description               : "As user, I want this template to provide issues for requirements creation, in order to store contentn and track the progress of USR documents.",
                    component                 : "Technology-demo-app-front-end",
                    gampTopic                 : CreateStoryIssuePage.gampTopics.functionalRequirements,
                    funcSpecSummary           : "Standard functionality",
                    funcSpecSummaryDescription: "Standard functionality of Jira supported by configuration of issue type in the ODS project template to suport the creation of the C-CSD document (Combined Specification Document).",
                    configSpecSummary         : "Configuration Jira",
                    configSpecDescription     : "template configuration",
            ],
            story2: [
                    summary                   : "Story 2: Test Creating Story 2",
                    description               : "Story 2 description:As user, I want this template to provide issues for requirements creation, in order to store contentn and track the progress of USR documents.",
                    component                 : "Technology-demo-app-front-end",
                    gampTopic                 : CreateStoryIssuePage.gampTopics.dataRequirements,
                    funcSpecSummary           : "Story 2: funcSpecSummary",
                    funcSpecSummaryDescription: "Story 2: funcSpecSummaryDescription - Standard functionallity of Jira supported by configuration of issue type in the ODS project template to suport the creation of the C-CSD document (Combined Specification Document).",
                    configSpecSummary         : "Story 2 configSpecSummary: Configuration Jira",
                    configSpecDescription     : "Story 2 configSpecDescription: template configuration",
            ],
            story3: [
                    summary                   : "Story 3: Test Creating Story 3",
                    description               : "Story 3 description:As user, I want this template to provide issues for requirements creation, in order to store contentn and track the progress of USR documents.",
                    component                 : "Technology-demo-app-front-end",
                    gampTopic                 : CreateStoryIssuePage.gampTopics.interfaceRequirements,
                    funcSpecSummary           : "Story 3: funcSpecSummary",
                    funcSpecSummaryDescription: "Story 3: funcSpecSummaryDescription - Standard functionallity of Jira supported by configuration of issue type in the ODS project template to suport the creation of the C-CSD document (Combined Specification Document).",
                    configSpecSummary         : "Story 3 configSpecSummary: Configuration Jira",
                    configSpecDescription     : "Story 3 configSpecDescription: template configuration",
            ],
            story4: [
                    summary                   : "Story 4: Test Creating Story 4",
                    description               : "Story 4 description:As user, I want this template to provide issues for requirements creation, in order to store contentn and track the progress of USR documents.",
                    component                 : "Technology-demo-app-front-end",
                    gampTopic                 : CreateStoryIssuePage.gampTopics.interfaceRequirements,
                    funcSpecSummary           : "Story 4: funcSpecSummary",
                    funcSpecSummaryDescription: "Story 4: funcSpecSummaryDescription - Standard functionallity of Jira supported by configuration of issue type in the ODS project template to suport the creation of the C-CSD document (Combined Specification Document).",
                    configSpecSummary         : "Story 4 configSpecSummary: Configuration Jira",
                    configSpecDescription     : "Story 4 configSpecDescription: template configuration",
            ],
    ]

    def static documentChapters = [CSD: [
            "1"  : [edpContent: "This document was prepared for GxP-software development using agile methodologies using BI-AS-ATLASSIAN. The purpose of the document is to ensure the application BI-AS ATLASSIAN supports the planned requirements to manage the agile software development process.",],
            "2"  : [edpContent: "The BI-AS-ATLASSIAN system provides four components, which are in scope of validation:\n" +
                    "Jira: This tool helps users to track and manage project tasks using agile methodologies. It enables users to have transparency of the tasks within the team and is the source of SLC documentation, which is later managed in a document management system.\n" +
                    "Bitbucket: The tool enables the software development team to store the software codes centrally and controlled in a repository. It also helps in version control that integrates with Jira.\n" +
                    "Crowd: Identity management infrastructure for the Atlassian environment: Manage users from multiple sources, like Active Directory or Microsoft Azure AD - and control authentication permissions in one single location.\n" +
                    "Confluence is a Wiki and helps to create, organize and discuss work within the team. It is further used for GBS demand management.",],
            "3.1": [edpContent: "Agile project managment: Jira provides a project management tool. It enables users to track tasks (issues in Jira terminology) through configurable workflows. The tasks are organized by project, allowing tracking issues at a project level with complete transparency. Permissions are granular and on a per-project level. \n" +
                    "Jira projects are provisioned by an ODS component and content from Jira is used to compile SLC documents by ODS for software applications. There can be multiple ODS templates (boilerplates for different software technologies) with different settings for new projects. In general templates contain settings associated with specific project types (i.e. permissions, project type, issue type scheme, etc.).",],
            "3.2": [edpContent: "21 CFR Part 11 Compliance Assessment is required for the system.",],
            "5.1": [edpContent: "|*Term*|*Definition*|\n" +
                    "| AD| Active Directory, a (user and groups) directory service developed by Microsoft for Windows domain Networks.|\n" +
                    "|Azure AD| Azure Active Directory, and AD using the Microsoft Cloud (Azure)|",],
            "5.2": [edpContent: "|*Abbreviation*|*Meaning*|\n" +
                    "| BI| Boehringer Ingelheim|\n" +
                    "| | |",],
            "6"  : [edpContent: "BI-AS-ATLASSIAN Validation Plan, ITEMS DocID 20548551, latest Version.",],
            "7"  : [edpContent: "|*Version*|*Date*|*Author*|*Change Reference*|\n" +
                    "| 1.0|See Summary of electronic document or signature page of printout.| xyz| Initial version|",],
    ]
    ]

    def static technicalSpecifications = [
            tst1: [
                    summary                    : "TST1",
                    component                  : "Technology-demo-app-front-end",
                    systemDesignSpecification  : "TST1: System Design Specification",
                    softwareDesignSpecification: "TST1: Software Design Specification",
            ],
            tst2: [
                    summary                    : "TST2",
                    component                  : "Technology-demo-app-front-end",
                    systemDesignSpecification  : "TST2: System Design Specification",
                    softwareDesignSpecification: "TST2: Software Design Specification",
            ],
            tst4: [
                    summary                    : "TST4",
                    component                  : "Technology-demo-app-front-end",
                    systemDesignSpecification  : "TST4: System Design Specification",
                    softwareDesignSpecification: "TST4: Software Design Specification",
            ],
    ]

    // TEST CASES TEST GROUP 04 – CREATION OF C-CSD
    // Test if a C-CSD document can be created. Start creating an application, use Stories in Jira,
    // amend the Documentation chapter issues and check the issue workflows.
    def "RT_04_001"() {

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

        // STEP 2 Click on “Create” and choose a Jira issue type Story.
        when: "Click on create"
        navigationBar.createLink.click()
        then:
        at IssueCreationSelectorPage

        when: "Select to create a Story"
        selectIssueOfType(IssueCreationSelectorPage.issueType.story)
        report('Step 2 - Start Creating a Story ')
        nextButton.click()

        then: "We are in the issue creation of type Story"
        at CreateStoryIssuePage

        // STEP 3 Add all required information.
        //        Add a component to the Story.
        //        Add a GAMP topic.
        //        Click on “Create”.
        when:
        createIssue(issues.story1, this)
        issues.story1.key = currentUrl.substring(currentUrl.lastIndexOf('/') + 1)

        then: "The story has been created and we are in the IssuePage"
        at IssueBrowsePage
        report('Step 3 - Story 1 created')

        // STEP 4 Repeat steps 2 and 3 creating three additional specifications.
        //        For each Story choose a different GAMP topic.
        when: "Click on create"
        navigationBar.createLink.click()
        then:
        at IssueCreationSelectorPage

        when: "Select to create a Story"
        selectIssueOfType(IssueCreationSelectorPage.issueType.story)
        report('Step 4 - Start Creating a Story 2')
        nextButton.click()

        then: "We are in the issue creation of type Story"
        at CreateStoryIssuePage

        when:
        createIssue(issues.story2, this)
        issues.story2.key = currentUrl.substring(currentUrl.lastIndexOf('/') + 1)

        then: "The story has been created and we are in the IssuePage"
        at IssueBrowsePage
        report('Step 4 - Story 2 created')

        when: "Click on create"
        navigationBar.createLink.click()
        then:
        at IssueCreationSelectorPage

        when: "Select to create a Story"
        selectIssueOfType(IssueCreationSelectorPage.issueType.story)
        report('Step 4 - Start Creating a Story 3')
        nextButton.click()

        then: "We are in the issue creation of type Story"
        at CreateStoryIssuePage

        when:
        createIssue(issues.story3, this)
        issues.story3.key = currentUrl.substring(currentUrl.lastIndexOf('/') + 1)

        then: "The story has been created and we are in the IssuePage"
        at IssueBrowsePage
        report('Step 4 - Story 3 created')

        when: "Click on create"
        navigationBar.createLink.click()
        then:
        at IssueCreationSelectorPage

        when: "Select to create a Story"
        selectIssueOfType(IssueCreationSelectorPage.issueType.story)
        report('Step 4 - Start Creating a Story 4')
        nextButton.click()

        then: "We are in the issue creation of type Story"
        at CreateStoryIssuePage

        when:
        createIssue(issues.story4, this)
        issues.story4.key = currentUrl.substring(currentUrl.lastIndexOf('/') + 1)

        then: "The story has been created and we are in the IssuePage"
        at IssueBrowsePage
        report('Step 4 - Story 4 created')


        // STEP 5 Open Stories 1 and 2 and go through their workflow to the status “Done”.
        when: "Move story 1 to Done"
        moveStoryToDone(issues.story1.key)
        sleep(1000)
        report('Story 1: Move to Done')

        and: "Search for that issue in status Done"
        to IssuesPage
        findIssue(projectName: projectName, issueId: issues.story1.key, status: 'Done')
        sleep(2000)

        then: "There must be one issue"
        waitFor {
            $("tr.issuerow").size() == 1
        }

        when: "Move story 2 to Done"
        moveStoryToDone(issues.story2.key)
        sleep(1000)
        report('Story 2: Move to Done')

        and: "Search for that issue in status Done"
        to IssuesPage
        findIssue(projectName: projectName, issueId: issues.story2.key, status: 'Done')
        sleep(2000)

        then: "There must be one issue"
        $("tr.issuerow").size() == 1

        // STEP 6 Open Story 3 and move it to the status “Cancelled”..
        when: "Move story 3 to Cancelled"
        moveStoryToCancel(issues.story3.key)
        sleep(1000)
        report('Story 3: Move to Cancel')

        and: "Search for that issue in status Done"
        to IssuesPage
        findIssue(projectName: projectName, issueId: issues.story3.key, status: 'Cancelled')
        sleep(2000)

        then: "There must be one issue"
        $("tr.issuerow").size() == 1

        // STEP 7 Open Story 4 and move it to the status “In progress”.
        when: "Move Story 4 into progress"
        moveStoryToInProgress(issues.story4.key)
        sleep(1000)
        report('Story 4: Move to In Progress')

        and: "Search for that issue in status In progress"
        to IssuesPage

        findIssue(projectName: projectName, issueId: issues.story4.key, status: '"In Progress"')
        sleep(2000)

        then: "There must be one issue"
        $("tr.issuerow").size() == 1

        // STEP 8 Filter the Jira issues in order to have all “Documentation chapters” relevant for C-CSD in the list.
        //        Open each issue and amend it accordingly.
        //        Go through the workflow of the “Documentation chapter” issues and set their status to “Done”.
        //        Save the changes.
        when: "Go to the Issues page"
        to IssuesPage
        switchLayoutToList()

        and: "Find the document chapters of CSD"
        findDocumentChapters(projectName: projectName, document: 'CSD')
        sleep(1000)
        report("Document Chapters")

        and: "Visit the document Chapters and set the information in edpContent"
        def documentChaptersIssues = getIssuesList()

        documentChaptersIssues.each {
            to IssueBrowsePage, it.key
            println it
            sleep(1000)
            report()
            edpContentEditor.click()
            sleep(2000)
            waitFor { $("li", 'data-mode': 'source') }.click()
            waitFor { edpContent }.value(documentChapters.CSD[edpHeadingNumber.text()].edpContent)
            waitFor { edpContentSubmitButton }.click()
            sleep(4000)
            waitFor { issueMenu.transitionButtonDocument }.click()
            sleep(4000)
            waitFor { issueMenu.transitionButtonDefine }.click()

            report()
        }

        then: "All finished"
        true


    }

    // Helpers to make more understandable the tests.

    /**
     * Move a issue to status 'Cancel'
     */
    private void moveStoryToDone(key) {
        to IssueBrowsePage, key
        issueMenu.transitionButtonsConfirmDoR().click()
        issueMenu.transitionButtonsImplement().click()
        issueMenu.transitionButtonsIConfirmDoD().click()
    }

    /**
     * Move a issue to status 'Cancel'
     */
    private void moveStoryToCancel(key) {
        to IssueBrowsePage, key
        issueMenu.transitionButtonsCancel().click()
    }

    /**
     * Move a issue to status 'In Progress'
     */
    private void moveStoryToInProgress(key) {
        to IssueBrowsePage, key
        issueMenu.transitionButtonsConfirmDoR.click()
        issueMenu.transitionButtonsImplement.click()
    }

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
        switchLayoutToDetail()
//        if (!searchTextArea) {
//            activateAdvancedSearchLink.click()
//        }

        then:
        assert searchTextArea

        when: "Search fo the Story with summary 'Story 1'"
        findIssue(projectName: projectName, issueId: issues.story1.key)
        switchLayoutToDetail()

        then: "Story 1 exists"
        waitFor { $("ol.issue-list > li").size() == 1 }

        when: "Create a subtask for the Story 1: Open sub task creation form"
        currentStory = $("ol.issue-list > li").first()
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
        to IssueBrowsePage, raData1.key

        then: "Risk priority number must be 18 and risk priority HIGH"
        assert riskprioritynumber.text() == '18'
        assert riskpriority.text() == 'HIGH'
        report('Step_5_Story_1_risk_priority_number and risk_priority')

        // STEP 6
        // Create a Jira sub-task type Risk Assessment “Risk_Medium_wo_PoO” to Story2
        when:
        to IssuesPage
        sleep(1000)
        findIssue(projectName: projectName, issueId: issues.story2.key)
        switchLayoutToDetail()

        then: "Story 2 exists"
        waitFor { $("ol.issue-list > li").size() == 1 }

        when: "Create a subtask for the Story 2: Open sub task creation form"
        currentStory = $("ol.issue-list > li").first()
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
        to IssueBrowsePage, raData2.key

        then: "Risk priority number must be 4 and risk priority MEDIUM"
        assert riskprioritynumber.text() == '4'
        assert riskpriority.text() == 'MEDIUM'
        report('Step_9_Story_2_risk_priority_number and risk_priority')

        // STEP 10
        // Create a Jira sub-task type Risk Assessment “Risk_Low_wo_PoO” to Story3
        when:
        to IssuesPage
        sleep(1000)
        findIssue(projectName: projectName, issueId: issues.story3.key)
        switchLayoutToDetail()

        then: "Story 3 exists"
        waitFor { $("ol.issue-list > li").size() == 1 }

        when: "Create a subtask for the Story 3: Open sub task creation form"
        currentStory = $("ol.issue-list > li").first()
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

        to IssueBrowsePage, raData3.key

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
        assert CreateSubtaskDialogModule.GxPRelevanceGroupTypesStrings[raData1.gxPRelevance].toLowerCase() == ra1.gxPRelevance.replaceAll("\\s", "").replaceAll("/", "").toLowerCase()
        assert CreateSubtaskDialogModule.SeverityOfImpactTypesStrings[raData1.severityOfImpact].toLowerCase() == ra1.severityOfImpact.replaceAll("\\s", "").toLowerCase()
        assert CreateSubtaskDialogModule.ProbabilityOfDetectionTypesStrings[raData1.probabilityOfDetection].toLowerCase() == ra1.probabilityOfDetection.replaceAll("\\s", "").toLowerCase()

        and:
        assert rar.containsKey(raData2.key)
        def ra2 = rar.get(raData2.key)
        assert CreateSubtaskDialogModule.GxPRelevanceGroupTypesStrings[raData2.gxPRelevance].toLowerCase() == ra2.gxPRelevance.replaceAll("\\s", "").replaceAll("/", "").toLowerCase()
        assert CreateSubtaskDialogModule.SeverityOfImpactTypesStrings[raData2.severityOfImpact].toLowerCase() == ra2.severityOfImpact.replaceAll("\\s", "").toLowerCase()
        assert CreateSubtaskDialogModule.ProbabilityOfDetectionTypesStrings[raData2.probabilityOfDetection].toLowerCase() == ra2.probabilityOfDetection.replaceAll("\\s", "").toLowerCase()

        and:
        assert rar.containsKey(raData3.key)
        def ra3 = rar.get(raData3.key)
        assert CreateSubtaskDialogModule.GxPRelevanceGroupTypesStrings[raData3.gxPRelevance].toLowerCase() == ra3.gxPRelevance.replaceAll("\\s", "").replaceAll("/", "").toLowerCase()
        assert CreateSubtaskDialogModule.SeverityOfImpactTypesStrings[raData3.severityOfImpact].toLowerCase() == ra3.severityOfImpact.replaceAll("\\s", "").toLowerCase()
        assert CreateSubtaskDialogModule.ProbabilityOfDetectionTypesStrings[raData3.probabilityOfDetection].toLowerCase() == ra3.probabilityOfDetection.replaceAll("\\s", "").toLowerCase()

        report('Step_14_Risk_Assesment')

    }

    // TEST CASES TEST GROUP 04 – CREATION OF C-CSD
    // Create Technical Specification Tasks in Jira, check their workflow.
    //
    // Depends that the previous test had been executed in order to create the Stories that this test use.
    def "RT_06_001"() {

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

        // STEP 2 Click on “Create” and choose a Jira issue type Technical Specification.
        when: "Click on create"
        navigationBar.createLink.click()
        then:
        at IssueCreationSelectorPage

        when: "Select to create a Technical Specification"
        selectIssueOfType(IssueCreationSelectorPage.issueType.technicalSpecificationTask)
        report('Step 2 - Start Creating a Technical Specification')
        nextButton.click()

        then: "We are in the issue creation of type Technical Specification Task"
        at CreateTechnicalSpecificationTaskIssuePage

        // STEP 3 Add all required information:
        //       Summary: TST1
        //       Description
        //       Systems Design
        //       Specification
        //       Software Design
        //       Specification
        // Click on “Create”.
        when: "We create the Technical Specification Task 1"
        createIssue(technicalSpecifications.tst1, this)
        technicalSpecifications.tst1.key = currentUrl.substring(currentUrl.lastIndexOf('/') + 1)

        then: "The issue is created"
        at IssueBrowsePage
        report("TST1 Created")

        // STEP 4 Link the Story1 to the Technical Specification Task.
        when: "We add a link to the story 1"
        addLinkToIssue(CreateLinkDialogModule.linkType.specifies, issues.story1.key)
        report("Added link to TST1")

        then: "The link exists"
        assert $("a", 'data-issue-key': issues.story1.key).size() == 1

        // STEP 5 Repeat steps 2 and 3 creating an additional Technical Specification Task. Link it to Story2 and call
        //      it TST2.
        //      Repeat steps 2 and 3 creating an additional Technical Specification Task.
        //      Link it to Story4 and call it TST4.

        // Creation and link of TST2
        when: "Click on create"
        navigationBar.createLink.click()

        and: "Select to create a Technical Specification"
        at IssueCreationSelectorPage
        selectIssueOfType(IssueCreationSelectorPage.issueType.technicalSpecificationTask)
        report('Step 5 - Start Creating a Technical Specification 2')
        nextButton.click()

        and: "We create the Technical Specification Task 2"
        at CreateTechnicalSpecificationTaskIssuePage
        createIssue(technicalSpecifications.tst2, this)
        technicalSpecifications.tst2.key = currentUrl.substring(currentUrl.lastIndexOf('/') + 1)

        then: "The issue is created"
        at IssueBrowsePage

        when: "We add a link to the story 2"
        addLinkToIssue(CreateLinkDialogModule.linkType.specifies, issues.story2.key)
        report("Added link to TST2")

        then: "The link exists"
        assert $("a", 'data-issue-key': issues.story2.key).size() == 1

        // Creation and link of TST4
        when: "Click on create"
        navigationBar.createLink.click()

        and: "Select to create a Technical Specification"
        at IssueCreationSelectorPage
        selectIssueOfType(IssueCreationSelectorPage.issueType.technicalSpecificationTask)
        report('Step 5 - Start Creating a Technical Specification 4')
        nextButton.click()

        and: "We create the Technical Specification Task 2"
        at CreateTechnicalSpecificationTaskIssuePage
        createIssue(technicalSpecifications.tst4, this)
        technicalSpecifications.tst4.key = currentUrl.substring(currentUrl.lastIndexOf('/') + 1)

        then: "The issue is created"
        at IssueBrowsePage

        when: "We add a link to the story 2"
        addLinkToIssue(CreateLinkDialogModule.linkType.specifies, issues.story4.key)
        report("Added link to TST4")

        then: "The link exists"
        assert $("a", 'data-issue-key': issues.story4.key).size() == 1

        when: 'Move to status Done'
        technicalSpecifications.each {
            println it
            to IssueBrowsePage, it.value.key
            sleep(1000)
            report()
            waitFor { issueMenu.transitionButtonTstImplement }.click()
            sleep(1000)
            waitFor { issueMenu.transitionButtonsTstConfirmDoD }.click()

        }

        and: "Check all Tehcnical Specification Tasks in Done status"
        to IssuesPage
        findIssue(projectName: projectName, issueId: technicalSpecifications.tst1.key, status: 'Done')
        report('Tecnical Specification Task 1 in Done Status')

        then: 'Technical Specification Task 1 in Done Status'
        waitFor { $("tr.issuerow").size() == 1 }

        when: "Check Tehcnical Specification Tasks 2 in Done status"
        findIssue(projectName: projectName, issueId: technicalSpecifications.tst2.key, status: 'Done')
        report('Tecnical Specification Task 2 in Done Status')

        then: 'Technical Specification Task 2 in Done Status'
        waitFor { $("tr.issuerow").size() == 1 }

        when:
        findIssue(projectName: projectName, issueId: technicalSpecifications.tst4.key, status: 'Done')
        report('Tecnical Specification Task 4 in Done Status')

        then: 'Technical Specification Task 3 in Done Status'
        waitFor { $("tr.issuerow").size() == 1 }
    }
}
