package org.ods.e2e.jenkins

import geb.spock.GebReportingSpec
import org.ods.e2e.jenkins.pages.JenkinsConsolePage
import org.ods.e2e.jenkins.pages.JenkinsLoginPage
import org.ods.e2e.jenkins.pages.JenkinsLoginSelectorPage
import org.ods.e2e.openshift.pages.OpenShiftLoginPage
import org.ods.e2e.util.BaseSpec
import org.ods.e2e.util.SpecHelper
import spock.lang.Ignore

class JenkinsSpec extends BaseSpec {

    static Properties applicationProperties = new SpecHelper().getApplicationProperties()
    def projectName
    def componentName

    def setup() {
        projectName = applicationProperties."config.project.key"
        baseUrl = getJenkinsBaseUrl(projectName)
        componentName = 'golang-component'
    }

    def "can login to Jenkins"() {
        when: "Visit Jenkins login page"
        doJenkinsLoginProcess()

        then:
        at JenkinsConsolePage
        report()
    }

    def "checking project folder existence"() {
        given: "The user is logged in Jenkins"
        doJenkinsLoginProcess()
        expect: "The project folder exists"
        assert $("#job_$projectName-cd")
    }

    def "checking component creation job existence"() {
        given: "The user is logged in Jenkins"
        doJenkinsLoginProcess()

        when: "The user visit the jobs folder"
        $("#job_$projectName-cd > td:nth-child(3) > a").click()

        then: "The component creation jobs exists"
        assert getComponentJobs(projectName, componentName).findAll {
            it -> it.value.odsStartupComponentJob
        }
    }

    def "checking component build job master branch finalize successfully"() {
        given: "The user is logged in Jenkins"
        doJenkinsLoginProcess()

        when: "The user visit the jobs folder"
        $("#job_$projectName-cd > td:nth-child(3) > a").click()

        then: "The component creation jobs exists"
        assert getComponentJobs(projectName, componentName).findAll {
            it -> it.value.branch == 'master' && (it.value.success || it.value.notBuild)
        }
    }
}
