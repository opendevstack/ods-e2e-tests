package jenkins

import geb.spock.GebReportingSpec
import jenkins.pages.JenkinsLoginPage
import openshift.pages.OpenShiftLoginPage
import util.SpecHelper

class JenkinsSpec extends GebReportingSpec {

    static Properties applicationProperties = new SpecHelper().getApplicationProperties()

    def setup() {
        baseUrl = applicationProperties."config.jenkins.url"
    }

    def "can login to Jenkins"() {
        given: "Visit Jenkins login page"
        to JenkinsLoginPage

        when: "Click on login button"
        loginButton.click()
        then:
        at OpenShiftLoginPage

    }

}
