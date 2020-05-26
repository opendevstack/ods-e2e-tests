package jira


import jira.pages.ComponentPage
import jira.pages.DashboardPage
import jira.pages.ProjectPage

class JiraSpec extends JiraBaseSpec {

    def "can login to Jira"() {
        given: "Visit the dashboard Page"
        to DashboardPage

        and: "Wait page to load"
        sleep(5000)

        and: "Fills login data"
        loginForm.fillLoginData()
        report()

        when: "Do login"
        loginForm.doLogin()
        report()

        and: "Wait for login process to finish"
        waitFor { $("#header-details-user-fullname") }

        then: "We are logged in"
        at DashboardPage
        report()
    }

    def "project pages exists"() {
        given:
        to DashboardPage
        loginForm.doLoginProcess()

        expect:
        at DashboardPage

        when: "visit project page"
        to ProjectPage, projectName

        then: "We are in the project page"
        at ProjectPage
        report()
    }

    def "check components"() {
        given:
        to DashboardPage
        loginForm.doLoginProcess()

        expect:
        at DashboardPage

        when: "visit project page"
        to ComponentPage, projectName

        then:
        $(".aui-page-header-fixed > div:nth-child(1) > h1:nth-child(1)").text() == 'Components'
        existComponent(componentName)
        report()
    }


}
