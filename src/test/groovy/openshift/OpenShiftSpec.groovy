package openshift

import geb.Browser
import geb.Page
import geb.spock.GebReportingSpec
import openshift.pages.OpenShiftLoginPage
import openshift.pages.LoginSelectorPage
import openshift.pages.ConsoleCatalogPage
import openshift.pages.ConsoleProjectsPage
import util.SpecHelper

class OpenShiftSpec extends GebReportingSpec{
    static Properties applicationProperties = new SpecHelper().getApplicationProperties()
    String projectName

    def setup() {
        baseUrl = applicationProperties."config.openshift.url"
        projectName = 'VTATL1'
    }

    def "can login to OpenShift"() {
        when: "Visit openshift login page"
        to LoginSelectorPage

        then: "Display the page to select the way we login"
        waitFor { title == 'Login - OpenShift Container Platform' }

        when: "Select login via ldap"
        ldapLink.click()

        then: "We are in the openshift login page"
        at OpenShiftLoginPage

        when: "We introduce the credentials and do the login"
        doLogin()

        then: "We are in the console page"
        at ConsoleCatalogPage
        report()
    }

    def "Check existing projects"() {
        when: "Visit openshift login page"
        to LoginSelectorPage

        then: "Display the page to select the way we login"
        waitFor { title == 'Login - OpenShift Container Platform' }
        report()

        when: "Select login via ldap"
        ldapLink.click()

        then: "We are in the openshift login page"
        at OpenShiftLoginPage
        report()

        when: "We introduce the credentials and do the login"
        doLogin()

        then: "We are in the console page"
        at ConsoleCatalogPage
        report()

        when: "Visit all project page"
        to ConsoleProjectsPage
        waitFor {projectList}
        report()

        then:
        sleep(5000)
        def projects =  findProjects(projectName)
        assert projects
        assert projects.contains(projectName.toLowerCase()+ '-cd')
        assert projects.contains(projectName.toLowerCase()+ '-dev')
        assert projects.contains(projectName.toLowerCase()+ '-test')
    }
}
