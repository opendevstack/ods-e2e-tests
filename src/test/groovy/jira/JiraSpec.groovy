package jira

import geb.spock.GebReportingSpec
import jira.pages.ComponentPage
import jira.pages.ProjectPage
import util.SpecHelper
import jira.pages.DashboardPage

class JiraSpec extends GebReportingSpec {

    static Properties applicationProperties = new SpecHelper().getApplicationProperties()
    String projectName

    def setup() {
        baseUrl = applicationProperties."config.jira.url"
        projectName = 'SCRUM'
    }


    def "can login to Jira"() {

        given: "Visit the dashboard Page"
        to DashboardPage
        sleep(5000)
        
        expect: "We are in the dashboard Page"
        at DashboardPage
        assert loginForm

        when: "Fills login data"
        loginForm.fillLoginData()

        and: "Do login"
        loginForm.doLogin()

        then: "We are logged in"
        assert $("#header-details-user-fullname")
        report()
    }

    def "project pages exists"() {

        given: "Visit the dashboard Page"
        to DashboardPage
        sleep(5000)

        expect: "We are in the dashboard Page"
        at DashboardPage
        assert loginForm

        when: "Fills login data"
        loginForm.fillLoginData()

        and: "Do login"
        loginForm.doLogin()

        then: "We are logged in"
        assert $("#header-details-user-fullname")

        when: "visit project page"
        to ProjectPage, projectName

        then: "We are in the project page"
        waitFor {title.toUpperCase ().startsWith(projectName.toString().toUpperCase())}
        report()

        and:
        sleep(5000)
        expect:
        true
    }

    def "check components"() {

        given: "Visit the dashboard Page"
        to DashboardPage
        sleep(5000)

        expect: "We are in the dashboard Page"
        at DashboardPage
        assert loginForm

        when: "Fills login data"
        loginForm.fillLoginData()

        and: "Do login"
        loginForm.doLogin()

        then: "We are logged in"
        assert $("#header-details-user-fullname")

        when: "visit project page"
        to ComponentPage, projectName

        then:
        $(".aui-page-header-fixed > div:nth-child(1) > h1:nth-child(1)").text() == 'Components'
        report()
    }


}
