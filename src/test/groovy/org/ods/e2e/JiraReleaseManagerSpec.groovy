package org.ods.e2e


import org.ods.e2e.jira.JiraBaseSpec
import org.ods.e2e.jira.helpers.GampTopics
import org.ods.e2e.jira.helpers.IssueSelectorHelper
import org.ods.e2e.jira.modules.CreateLinkDialogModule
import org.ods.e2e.jira.modules.CreateSubtaskDialogModule
import org.ods.e2e.jira.modules.TestCreationFormModule
import org.ods.e2e.jira.pages.*

class JiraReleaseManagerSpec extends JiraBaseSpec {
    def currentStory
    def currentStoryKey
    def projectSummary
    def static issues = [
            story1: [
                    summary                   : "Story 1: Test Creating Story 1",
                    description               : "As user, I want this template to provide issues for requirements creation, in order to store contentn and track the progress of USR documents.",
                    component                 : "Technology-demo-app-front-end",
                    gampTopic                 : GampTopics.functionalRequirements,
                    funcSpecSummary           : "Standard functionality",
                    funcSpecSummaryDescription: "Standard functionality of Jira supported by configuration of issue type in the ODS project template to suport the creation of the C-CSD document (Combined Specification Document).",
                    configSpecSummary         : "Configuration Jira",
                    configSpecDescription     : "template configuration",
            ],
            story2: [
                    summary                   : "Story 2: Test Creating Story 2",
                    description               : "Story 2 description:As user, I want this template to provide issues for requirements creation, in order to store contentn and track the progress of USR documents.",
                    component                 : "Technology-demo-app-front-end",
                    gampTopic                 : GampTopics.dataRequirements,
                    funcSpecSummary           : "Story 2: funcSpecSummary",
                    funcSpecSummaryDescription: "Story 2: funcSpecSummaryDescription - Standard functionallity of Jira supported by configuration of issue type in the ODS project template to suport the creation of the C-CSD document (Combined Specification Document).",
                    configSpecSummary         : "Story 2 configSpecSummary: Configuration Jira",
                    configSpecDescription     : "Story 2 configSpecDescription: template configuration",
            ],
            story3: [
                    summary                   : "Story 3: Test Creating Story 3",
                    description               : "Story 3 description:As user, I want this template to provide issues for requirements creation, in order to store contentn and track the progress of USR documents.",
                    component                 : "Technology-demo-app-front-end",
                    gampTopic                 : GampTopics.interfaceRequirements,
                    funcSpecSummary           : "Story 3: funcSpecSummary",
                    funcSpecSummaryDescription: "Story 3: funcSpecSummaryDescription - Standard functionallity of Jira supported by configuration of issue type in the ODS project template to suport the creation of the C-CSD document (Combined Specification Document).",
                    configSpecSummary         : "Story 3 configSpecSummary: Configuration Jira",
                    configSpecDescription     : "Story 3 configSpecDescription: template configuration",
            ],
            story4: [
                    summary                   : "Story 4: Test Creating Story 4",
                    description               : "Story 4 description:As user, I want this template to provide issues for requirements creation, in order to store contentn and track the progress of USR documents.",
                    component                 : "Technology-demo-app-front-end",
                    gampTopic                 : GampTopics.interfaceRequirements,
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

    def static tests = [
            testAcceptance1  : [
                    summary          : 'Test_Acceptance_1',
                    description      : 'Given - When - Then',
                    testExecutionType: TestCreationFormModule.TestExecutionTypes.Automated,
                    testLevel        : TestCreationFormModule.TestLevels.Acceptance,
                    steps            : [1: [step: 'do something', data: '', result: 'get it'],
                                        2: [step: 'do something', data: '', result: 'got it'],
                                        3: [step: 'do', data: '', result: 'have it']]
            ],
            testAcceptance2  : [
                    summary          : 'Test_Acceptance_2',
                    description      : 'Given - When - Then',
                    testExecutionType: TestCreationFormModule.TestExecutionTypes.Automated,
                    testLevel        : TestCreationFormModule.TestLevels.Acceptance,
                    steps            : [1: [step: 'do something', data: '', result: 'get it'],
                                        2: [step: 'do something', data: '', result: 'got it'],
                                        3: [step: 'do', data: '', result: 'have it']]
            ],
            testAcceptance4  : [
                    summary          : 'Test_Acceptance_4',
                    description      : 'Given - When - Then',
                    testExecutionType: TestCreationFormModule.TestExecutionTypes.Automated,
                    testLevel        : TestCreationFormModule.TestLevels.Acceptance,
                    steps            : [1: [step: 'do something', data: '', result: 'get it'],
                                        2: [step: 'do something', data: '', result: 'got it'],
                                        3: [step: 'do', data: '', result: 'have it']]
            ],
            testUnit1        : [
                    summary          : 'Test_Unit_1',
                    description      : 'Given - When - Then',
                    testExecutionType: TestCreationFormModule.TestExecutionTypes.Automated,
                    testLevel        : TestCreationFormModule.TestLevels.Unit,
                    steps            : [1: [step: 'do something', data: '', result: 'get it'],
                                        2: [step: 'do something', data: '', result: 'got it'],
                                        3: [step: 'do', data: '', result: 'have it']]
            ],
            testUnit2        : [
                    summary          : 'Test_Unit_2',
                    description      : 'Given - When - Then',
                    testExecutionType: TestCreationFormModule.TestExecutionTypes.Automated,
                    testLevel        : TestCreationFormModule.TestLevels.Unit,
                    steps            : [1: [step: 'do something', data: '', result: 'get it'],
                                        2: [step: 'do something', data: '', result: 'got it'],
                                        3: [step: 'do', data: '', result: 'have it']]
            ],
            testIntegration2 : [
                    summary          : 'Test_Integration2',
                    description      : 'Given - When - Then',
                    testExecutionType: TestCreationFormModule.TestExecutionTypes.Automated,
                    testLevel        : TestCreationFormModule.TestLevels.Integration,
                    steps            : [1: [step: 'do something', data: '', result: 'get it'],
                                        2: [step: 'do something', data: '', result: 'got it'],
                                        3: [step: 'do', data: '', result: 'have it']]
            ],
            testIntegration4 : [
                    summary          : 'Test_Integration4',
                    description      : 'Given - When - Then',
                    testExecutionType: TestCreationFormModule.TestExecutionTypes.Automated,
                    testLevel        : TestCreationFormModule.TestLevels.Integration,
                    steps            : [1: [step: 'do something', data: '', result: 'get it'],
                                        2: [step: 'do something', data: '', result: 'got it'],
                                        3: [step: 'do', data: '', result: 'have it']]
            ],
            testInstallation1: [
                    summary          : 'Test_Installation1',
                    description      : 'Given - When - Then',
                    testExecutionType: TestCreationFormModule.TestExecutionTypes.Automated,
                    testLevel        : TestCreationFormModule.TestLevels.Installation,
                    steps            : [1: [step: 'do something', data: '', result: 'get it'],
                                        2: [step: 'do something', data: '', result: 'got it'],
                                        3: [step: 'do', data: '', result: 'have it']]
            ],
    ]

    def static riskAssesments = [
            riskHighWOPoO  : [storyKey               : issues.story1.key,
                              summaryInput           : 'Risk_High_wo_PoO',
                              descriptionEditor      : 'Risk_High_wo_PoO',
                              riskComment            : 'Must be tested',
                              gxPRelevance           : CreateSubtaskDialogModule.GxPRelevanceGroupTypes.Relevant,
                              severityOfImpact       : CreateSubtaskDialogModule.SeverityOfImpactTypes.High,
                              probabilityOfDetection : CreateSubtaskDialogModule.ProbabilityOfDetectionTypes.AfterImpact,
            ],

            riskMediumWOPoO: [storyKey               : issues.story2.key,
                              summaryInput           : 'Risk_Medium_wo_PoO',
                              descriptionEditor      : 'Risk_Medium_wo_PoO',
                              riskComment            : 'Story 2 comment: Must be tested',
                              gxPRelevance           : CreateSubtaskDialogModule.GxPRelevanceGroupTypes.NotRelevantLess,
                              severityOfImpact       : CreateSubtaskDialogModule.SeverityOfImpactTypes.Medium,
                              probabilityOfDetection : CreateSubtaskDialogModule.ProbabilityOfDetectionTypes.BeforeImpact,
            ],

            riskLowWOPoO   : [storyKey               : issues.story3.key,
                              summaryInput           : 'Risk_Low_wo_PoO',
                              descriptionEditor      : 'Risk_Low_wo_PoO',
                              riskComment            : 'Story 3 comment: Could be tested',
                              gxPRelevance           : CreateSubtaskDialogModule.GxPRelevanceGroupTypes.NotRelevantLess,
                              severityOfImpact       : CreateSubtaskDialogModule.SeverityOfImpactTypes.Low,
                              probabilityOfDetection : CreateSubtaskDialogModule.ProbabilityOfDetectionTypes.Immediate,
            ],

            riskHigh1      : [storyKey               : issues.story1.key,
                              summaryInput           : 'Risk_High1',
                              descriptionEditor      : 'Risk_High1',
                              riskComment            : 'Risk High for story 1 comment: Must be tested',
                              gxPRelevance           : CreateSubtaskDialogModule.GxPRelevanceGroupTypes.Relevant,
                              severityOfImpact       : CreateSubtaskDialogModule.SeverityOfImpactTypes.High,
                              probabilityOfDetection : CreateSubtaskDialogModule.ProbabilityOfDetectionTypes.AfterImpact,
                              probabilityOfOccurrence: CreateSubtaskDialogModule.ProbabilityOfOccurrenceTypes.High,],

            riskMedium2    : [storyKey               : issues.story2.key,
                              summaryInput           : 'Risk Medium2',
                              descriptionEditor      : 'Risk Medium2',
                              riskComment            : 'Risk Medium for story 2 comment: Must be tested',
                              gxPRelevance           : CreateSubtaskDialogModule.GxPRelevanceGroupTypes.NotRelevantLess,
                              severityOfImpact       : CreateSubtaskDialogModule.SeverityOfImpactTypes.Medium,
                              probabilityOfDetection : CreateSubtaskDialogModule.ProbabilityOfDetectionTypes.BeforeImpact,
                              probabilityOfOccurrence: CreateSubtaskDialogModule.ProbabilityOfOccurrenceTypes.Medium,],

            riskLow4       : [storyKey               : issues.story4.key,
                              summaryInput           : 'Risk_Low4',
                              descriptionEditor      : 'Risk_Low4',
                              riskComment            : 'Risk_Low for story 4 comment: Must be tested',
                              gxPRelevance           : CreateSubtaskDialogModule.GxPRelevanceGroupTypes.NotRelevantLess,
                              severityOfImpact       : CreateSubtaskDialogModule.SeverityOfImpactTypes.Low,
                              probabilityOfDetection : CreateSubtaskDialogModule.ProbabilityOfDetectionTypes.BeforeImpact,
                              probabilityOfOccurrence: CreateSubtaskDialogModule.ProbabilityOfOccurrenceTypes.Low,]
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
        projectSummary = (title - ~/- Jira/).trim()

        then: "Login in the project is successful."
        at ProjectPage
        report('Step_1_login')

        // STEP 2 Click on “Create” and choose a Jira issue type Story.
        when: "Click on create"
        navigationBar.createLink.click()
        waitFor {
            issueCreationDialog
        }

        and: "Select to create a Story"
        issueCreationDialog.issueTypeSelectorModule.selectIssueOfType(IssueSelectorHelper.issueType.story)

        then: "We are in the issue creation of type Story"
        report('Step 2 - Start Creating a Story ')

        // STEP 3 Add all required information.
        //        Add a component to the Story.
        //        Add a GAMP topic.
        //        Click on “Create”.
        when:
        issueCreationDialog.storyCreationFormModule.createIssue(issues.story1, this)

        then: "The story has been created"
        waitFor { $('a.issue-created-key.issue-link') }

        when:
        issues.story1.key = $('a.issue-created-key.issue-link').getAttribute('data-issue-key')
        println "issues.story1.key $issues.story1.key"

        and:
        to IssueBrowsePage, issues.story1.key

        then:
        at IssueBrowsePage
        report('Step 3 - Story 1 created')

        // STEP 4 Repeat steps 2 and 3 creating three additional specifications.
        //        For each Story choose a different GAMP topic.
        when: "Click on create"
        navigationBar.createLink.click()
        waitFor {
            issueCreationDialog
        }

        and: "Select to create a Story"
        issueCreationDialog.issueTypeSelectorModule.selectIssueOfType(IssueSelectorHelper.issueType.story)
        report('Step 4 - Start Creating a Story 2')

        and:
        issueCreationDialog.storyCreationFormModule.createIssue(issues.story2, this)
        waitFor { $('a.issue-created-key.issue-link') }
        issues.story2.key = $('a.issue-created-key.issue-link').getAttribute('data-issue-key')
        println "issues.story2.key $issues.story2.key"

        and:
        to IssueBrowsePage, issues.story2.key

        then:
        at IssueBrowsePage
        report('Step 4 - Story 2 created')

        when: "Click on create"
        navigationBar.createLink.click()
        waitFor {
            issueCreationDialog
        }

        and: "Select to create a Story"
        issueCreationDialog.issueTypeSelectorModule.selectIssueOfType(IssueSelectorHelper.issueType.story)
        report('Step 4 - Start Creating a Story 3')

        and:
        issueCreationDialog.storyCreationFormModule.createIssue(issues.story3, this)
        waitFor { $('a.issue-created-key.issue-link') }
        issues.story3.key = $('a.issue-created-key.issue-link').getAttribute('data-issue-key')
        println "issues.story3.key $issues.story3.key"

        and:
        to IssueBrowsePage, issues.story3.key

        then:
        at IssueBrowsePage
        report('Step 4 - Story 3 created')

        when: "Click on create"
        navigationBar.createLink.click()
        waitFor {
            issueCreationDialog
        }

        and: "Select to create a Story"
        issueCreationDialog.issueTypeSelectorModule.selectIssueOfType(IssueSelectorHelper.issueType.story)
        report('Step 4 - Start Creating a Story 4')

        and:
        issueCreationDialog.storyCreationFormModule.createIssue(issues.story4, this)
        waitFor { $('a.issue-created-key.issue-link') }
        issues.story4.key = $('a.issue-created-key.issue-link').getAttribute('data-issue-key')
        println "issues.story4.key $issues.story4.key"

        and:
        to IssueBrowsePage, issues.story4.key

        then:
        at IssueBrowsePage
        report('Step 4 - Story 4 created')


        // STEP 5 Open Stories 1 and 2 and go through their workflow to the status “Done”.
        when: "Move story 1 to Done"
        moveStoryToDone(issues.story1.key)
        sleep(1000)
        report('Story 1: Move to Done')

        then: 'Status must be done'
        $("#status-val > span").text().toLowerCase() == 'done'

        when: 'Move story 2 to Done'
        moveStoryToDone(issues.story2.key)
        sleep(1000)
        report('Story 2: Move to Done')

        then: 'Status must be done'
        $("#status-val > span").text().toLowerCase() == 'done'

        // STEP 6 Open Story 3 and move it to the status “Cancelled”..
        when: "Move story 3 to Cancelled"
        moveStoryToCancel(issues.story3.key)
        sleep(1000)
        report('Story 3: Move to Cancel')

        then: 'Status must be cancelled'
        $("#status-val > span").text().toLowerCase() == 'cancelled'

        // STEP 7 Open Story 4 and move it to the status “In progress”.
        when: "Move Story 4 into progress"
        moveStoryToInProgress(issues.story4.key)
        sleep(1000)
        report('Story 4: Move to In Progress')

        then: 'Status must be in progress'
        $("#status-val > span").text().toLowerCase() == 'in progress'

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
            sleep(1000)
            report()
            editIssueButton.click()
            waitFor { documentChapterDialogModule }
            sleep(1000)
            waitFor { $("li", 'data-mode': 'source') }*.click()
            waitFor { documentChapterDialogModule.edpContent }.value(
                    documentChapters.CSD[documentChapterDialogModule.edpHeadingNumber.value()].edpContent)
            waitFor { documentChapterDialogModule.submitButton }.click()
            sleep(1000)
            if (issueMenu.transitionButtonsReopenDocumentChapter.size() != 0) {
                issueMenu.transitionButtonsReopenDocumentChapter.click()
            }
            sleep(1000)
            waitFor { issueMenu.transitionButtonDocument }.click()
            sleep(1000)
            waitFor { issueMenu.transitionButtonDefine }.click()

            report()
        }

        then: "All finished"
        true
    }


    // TEST CASES TEST GROUP 02
    // CHECK THE CORRECTNESS OF CALCULATION – RISK ASSESSMENT WITHOUT PROBABILITY OF OCCURRENCE
    def "RT_02_001"() {
        // STEP 1 Log in as team member who has rights to the project.
        given: 'Log in as team member who has rights to the project'
        to DashboardPage
        loginForm.doLoginProcess()

        expect: 'We can login in Jira'
        at DashboardPage

        when: 'visit project page'
        to ProjectPage, projectName
        projectSummary = (title - ~/- Jira/).trim()

        then: 'Login in the project is successful.'
        at ProjectPage
        report('Step_1_login')

        // STEP 2 - Create a Jira sub-task type Risk Assessment “Risk_High_wo_PoO” to Story1
        when: 'Visit issues page'
        to IssueBrowsePage, issues.story1.key

        and:
        issueMenu.clickCreateSubtask()

        then: 'the create subtask dialog is displayed'
        assert createSubtaskDialog
        report('Step_2_window with information to specify')

        //--------------------------------------------------------------------------------------------------------------
        // STEP 3:
        // Add following information:
        // - Risk description
        // - GxP relevance: Relevant
        // - Severity of Impact: Low, Medium, High → choose High
        // - Probability of Detection: Immediate, Before, Impact, After Impact → choose After Impact
        // Add a comment to the risk.
        //--------------------------------------------------------------------------------------------------------------
        when: 'Fill the data'
        createSubtaskDialog.fillRiskSubtask(riskAssesments.riskHighWOPoO, 'RT_02_001_Step3_Story1')

        // STEP 4
        and: 'Click on create'
        createSubtaskDialog.createSubmitButton.click()
        sleep(2000)
        report('Step_4_Open_Create_Subtask_for_Story_1')

        // STEP 5
        and: 'Check if the Risk Priority Number (RPN) and Risk Priority is automatically calculated'
        riskAssesments.riskHighWOPoO.key = subsTaskIssues.last().getAttribute("data-issuekey")
        to IssueBrowsePage, riskAssesments.riskHighWOPoO.key

        then: 'Risk priority number must be 18 and risk priority HIGH'
        assert riskprioritynumber.text() == '18'
        assert riskpriority.text() == 'HIGH'
        report('Step_5_Story_1_risk_priority_number and risk_priority')

        // STEP 6
        // Create a Jira sub-task type Risk Assessment “Risk_Medium_wo_PoO” to Story2
        when:
        to IssueBrowsePage, issues.story2.key

        and:
        issueMenu.clickCreateSubtask()

        then: 'the create subtask dialog is displayed'
        assert createSubtaskDialog
        report('Step_7_window with information to specify')

        // STEP 7
        when: 'Fill the data'
        createSubtaskDialog.fillRiskSubtask(riskAssesments.riskMediumWOPoO, 'RT_02_001_Step7_Story2')

        // STEP 8
        and: 'Click create'
        createSubtaskDialog.createSubmitButton.click()
        sleep(2000)
        report('Step_8_Open_Create_Subtask_for_Story_2')

        // STEP 9 Check if the Risk Priority Number (RPN) and Risk Priority is automatically calculated
        and: 'Check if the Risk Priority Number (RPN) and Risk Priority is automatically calculated'
        riskAssesments.riskMediumWOPoO.key = subsTaskIssues.last().getAttribute("data-issuekey")
        to IssueBrowsePage, riskAssesments.riskMediumWOPoO.key

        then: 'Risk priority number must be 4 and risk priority MEDIUM'
        assert riskprioritynumber.text() == '4'
        assert riskpriority.text() == 'MEDIUM'
        report('Step_9_Story_2_risk_priority_number and risk_priority')

        // STEP 10
        // Create a Jira sub-task type Risk Assessment “Risk_Low_wo_PoO” to Story3
        when:
        to IssueBrowsePage, issues.story3.key

        and:
        issueMenu.clickCreateSubtask()

        then: "the create subtask dialog is displayed"
        assert createSubtaskDialog
        report('Step_10_Open_Create_Subtask_for_Story_3')

        // STEP 11
        when: "Fill the data"
        createSubtaskDialog.fillRiskSubtask(riskAssesments.riskLowWOPoO, 'RT_02_001_Step11_Story3')

        // STEP 13
        and: "Click create"
        createSubtaskDialog.createSubmitButton.click()
        sleep(5000)

        // STEP 12 Check if the Risk Priority Number (RPN) and Risk Priority is automatically calculated
        and: "Check if the Risk Priority Number (RPN) and Risk Priority is automatically calculated"
        riskAssesments.riskLowWOPoO.key = subsTaskIssues.last().getAttribute("data-issuekey")

        to IssueBrowsePage, riskAssesments.riskLowWOPoO.key

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
        assert rar.containsKey(riskAssesments.riskHighWOPoO.key)
        def ra1 = rar.get(riskAssesments.riskHighWOPoO.key)
        assert CreateSubtaskDialogModule.GxPRelevanceGroupTypesStrings[riskAssesments.riskHighWOPoO.gxPRelevance].toLowerCase() == ra1.gxPRelevance.replaceAll("\\s", "").replaceAll("/", "").toLowerCase()
        assert CreateSubtaskDialogModule.SeverityOfImpactTypesStrings[riskAssesments.riskHighWOPoO.severityOfImpact].toLowerCase() == ra1.severityOfImpact.replaceAll("\\s", "").toLowerCase()
        assert CreateSubtaskDialogModule.ProbabilityOfDetectionTypesStrings[riskAssesments.riskHighWOPoO.probabilityOfDetection].toLowerCase() == ra1.probabilityOfDetection.replaceAll("\\s", "").toLowerCase()

        and:
        assert rar.containsKey(riskAssesments.riskMediumWOPoO.key)
        def ra2 = rar.get(riskAssesments.riskMediumWOPoO.key)
        assert CreateSubtaskDialogModule.GxPRelevanceGroupTypesStrings[riskAssesments.riskMediumWOPoO.gxPRelevance].toLowerCase() == ra2.gxPRelevance.replaceAll("\\s", "").replaceAll("/", "").toLowerCase()
        assert CreateSubtaskDialogModule.SeverityOfImpactTypesStrings[riskAssesments.riskMediumWOPoO.severityOfImpact].toLowerCase() == ra2.severityOfImpact.replaceAll("\\s", "").toLowerCase()
        assert CreateSubtaskDialogModule.ProbabilityOfDetectionTypesStrings[riskAssesments.riskMediumWOPoO.probabilityOfDetection].toLowerCase() == ra2.probabilityOfDetection.replaceAll("\\s", "").toLowerCase()

        and:
        assert rar.containsKey(riskAssesments.riskLowWOPoO.key)
        def ra3 = rar.get(riskAssesments.riskLowWOPoO.key)
        assert CreateSubtaskDialogModule.GxPRelevanceGroupTypesStrings[riskAssesments.riskLowWOPoO.gxPRelevance].toLowerCase() == ra3.gxPRelevance.replaceAll("\\s", "").replaceAll("/", "").toLowerCase()
        assert CreateSubtaskDialogModule.SeverityOfImpactTypesStrings[riskAssesments.riskLowWOPoO.severityOfImpact].toLowerCase() == ra3.severityOfImpact.replaceAll("\\s", "").toLowerCase()
        assert CreateSubtaskDialogModule.ProbabilityOfDetectionTypesStrings[riskAssesments.riskLowWOPoO.probabilityOfDetection].toLowerCase() == ra3.probabilityOfDetection.replaceAll("\\s", "").toLowerCase()

        report('Step_14_Risk_Assesment')

    }

    /**
     * Test Objective:
     * Risk assessment with Probability of Occurrence: Create several Risk Assessments with different
     * occurrence-severity-detectability combinations and risk priority. Link them to URS-items/issues and to
     * mitigations and tests.
     *
     * Prerequisites:
     * The project where the Risk Assessments have the field probability of occurrence shall be used. A project in Dev
     * with the same name as the project in Q is available.The project has already created stories from Test Case
     * RT_04_001.
     */
    def "RT_05_001"() {
        // -------------------------------------------------------------------------------------------------------------
        // STEP 1: Log in to Jira as team member who has rights to the project.
        //          Result: Login works, within a provisioning and history links
        // -------------------------------------------------------------------------------------------------------------
        given: "Log in as team member who has rights to the project"
        to DashboardPage
        loginForm.doLoginProcess()

        expect: "We can login in Jira"
        at DashboardPage

        when: "visit project page"
        to ProjectPage, projectName
        projectSummary = (title - ~/- Jira/).trim()

        then: "Login in the project is successful."
        at ProjectPage
        report('Step_01_login')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 2: Create a Jira sub-task type Risk Assessment “Risk_High1” to Story1:
        // Therefore open Story1 → select under “More” the button “Create subtask”.
        //          Result: A window with information to specify opens.
        // -------------------------------------------------------------------------------------------------------------
        when:
        to IssueBrowsePage, issues.story1.key

        and:
        issueMenu.clickCreateSubtask()

        then: "the create subtask dialog is displayed"
        assert createSubtaskDialog
        report('Step_02_window with information to specify')


        // -------------------------------------------------------------------------------------------------------------
        // STEP 3: Add following information:
        // - Risk description
        // - GxP relevance :None (Default value), Relevant, Not relevant/EQUAL, Not relevant/LESS, Not relevant/ZERO →
        //   choose Relevant
        // - Probability of occurrence Low, Medium, High (= 1,2,3) →choose High Severity of Impact: Low, Medium, High →
        //   choose High
        // - Probability of Detection : Immediate, Before Impact, After Impact → choose After Impact
        // - Add a comment.
        //          Result: It is possible to add all information defined in the instruction.
        // -------------------------------------------------------------------------------------------------------------
        when: "Fill the data"
        createSubtaskDialog.fillRiskSubtask(riskAssesments.riskHigh1, 'RT_05_001_Step_03_Fill_Risk_Data')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 4: Click on "create"
        //         Result: New Risk Assessment with all required information has been created successfully. It is
        //         related to the Story 1.
        // -------------------------------------------------------------------------------------------------------------
        and: "Click on create"
        createSubtaskDialog.createSubmitButton.click()
        sleep(1000)
        report('Step_04_Subtask_Created_for_Story_1')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 5: Check if the Risk Priority Number (RPN) and Risk Priority is automatically calculated
        //         Result: RPN = GxP * Po * Pd * Si = 2*3*3*3 =  54
        //                 Risk Priority is 'High' (equal to value 1)
        // -------------------------------------------------------------------------------------------------------------
        and: "Check if the Risk Priority Number (RPN) and Risk Priority is automatically calculated"
        riskAssesments.riskHigh1.key = subsTaskIssues.last().getAttribute("data-issuekey")
        to IssueBrowsePage, riskAssesments.riskHigh1.key

        then: "Risk priority number must be 18 and risk priority HIGH"
        assert riskprioritynumber.text() == '54'
        assert riskpriority.text().toUpperCase() == 'HIGH'
        report('Step_05_Story_1_risk_priority_number and risk_priority')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 6: Create a Jira issue type test Test_Acceptance1.
        //          Result: A window with information to specify opens.
        // -------------------------------------------------------------------------------------------------------------
        when: "Click on create"
        navigationBar.createLink.click()
        waitFor {
            issueCreationDialog
        }

        and: "Select to create a Test"
        issueCreationDialog.issueTypeSelectorModule.selectIssueOfType(IssueSelectorHelper.issueType.test)

        then: "We are in the issue creation of type Test"
        report('Step_06_Start_Creating_a_Test ')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 7: Add following information:
        //         - Description (Given-When- Then)
        //         - Test Execution Type. Automated, Manual → Choose Automated
        //         - Test Level: Choose Acceptance
        //         - Test steps
        //         Link the test to the “Risk_High1”.
        //          Result: The test Test_Acceptance1 is successfully created and it is linked to the Risk Assessment
        //                  “Risk_High1”.
        // -------------------------------------------------------------------------------------------------------------
        when:
        issueCreationDialog.testCreationFormModule.createIssue(tests.testAcceptance1, this)

        then: "The test has been created"
        waitFor { $('a.issue-created-key.issue-link') }

        when:
        tests.testAcceptance1.key = $('a.issue-created-key.issue-link').getAttribute('data-issue-key')
        println "tests.testAcceptance1.key $tests.testAcceptance1.key"

        and: 'Navigate to the Risk Assesment'
        to IssueBrowsePage, riskAssesments.riskHigh1.key

        then: 'We are in the Risk Assesment page'
        at IssueBrowsePage

        when: 'Link the RA to he Test'
        addLinkToIssue(CreateLinkDialogModule.linkType.isTestedBy, tests.testAcceptance1.key)


        then:
        report('Step_07_Test_1_created')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 8: Set the status of the “Test_Acceptance1” to “Done”.
        //          Result: The test ‘Test_ Acceptance1’ has status “Done”.
        // -------------------------------------------------------------------------------------------------------------
        when:
        moveTestToDone(tests.testAcceptance1.key)

        then:
        $("#status-val > span").text().toLowerCase() == 'done'
        report('Step_08_Test_acceptance1_move_to_done')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 9: Check that no mitigation, but one test is linked to the risk.
        //         Select the menu point “reports” → Click on the button “Risk Assessment” → Select the current project
        //         and click on “next”.
        //         Result: The result that no mitigation, but one test is linked to the risk can be looked up under
        //                  reports. Thus, the risk remains uncovered.
        // -------------------------------------------------------------------------------------------------------------
        when: "Visit the Report Selector Page"
        to ReportSelectorPage, projectName
        report('Report select page')
        riskAssesmentLink.click()

        and: "Select the current project"
        $("form").projectId = projectSummary
        $("#next_submit").click()

        then: "In the report page"
        at RiskAssementReportPage

        when: 'Retrieve information from the risk assesment'
        Map rar = getRiskAssesmentReport()

        then:
        assert rar.containsKey(riskAssesments.riskHigh1.key)

        and:
        rar.get(riskAssesments.riskHigh1.key).mitigationTests.size() == 1
        rar.get(riskAssesments.riskHigh1.key).mitigationTests[0].key == tests.testAcceptance1.key
        report('Step_09_Check_risk_assement_report')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 10: Create a Jira issue type mitigation “Mitigation_High1”.
        //          Result: A window with information to specify opens.
        // -------------------------------------------------------------------------------------------------------------
        when: "Click on create"
        to IssueBrowsePage, riskAssesments.riskHigh1.key
        navigationBar.createLink.click()
        waitFor {
            issueCreationDialog
        }

        and: "Select to create a Mitigation"
        issueCreationDialog.issueTypeSelectorModule.selectIssueOfType(IssueSelectorHelper.issueType.mitigation)

        then: "We are in the issue creation of type Test"
        report('Step_10_Start_Creating_a_Mitigation ')
        // -------------------------------------------------------------------------------------------------------------
        // STEP 11: Add the following information:
        //          - Summary: Mitigation_High1
        //          - Description
        //          Link the mitigation to the “Risk_High1”.
        //          Result: The mitigation is successfully created and it is linked to the risk “Risk_High1”.
        // -------------------------------------------------------------------------------------------------------------
        when:
        def mitigationData1 = [
                summary    : 'Mitigation High 1',
                description: 'Mitigation for Risk Hight 1',
        ]

        issueCreationDialog.mitigationCreationFormModule.createIssue(mitigationData1, this)

        then: "The mitigatoin has been created"
        waitFor { $('a.issue-created-key.issue-link') }

        when:
        mitigationData1.key = $('a.issue-created-key.issue-link').getAttribute('data-issue-key')
        println "mitigationData1.key $mitigationData1.key"

        and: 'Navigate to the Migitation'
        to IssueBrowsePage, mitigationData1.key

        then: 'We are in the Migitation page'
        at IssueBrowsePage

        when: 'Link the RA to he Test'
        addLinkToIssue(CreateLinkDialogModule.linkType.mitigates, riskAssesments.riskHigh1.key)

        then:
        report('Step_11_Mitigation_created')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 12: Check that one mitigation and one test are linked to the Risk Assessment.
        //          Select the menu point “reports” → Click on the button “Risk Assessment” → Select the current project
        //          and click on “next”.
        //          Result: The result that one mitigation and one test are linked to the Risk Assessment can be looked
        //                  up under reports. Thus, we have a successful report on the completeness of risk mitigations.
        // -------------------------------------------------------------------------------------------------------------
        when: "Visit the Report Selector Page"
        to ReportSelectorPage, projectName
        report('Report select page')
        riskAssesmentLink.click()

        and: "Select the current project"
        $("form").projectId = projectSummary
        $("#next_submit").click()

        then: "In the report page"
        at RiskAssementReportPage

        when: 'Retrieve information from the risk assesment'
        rar = getRiskAssesmentReport()
        then:
        assert rar.containsKey(riskAssesments.riskHigh1.key)

        and:
        rar.get(riskAssesments.riskHigh1.key).mitigationTests.size() == 2
        rar.get(riskAssesments.riskHigh1.key).mitigationTests.findAll {
            it.key == tests.testAcceptance1.key || it.key == mitigationData1.key
        }.size() == 2

        report('Step_12_Check_risk_assement_report')


        // -------------------------------------------------------------------------------------------------------------
        // STEP 13: Reevaluate the “Risk_High1”.
        //          Result: The calculation of Risk Priority Number (RPN) and Risk Priority is automatically amended.
        // -------------------------------------------------------------------------------------------------------------
        when: 'change the risk assesment'
        to IssueBrowsePage, riskAssesments.riskHigh1.key
        editIssueButton.click()
        waitFor { createSubtaskDialog }
        sleep(1000)
        createSubtaskDialog.updateRiskSubtask(
                [probabilityOfDetection: CreateSubtaskDialogModule.ProbabilityOfDetectionTypes.BeforeImpact],
                'RT_05_001_Step_13_Update_Risk'
        )
        and: "Click on update"
        createSubtaskDialog.createSubmitButton.click()
        sleep(1000)
        report('Step_13_Update_Risk')

        and: 'Reload page to check that it has been updated'
        to IssueBrowsePage, riskAssesments.riskHigh1.key

        then: "Risk priority number must be 18 and risk priority HIGH"
        assert riskprioritynumber.text() == '36'
        assert riskpriority.text() == 'HIGH'
        report('Step_13_Story_1_risk_priority_number_recalculated')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 14: Move the Risk Assessment “Risk_High1” to the next status “done” (click on “approve”).
        //          Result: The Risk Assessment has the status done.
        // -------------------------------------------------------------------------------------------------------------
        when:
        moveRiskAssesmentToDone(riskAssesments.riskHigh1.key)

        then:
        $("#status-val > span").text().toLowerCase() == 'done'
        report('Step_1_Story_1_risk_move_to_done')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 15: Create a Jira sub-task type Risk Assessment “Risk_Medium2” to Story2.
        //          Result: A window with information to specify opens.
        // -------------------------------------------------------------------------------------------------------------
        when:
        to IssueBrowsePage, issues.story2.key

        and:
        issueMenu.clickCreateSubtask()

        then: "the create subtask dialog is displayed"
        assert createSubtaskDialog
        report('Step_15_window with information to specify')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 16: Add following information:
        //          - Risk description
        //          - GxP relevance : Notrelevant/LESS
        //          - Probability of occurrence: Medium
        //          - Severity of Impact:Medium
        //          - Probability of Detection: Before Impact
        //          Leave the field comment empty.
        //          Result: It is possible to add all information defined in the instruction.
        // -------------------------------------------------------------------------------------------------------------
        when: "Fill the data"
        createSubtaskDialog.fillRiskSubtask(riskAssesments.riskMedium2, 'RT_05_001_Step_16_Fill_Risk_Data')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 17: Click on “Create”.
        //          Result: New Risk Assessment with all required information has been created successfully. It is
        //                  related to the Story2.
        // -------------------------------------------------------------------------------------------------------------
        and: "Click on create"
        createSubtaskDialog.createSubmitButton.click()
        sleep(1000)
        report('Step_17_Subtask_Created_for_Story_2')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 18: Check if the Risk Priority Number (RPN) and Risk Priority is automatically calculated
        //          Result: RPN = 8
        //                  Risk Priority is “Medium” (equal to value 2).
        // -------------------------------------------------------------------------------------------------------------
        and: "Check if the Risk Priority Number (RPN) and Risk Priority is automatically calculated"
        riskAssesments.riskMedium2.key = subsTaskIssues.last().getAttribute("data-issuekey")
        to IssueBrowsePage, riskAssesments.riskMedium2.key

        then: "Risk priority number must be 8 and risk priority Medium"
        assert riskprioritynumber.text() == '8'
        assert riskpriority.text() == 'MEDIUM'
        report('Step_18_Story_2_risk_priority_number and risk_priority')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 19: Create a Jira issue type mitigation “Mitigation_Medium2”.
        //          Result: A window with information to specify opens.
        // -------------------------------------------------------------------------------------------------------------
        when: "Click on create"
        to IssueBrowsePage, riskAssesments.riskMedium2.key
        navigationBar.createLink.click()
        waitFor {
            issueCreationDialog
        }

        and: "Select to create a Mitigation"
        issueCreationDialog.issueTypeSelectorModule.selectIssueOfType(IssueSelectorHelper.issueType.mitigation)

        then: "We are in the issue creation of type Test"
        report('Step_19_Start_Creating_a_Mitigation')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 20: Add the following information:
        //          - Summary: Mitigation_Medium2
        //          - Description
        //          Link the mitigation to the “Risk_Medium2”.
        //          Result: The mitigation is successfully created and it is linked to the Risk Assessment
        //                  “Risk_Medium2”.
        // -------------------------------------------------------------------------------------------------------------
        when:
        def mitigationData2 = [
                summary    : 'Mitigation_Medium2',
                description: 'Mitigation for Risk Hight 1',
        ]

        issueCreationDialog.mitigationCreationFormModule.createIssue(mitigationData2, this)

        then: "The mitigation has been created"
        waitFor { $('a.issue-created-key.issue-link') }

        when:
        mitigationData2.key = $('a.issue-created-key.issue-link').getAttribute('data-issue-key')
        println "mitigationData2.key $mitigationData2.key"

        and: 'Navigate to the Migitation'
        to IssueBrowsePage, mitigationData2.key

        then: 'We are in the Migitation page'
        at IssueBrowsePage

        when: 'Link the RA to he Test'
        addLinkToIssue(CreateLinkDialogModule.linkType.mitigates, riskAssesments.riskMedium2.key)

        then:
        report('Step_20_Mitigation_created')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 21: Reevaluate the “Risk_Medium2”.
        //          Result: The calculation of Risk Priority Number (RPN) and Risk Priority is automatically amended.
        // -------------------------------------------------------------------------------------------------------------
        when: 'change the risk assesment'
        to IssueBrowsePage, riskAssesments.riskMedium2.key
        editIssueButton.click()
        waitFor { createSubtaskDialog }
        sleep(1000)
        riskAssesments.riskMedium2.probabilityOfDetection = CreateSubtaskDialogModule.ProbabilityOfDetectionTypes.Immediate
        createSubtaskDialog.updateRiskSubtask(
                [probabilityOfDetection: riskAssesments.riskMedium2.probabilityOfDetection],
                'RT_05_001_Step_21_Update_Risk_Assesment'
        )
        and: "Click on update"
        createSubtaskDialog.createSubmitButton.click()
        sleep(1000)
        report('Step_21_Update_Risk_Assesment')

        and: 'Reload page to check that it has been updated'
        to IssueBrowsePage, riskAssesments.riskMedium2.key

        then: "Risk priority number must be 4 and risk priority LOW"
        assert riskprioritynumber.text() == '4'
        assert riskpriority.text().toUpperCase() == 'LOW'
        report('Step_21_Risk_priority_number_recalculated')
        // -------------------------------------------------------------------------------------------------------------
        // STEP 22: Move the Risk Assessment “Risk_Medium2” to the next status “done” (click on ‘approve’).
        //          Result: The Risk Assessment has the status done.
        // -------------------------------------------------------------------------------------------------------------
        when:
        moveRiskAssesmentToDone(riskAssesments.riskMedium2.key)

        then:
        $("#status-val > span").text().toLowerCase() == 'done'
        report('Step_22_Risk_move_to_done')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 23: Create a Jira sub-task type Risk Assessment “Risk_Low4” to Story4.
        //          Result: A window with information to specify opens.
        // -------------------------------------------------------------------------------------------------------------
        when:
        to IssueBrowsePage, issues.story4.key

        and:
        issueMenu.clickCreateSubtask()

        then: "the create subtask dialog is displayed"
        assert createSubtaskDialog
        report('Step_23_window_with_information_to_specify')


        // -------------------------------------------------------------------------------------------------------------
        // STEP 24: Add following information: - Risk description
        //          - GxP relevance : Not relevant/LESS
        //          - Probability of occurrence_ Low (equal to value 1)
        //          - Severity of Impact: Low
        //          - Probability of Detection: Before Impact
        //            Add a comment to the risk.
        //          Result: It is possible to add all information defined in the instruction.
        // -------------------------------------------------------------------------------------------------------------
        when: "Fill the data"
        createSubtaskDialog.fillRiskSubtask(riskAssesments.riskLow4, 'RT_05_001_Step24_Fill_Risk_Data')

        and: "Click on create"
        createSubtaskDialog.createSubmitButton.click()
        sleep(1000)
        report('Step_24_Subtask_Created_for_Story_4')


        // -------------------------------------------------------------------------------------------------------------
        // STEP 25: Check if the Risk Priority Number (RPN) and Risk Priority is automatically calculated
        //          Result: RPN = 2.
        //          Risk Priority is “Low” (equal to value 3).
        // -------------------------------------------------------------------------------------------------------------
        and: "Check if the Risk Priority Number (RPN) and Risk Priority is automatically calculated"
        riskAssesments.riskLow4.key = subsTaskIssues.last().getAttribute("data-issuekey")
        to IssueBrowsePage, riskAssesments.riskLow4.key

        then: "Risk priority number must be 2 and risk priority HIGH"
        assert riskprioritynumber.text() == '2'
        assert riskpriority.text().toUpperCase() == 'LOW'
        report('Step_25_Story_4_risk_priority_number_and_risk_priority')

        // -------------------------------------------------------------------------------------------------------------
        // TODO: Ammend the documents, the step must be done previously
        // STEP 26: Click on “Create”.
        //          Result: New Risk Assessment with all required information has been created successfully. It is
        // related to Story4.
        // -------------------------------------------------------------------------------------------------------------

        // -------------------------------------------------------------------------------------------------------------
        // STEP 27: Create a Jira issue type test “Test_Integration4”.
        //          Result: A window with information to specify opens.
        // -------------------------------------------------------------------------------------------------------------
        when: "Click on create"
        navigationBar.createLink.click()
        waitFor {
            issueCreationDialog
        }

        and: "Select to create a Test"
        issueCreationDialog.issueTypeSelectorModule.selectIssueOfType(IssueSelectorHelper.issueType.test)

        then: "We are in the issue creation of type Test"
        report('Step_27_Start_Creating_a_Test')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 28: Add following information:
        //          - Description (Given-When-Then)
        //          - Test Execution Type. Automated, Manual → Choose Automated
        //          - Test Level: Choose Integration
        //          - Test steps Link the test to the “Risk_Low4”.
        //          Result: The test ‘Test_Integration4” is successfully created and it’s linked to the Risk Assessment
        //                  “Risk_Low4”.
        // -------------------------------------------------------------------------------------------------------------
        when:
        issueCreationDialog.testCreationFormModule.createIssue(tests.testIntegration4, this)

        then: "The test has been created"
        waitFor { $('a.issue-created-key.issue-link') }

        when:
        tests.testIntegration4.key = $('a.issue-created-key.issue-link').getAttribute('data-issue-key')
        println "test.testIntegration4.key $tests.testIntegration4.key"

        and: 'Navigate to the Risk Assesment'
        to IssueBrowsePage, riskAssesments.riskLow4.key

        then: 'We are in the Risk Assesment page'
        at IssueBrowsePage

        when: 'Link the RA to he Test'
        addLinkToIssue(CreateLinkDialogModule.linkType.isTestedBy, tests.testIntegration4.key)

        then:
        report('Step_28_Test_2_created')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 29: Set to status of the “Test_Integration4” to “Done”.
        //          Result: The test ‘Test_Integration4’ has status “Done”.
        // -------------------------------------------------------------------------------------------------------------
        when:
        moveTestToDone(tests.testIntegration4.key)

        then:
        $("#status-val > span").text().toLowerCase() == 'done'
        report('Step_29_Test_Acceptance_to_done')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 30: Reevaluate the “Risk_Low4”.
        //          Result: The calculation of Risk Priority Number (RPN) and Risk Priority is automatically amended.
        // -------------------------------------------------------------------------------------------------------------
        when: 'change the risk assesment'
        to IssueBrowsePage, riskAssesments.riskLow4.key
        editIssueButton.click()
        waitFor { createSubtaskDialog }
        sleep(1000)
        riskAssesments.riskLow4.probabilityOfDetection = CreateSubtaskDialogModule.ProbabilityOfDetectionTypes.Immediate
        createSubtaskDialog.updateRiskSubtask(
                [probabilityOfDetection: riskAssesments.riskLow4.probabilityOfDetection],
                'RT_05_001_Step_30_Update_Risk_Assesment'
        )
        and: "Click on update"
        createSubtaskDialog.createSubmitButton.click()
        sleep(1000)
        report('Step_30_Update_Risk_Assesment')

        and: 'Reload page to check that it has been updated'
        to IssueBrowsePage, riskAssesments.riskLow4.key

        then: "Risk priority number must be 4 and risk priority LOW"
        assert riskprioritynumber.text() == '1'
        assert riskpriority.text().toUpperCase() == 'LOW'
        report('Step_21_Risk_priority_number_recalculated')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 31: Move the Risk Assessment “Risk_Low4” to the next status “done” (click on ‘approve’).
        //          Result: The Risk Assessment has the status done.
        // -------------------------------------------------------------------------------------------------------------
        when:
        moveRiskAssesmentToDone(riskAssesments.riskLow4.key)

        then:
        $("#status-val > span").text().toLowerCase() == 'done'
        report('Step_31_Risk_move_to_done')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 32: - Create a “Test_Acceptance2” (Test level “Acceptance”) linked to the “Risk_Medium2”.
        //          - Create a “Test_Unit1” (Test level “Unit”) linked to the “Risk_High1”.
        //          - Create a “Test_Unit2” (Test level “Unit”) linked to the “Risk_Medium2”.
        //          - Create a “Test_Acceptance4” linked directly to Story4.
        //          - Create “Test_Integration2” linked to the “Risk_Medium2”.
        //          - Create “Test_Installation1” linked to the “Risk_High1”.
        //          Result: New tests have been created and are linked to the correct issues.
        // -------------------------------------------------------------------------------------------------------------
        when:
        createTest(tests.testAcceptance2)
        to IssueBrowsePage, riskAssesments.riskMedium2.key
        addLinkToIssue(CreateLinkDialogModule.linkType.isTestedBy, tests.testAcceptance2.key)

        and:
        createTest(tests.testUnit1)
        to IssueBrowsePage, riskAssesments.riskHigh1.key
        addLinkToIssue(CreateLinkDialogModule.linkType.isTestedBy, tests.testUnit1.key)

        and:
        createTest(tests.testUnit2)
        to IssueBrowsePage, riskAssesments.riskMedium2.key
        addLinkToIssue(CreateLinkDialogModule.linkType.isTestedBy, tests.testUnit2.key)

        and:
        createTest(tests.testAcceptance4)
        to IssueBrowsePage, issues.story4.key
        addLinkToIssue(CreateLinkDialogModule.linkType.isTestedBy, tests.testAcceptance4.key)

        and:
        createTest(tests.testIntegration2)
        to IssueBrowsePage, riskAssesments.riskMedium2.key
        addLinkToIssue(CreateLinkDialogModule.linkType.isTestedBy, tests.testIntegration2.key)

        and:
        createTest(tests.testInstallation1)
        to IssueBrowsePage, riskAssesments.riskHigh1.key
        addLinkToIssue(CreateLinkDialogModule.linkType.isTestedBy, tests.testInstallation1.key)

        and: "Visit the Report Selector Page"
        to ReportSelectorPage, projectName
        riskAssesmentLink.click()

        and: "Select the current project"
        $("form").projectId = projectSummary
        $("#next_submit").click()

        then: "In the report page"
        at RiskAssementReportPage

        when:
        rar = getRiskAssesmentReport()

        then:
        rar.containsKey(riskAssesments.riskMedium2.key)

        rar.get(riskAssesments.riskMedium2.key).mitigationTests.findAll {
            it.title == 'Test_Acceptance2' ||
                    it.title == 'Test_Unit2' ||
                    it.title == 'Test_Integration2'
        }.size() == 3

        rar.get(riskAssesments.riskLow4).mitigationTests.findAll {
            it.title == 'Test_Integration4'
        }.size() == 1

        rar.get(riskAssesments.riskHigh1).mitigationTests.findAll {
            it.title == 'Test_Unit1' ||
                    it.title == 'Test_Installation1'
        }.size() == 2


        // -------------------------------------------------------------------------------------------------------------
        // STEP 33: Log in to Bitbucket.
        //          Link all created Test issues with the code.
        //          Result: Test issues are linked with the code.
        // -------------------------------------------------------------------------------------------------------------

        // -------------------------------------------------------------------------------------------------------------
        // STEP 34: Go back to Jira. Filter only Test issues. Move the status of Test Unit 1 to “In Progress”
        //          Move the status of the other test issues to “Done”.
        //          Result: All test issues except Test Unit 1 are in status “Done”.
        //                   Test Unit 1 is in status “In Progress”.
        // -------------------------------------------------------------------------------------------------------------
    }

    def createTest(test) {
        to IssuesPage
        navigationBar.createLink.click()
        waitFor {
            issueCreationDialog
        }
        issueCreationDialog.issueTypeSelectorModule.selectIssueOfType(IssueSelectorHelper.issueType.test)
        issueCreationDialog.testCreationFormModule.createIssue(test, this)

        waitFor { $('a.issue-created-key.issue-link') }

        test.key = $('a.issue-created-key.issue-link').getAttribute('data-issue-key')
        return test.key
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
        projectSummary = (title - ~/- Jira/).trim()

        then: "Login in the project is successful."
        at ProjectPage
        report('Step_1_login')

        // STEP 2 Click on “Create” and choose a Jira issue type Technical Specification.
        when: "Click on create"
        navigationBar.createLink.click()
        waitFor {
            issueCreationDialog
        }

        and: "Select to create a Technical Specification"
        issueCreationDialog.issueTypeSelectorModule.selectIssueOfType(IssueSelectorHelper.issueType.technicalSpecificationTask)

        then:
        report('Step 2 - Start Creating a Technical Specification')

        // STEP 3 Add all required information:
        //       Summary: TST1
        //       Description
        //       Systems Design
        //       Specification
        //       Software Design
        //       Specification
        // Click on “Create”.
        when: "We create the Technical Specification Task 1"
        issueCreationDialog.technicalSpecificationFormModule.createIssue(technicalSpecifications.tst1, this)

        then: "The Technical Specification has been created"
        waitFor { $('a.issue-created-key.issue-link') }

        when:
        technicalSpecifications.tst1.key = $('a.issue-created-key.issue-link').getAttribute('data-issue-key')
        println "technicalSpecifications.tst1.key $technicalSpecifications.tst1.key"

        then:
        assert technicalSpecifications.tst1.key != null
        report("TST1 Created")

        // STEP 4 Link the Story1 to the Technical Specification Task.
        when: "We add a link to the story 1"
        to IssueBrowsePage, technicalSpecifications.tst1.key

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
        waitFor {
            issueCreationDialog
        }

        and: "Select to create a Technical Specification"
        issueCreationDialog.issueTypeSelectorModule.selectIssueOfType(IssueSelectorHelper.issueType.technicalSpecificationTask)
        report('Step 5 - Start Creating a Technical Specification 2')

        and: "We create the Technical Specification Task 2"
        issueCreationDialog.technicalSpecificationFormModule.createIssue(technicalSpecifications.tst2, this)

        then:
        waitFor { $('a.issue-created-key.issue-link') }

        when:
        technicalSpecifications.tst2.key = $('a.issue-created-key.issue-link').getAttribute('data-issue-key')
        println "technicalSpecifications.tst2.key $technicalSpecifications.tst2.key"

        then: "The issue is created"
        assert technicalSpecifications.tst2.key != null

        when: "We add a link to the story 2"
        to IssueBrowsePage, technicalSpecifications.tst2.key
        addLinkToIssue(CreateLinkDialogModule.linkType.specifies, issues.story2.key)
        report("Added link to TST2")

        then: "The link exists"
        assert $("a", 'data-issue-key': issues.story2.key).size() == 1

        // Creation and link of TST4
        when: "Click on create"
        navigationBar.createLink.click()
        waitFor {
            issueCreationDialog
        }

        and: "Select to create a Technical Specification"
        issueCreationDialog.issueTypeSelectorModule.selectIssueOfType(IssueSelectorHelper.issueType.technicalSpecificationTask)
        report('Step 5 - Start Creating a Technical Specification 4')

        and: "We create the Technical Specification Task 4"
        issueCreationDialog.technicalSpecificationFormModule.createIssue(technicalSpecifications.tst4, this)
        then:
        waitFor { $('a.issue-created-key.issue-link') }

        when:
        technicalSpecifications.tst4.key = $('a.issue-created-key.issue-link').getAttribute('data-issue-key')
        println "technicalSpecifications.tst4.key $technicalSpecifications.tst4.key"

        then: "The issue is created"
        assert technicalSpecifications.tst2.key != null

        when: "We add a link to the story 4"
        to IssueBrowsePage, technicalSpecifications.tst4.key
        addLinkToIssue(CreateLinkDialogModule.linkType.specifies, issues.story4.key)
        report("Added link to TST4")

        then: "The link exists"
        assert $("a", 'data-issue-key': issues.story4.key).size() == 1

        when: 'Move to status Done'
        technicalSpecifications.each {
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

    // Helpers to make more understandable the tests.

    /**
     * Move a issue to status 'Done'
     */
    private void moveStoryToDone(key) {
        to IssueBrowsePage, key
        issueMenu.transitionButtonsConfirmDoR().click()
        sleep(1000)
        if ($('#issue-workflow-transition-submit')) {
            $('#issue-workflow-transition-submit').click()
        }
        issueMenu.transitionButtonsImplement().click()
        sleep(1000)
        if ($('#issue-workflow-transition-submit')) {
            $('#issue-workflow-transition-submit').click()
        }
        issueMenu.transitionButtonsIConfirmDoD().click()
        sleep(1000)
        if ($('#issue-workflow-transition-submit')) {
            $('#issue-workflow-transition-submit').click()
        }
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
        sleep(1000)
        if ($('#issue-workflow-transition-submit')) {
            $('#issue-workflow-transition-submit').click()
        }
        issueMenu.transitionButtonsImplement.click()
        sleep(1000)
        if ($('#issue-workflow-transition-submit')) {
            $('#issue-workflow-transition-submit').click()
        }
    }

    /**
     * Move a test to status 'Done'
     */
    private void moveTestToDone(key) {
        to IssueBrowsePage, key
        issueMenu.transitionButtonsConfirmDoR().click()
        sleep(1000)
        if ($('#issue-workflow-transition-submit')) {
            $('#issue-workflow-transition-submit').click()
        }
        issueMenu.transitionButtonsImplement().click()
        sleep(1000)
        if ($('#issue-workflow-transition-submit')) {
            $('#issue-workflow-transition-submit').click()
        }
        issueMenu.transitionButtonsIConfirmDoD().click()
        sleep(1000)
        if ($('#issue-workflow-transition-submit')) {
            $('#issue-workflow-transition-submit').click()
        }
    }

    /**
     * Move a risk assement to status 'Done'
     * @param key
     */
    private void moveRiskAssesmentToDone(key) {
        to IssueBrowsePage, key
        issueMenu.transitionButtonsApproveRiskAssesmet().click()
        sleep(1000)
        if ($('#issue-workflow-transition-submit')) {
            $('#issue-workflow-transition-submit').click()
        }
    }

}
