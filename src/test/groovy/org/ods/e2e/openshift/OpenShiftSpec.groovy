package org.ods.e2e.openshift

import geb.spock.GebReportingSpec
import org.ods.e2e.openshift.pages.ConsoleCatalogPage
import org.ods.e2e.openshift.pages.ConsoleProjectsPage
import org.ods.e2e.openshift.pages.OpenShiftLoginPage
import org.ods.e2e.openshift.pages.OpenShiftLoginSelectorPage
import org.ods.e2e.util.BaseSpec
import org.ods.e2e.util.SpecHelper
import spock.lang.Ignore

class OpenShiftSpec extends BaseSpec {
    static Properties applicationProperties = new SpecHelper().getApplicationProperties()
    String projectName

    def setup() {
        baseUrl = applicationProperties."config.openshift.url"
        projectName = applicationProperties."config.project.key"
    }

    def "can login to OpenShift"() {
        when: "Visit openshift login page"
        doOpenshiftLoginProcess()

        then: "We are in the console page"
        at ConsoleCatalogPage
        report()
    }

    @Ignore
    def "Check existing projects"() {
        when: "Visit openshift login page"
        doOpenshiftLoginProcess()

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

    def cleanup(){
        browser.clearCookies()
        browser.clearWebStorage()
    }
}
