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
    def provisioningAppProject
    def provisioningAppDeployCfg
    def baseUrlJira
    def baseUrlBitbucket
    def baseBranchBitbucket
    def baseUrlJenkins
    def baseUrlOpenshift
    def openshiftPublichost
    def simulate
    def extraLoginPage
    def quickstartersConfigMap

    def setup() {
        driver.manage().window().setSize(new Dimension(1600, 1024))
        driver.manage().window().setPosition(new Point(0, 0))

        js = (JavascriptExecutor) driver
        openshiftPublichost = removeLastSlash(applicationProperties."config.openshift.publichost")
        baseUrlProvisioningApp = removeLastSlash(applicationProperties."config.provisioning.url")
        provisioningAppDeployCfg = applicationProperties."config.provisioning-app.deployCfg"
        provisioningAppProject = applicationProperties."config.provisioning-app.project"
        baseUrlJira = removeLastSlash(applicationProperties."config.atlassian.jira.url")
        baseUrlBitbucket = removeLastSlash(applicationProperties."config.atlassian.bitbucket.url")
        baseBranchBitbucket = applicationProperties."config.atlassian.bitbucket.branch"
        if (!baseBranchBitbucket) {
            baseBranchBitbucket = null
        }
        baseUrlJenkins = removeLastSlash(applicationProperties."config.jenkins.url")
        baseUrlOpenshift = removeLastSlash(applicationProperties."config.openshift.url")
        simulate = applicationProperties."config.simulate".toUpperCase() == 'TRUE'

        baseUrl = baseUrlProvisioningApp

        extraLoginPage = System.getenv("OCP_LOGIN_SELECTOR_PAGE")?.toUpperCase() == 'TRUE' ?
                true : false
        quickstartersConfigMap = applicationProperties."config.openshift.quickstarters.configMap"
        if (!quickstartersConfigMap) {
            quickstartersConfigMap = 'quickstarters.properties'
        }
    }

    def removeLastSlash(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == '/') {
            return str.substring(0, str.length() - 1)
        }
        return str
    }

    def getApplicationProperties() {
        return applicationProperties
    }

    /**
     * Gets the jenkins url based in the project name
     * @param project
     * @return
     */
    def getJenkinsBaseUrl(project) {
        return "https://jenkins-$project-cd.$openshiftPublichost".toLowerCase()
    }

    /**
     * Login in Openshift
     */
    def doOpenshiftLoginProcess() {
        if (extraLoginPage) {
            to OpenShiftLoginSelectorPage
            waitFor {
                ldapLink
            }
            ldapLink.click()
        } else {
            via OpenShiftLoginPage
            sleep(1000)
        }
        at OpenShiftLoginPage
        doLogin()

        at ConsoleCatalogPage
    }

    /**
     * The login in Jenkins has an special behavior as we have to go across openshift.
     */
    def doJenkinsLoginProcess() {
        via JenkinsLoginPage
        loginButton.click()
        if (extraLoginPage) {
            at(new JenkinsLoginSelectorPage())
            ldapLink.click()
        }
        at OpenShiftLoginPage
        doLogin()
        if ($('input', name: 'approve')) {
            $('input', name: 'approve').click()
        }
        at JenkinsConsolePage
    }

    /**
     * Cleanup cookies
     */
    def cleanupAllCookies(project) {
        def urls = [baseUrlProvisioningApp,
                    baseUrlJira,
                    baseUrlBitbucket,
                    getJenkinsBaseUrl(project),
                    baseUrlOpenshift] as String[]
        println "Cleaning up urls: $urls"
        browser.clearCookies(urls)
    }
}
