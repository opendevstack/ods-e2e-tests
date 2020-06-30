package org.ods.e2e.jira


import org.ods.e2e.jira.pages.ComponentPage
import org.ods.e2e.jira.pages.DashboardPage
import org.ods.e2e.jira.pages.ProjectPage
import spock.lang.Ignore


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
        to ProjectPage, projectName
        to ComponentPage, projectName

        then:
        existComponent(componentName)
        report()
    }


}
