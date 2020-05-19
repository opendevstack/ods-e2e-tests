package jira

import geb.spock.GebReportingSpec
import jira.pages.ComponentPage
import jira.pages.ProjectPage
import proxy.ProxyPage
import util.SpecHelper
import jira.pages.DashboardPage

class JiraSpec extends GebReportingSpec {

    static Properties applicationProperties = new SpecHelper().getApplicationProperties()
    String projectName
    String componentName
    def setup() {
        baseUrl = applicationProperties."config.jira.url"
        projectName = 'VTATL1'
        componentName = 'demo-app-front-end'

    }


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
        waitFor {title.toUpperCase ().startsWith(projectName.toString().toUpperCase())}
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
