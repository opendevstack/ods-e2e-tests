package jira

import jira.pages.DashboardPage
import jira.pages.IssuesPage
import jira.pages.ProjectPage
import org.openqa.selenium.By

class JiraReleaseManagerSpec extends JiraBaseSpec {

    // TEST CASES TEST GROUP 02
    // CHECK THE CORRECTNESS OF CALCULATION – RISK ASSESSMENT WITHOUT PROBABILITY OF OCCURRENCE
    def "RT_02_001"() {
        def story1

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
        $("li", title: "Story 1")
        sleep(1000)

        then: "Story 1 exists"
        report()

        when: "Modify the Story 1"
        issueMenu.moreMenu.click()
        sleep(1000)
        def element = driver.findElement(By.cssSelector ("#create-subtask > a"))
        js.executeScript("arguments[0].scrollIntoView();", element);
        issueMenu.moreMenuCreateSubtask.click()
        sleep(2000)
        report()

        then:
        true


        //when: "Therefore open Story1 → select"
//
        //and: "under 'More' the button Create subtask"


    }
}
