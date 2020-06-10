package org.ods.e2e.util

import geb.spock.GebReportingSpec
import org.ods.e2e.jenkins.pages.JenkinsConsolePage
import org.ods.e2e.jenkins.pages.JenkinsLoginPage
import org.ods.e2e.jenkins.pages.JenkinsLoginSelectorPage
import org.ods.e2e.openshift.pages.ConsoleCatalogPage
import org.ods.e2e.openshift.pages.OpenShiftLoginPage
import org.ods.e2e.openshift.pages.OpenShiftLoginSelectorPage
import org.openqa.selenium.Dimension
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Point

class BaseSpec extends GebReportingSpec {
    static Properties applicationProperties = new SpecHelper().getApplicationProperties()
    static JavascriptExecutor js

    def baseUrlProvisioningApp
    def baseUrlJira
    def baseUrlBitbucket
    def baseUrlJenkins
    def baseUrlOpenshift
    def openshiftPublichost

    def setup() {
        driver.manage().window().setSize(new Dimension(1600, 1024))
        driver.manage().window().setPosition(new Point(0, 0))

        js = (JavascriptExecutor) driver
        openshiftPublichost = applicationProperties."config.openshift.publichost"
        baseUrlProvisioningApp = applicationProperties."config.provisioning.url"
        baseUrlJira = applicationProperties."config.atlassian.jira.url"
        baseUrlBitbucket = applicationProperties."config.atlassian.bitbucket.url"
        baseUrlJenkins = applicationProperties."config.jenkins.url"
        baseUrlOpenshift = applicationProperties."config.openshift.url"
    }

    def getApplicationProperties() {
        return applicationProperties
    }

    /**
     * Gets the jenkins url based in the project name
     * @param project
     * @return
     */
    def getJenkinsBaseUrl(project){
        return "https://jenkins-${project.toLowerCase()}-cd.$openshiftPublichost"
    }

    /**
     * Login in Openshift
     */
    def doOpenshiftLoginProcess() {
        to OpenShiftLoginSelectorPage
        waitFor {
            title == 'Login - OpenShift Container Platform'
            ldapLink
        }
        ldapLink.click()

        at OpenShiftLoginPage
        doLogin()

        at ConsoleCatalogPage
    }

    /**
     * The login in Jenkins has an special behavior as we have to go across openshift.
     */
    def doJenkinsLoginProcess() {
        to JenkinsLoginPage
        loginButton.click()

        at(new JenkinsLoginSelectorPage())
        ldapLink.click()

        at OpenShiftLoginPage
        doLogin()

        at JenkinsConsolePage
    }
}
