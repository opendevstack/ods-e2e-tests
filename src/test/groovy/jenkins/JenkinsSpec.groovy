package jenkins

import geb.spock.GebReportingSpec
import jenkins.pages.JenkinsConsolePage
import jenkins.pages.JenkinsLoginPage
import jenkins.pages.JenkinsLoginSelectorPage
import openshift.pages.OpenShiftLoginPage
import util.SpecHelper

class JenkinsSpec extends GebReportingSpec {

    static Properties applicationProperties = new SpecHelper().getApplicationProperties()
    def projectName
    def componentName

    def setup() {
        projectName = 'vtatl1'
        baseUrl = applicationProperties."config.jenkins.url"
    }

    def "can login to Jenkins"() {
        when: "Visit Jenkins login page"
        to JenkinsLoginPage

        then:
        at JenkinsLoginPage
        report()

        when:
        loginButton.click()

        then:
        at JenkinsLoginSelectorPage

        when: "Select login via ldap"
        report()
        ldapLink.click()

        then: "We are in the openshift login page"
        at OpenShiftLoginPage
        report()

        when: "We introduce the credentials and do the login"
        doLogin()
        then: "get to the jenkins instance of the project"
        at JenkinsConsolePage
        report()
    }

    def "doing somthing else"() {
        given:
        doLoginProcess()
        expect:
        $("#job_" + projectName + "-cd")
    }

    def doLoginProcess() {
        to JenkinsLoginPage
        at JenkinsLoginPage
        loginButton.click()
        at JenkinsLoginSelectorPage
        ldapLink.click()
        at OpenShiftLoginPage
        doLogin()
        at JenkinsConsolePage
        report()
    }

}
