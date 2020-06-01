package org.ods.e2e.jenkins

import geb.spock.GebReportingSpec
import org.ods.e2e.jenkins.pages.JenkinsConsolePage
import org.ods.e2e.jenkins.pages.JenkinsLoginPage
import org.ods.e2e.jenkins.pages.JenkinsLoginSelectorPage
import org.ods.e2e.openshift.pages.OpenShiftLoginPage
import org.ods.e2e.util.SpecHelper

class JenkinsSpec extends GebReportingSpec {

    static Properties applicationProperties = new SpecHelper().getApplicationProperties()
    def projectName
    def componentName

    def setup() {
        baseUrl = applicationProperties."config.jenkins.url"
        projectName = applicationProperties."config.project.key"
        componentName = 'demo-app-user'
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

        then: "We are in the org.ods.e2e.openshift login page"
        at OpenShiftLoginPage
        report()

        when: "We introduce the credentials and do the login"
        doLogin()
        then: "get to the org.ods.e2e.jenkins instance of the project"
        at JenkinsConsolePage
        report()
    }

    def "checking project folder existence"() {
        given: "The user is logged in Jenkins"
        doLoginProcess()
        expect: "The project folder exists"
        assert $("#job_$projectName-cd")
    }

    def "checking component creation job existence"() {
        given: "The user is logged in Jenkins"
        doLoginProcess()

        when: "The user visit the jobs folder"
        $("#job_$projectName-cd > td:nth-child(3) > a").click()

        then: "The component creation jobs exists"
        assert getComponentJobs(projectName, componentName).findAll {
            it -> it.value.odsStartupComponentJob
        }
    }

    def "checking component build job master branch finalize successfully"() {
        given: "The user is logged in Jenkins"
        doLoginProcess()

        when: "The user visit the jobs folder"
        $("#job_$projectName-cd > td:nth-child(3) > a").click()

        then: "The component creation jobs exists"
        assert getComponentJobs(projectName, componentName).findAll {
            it -> it.value.branch == 'master' && (it.value.success|| it.value.notBuild)
        }
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
