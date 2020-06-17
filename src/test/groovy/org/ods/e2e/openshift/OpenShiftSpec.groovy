package org.ods.e2e.openshift

import geb.spock.GebReportingSpec
import org.ods.e2e.openshift.pages.ConsoleCatalogPage
import org.ods.e2e.openshift.pages.ConsoleProjectsPage
import org.ods.e2e.openshift.pages.OpenShiftLoginPage
import org.ods.e2e.openshift.pages.OpenShiftLoginSelectorPage
import org.ods.e2e.util.SpecHelper
import spock.lang.Ignore

@Ignore
class OpenShiftSpec extends GebReportingSpec {
    static Properties applicationProperties = new SpecHelper().getApplicationProperties()
    String projectName

    def setup() {
        baseUrl = applicationProperties."config.openshift.url"
        projectName = 'VTATL1'
    }

    def "can login to OpenShift"() {
        when: "Visit org.ods.e2e.openshift login page"
        to OpenShiftLoginSelectorPage

        then: "Display the page to select the way we login"
        waitFor { title == 'Login - OpenShift Container Platform' }

        when: "Select login via ldap"
        ldapLink.click()

        then: "We are in the org.ods.e2e.openshift login page"
        at OpenShiftLoginPage

        when: "We introduce the credentials and do the login"
        doLogin()

        then: "We are in the console page"
        at ConsoleCatalogPage
        report()
    }

    def "Check existing projects"() {
        when: "Visit org.ods.e2e.openshift login page"
        to OpenShiftLoginSelectorPage

        then: "Display the page to select the way we login"
        waitFor { title == 'Login - OpenShift Container Platform' }
        report()

        when: "Select login via ldap"
        ldapLink.click()

        then: "We are in the org.ods.e2e.openshift login page"
        at OpenShiftLoginPage
        report()

        when: "We introduce the credentials and do the login"
        doLogin()

        then: "We are in the console page"
        at ConsoleCatalogPage
        report()

        when: "Visit all project page"
        to ConsoleProjectsPage
        waitFor { projectList }
        report()

        then:
        sleep(5000)
        def projects = findProjects(projectName)
        assert projects
        assert projects.contains(projectName.toLowerCase() + '-cd')
        assert projects.contains(projectName.toLowerCase() + '-dev')
        assert projects.contains(projectName.toLowerCase() + '-test')
    }
}
