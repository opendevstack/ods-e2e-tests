package org.ods.e2e.util

import geb.spock.GebReportingSpec
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

    def setup() {
        driver.manage().window().setSize(new Dimension(1600, 1024))
        driver.manage().window().setPosition(new Point(0, 0))

        js = (JavascriptExecutor) driver

        baseUrlProvisioningApp = applicationProperties."config.provisioning.url"
        baseUrlJira = applicationProperties."config.atlassian.jira.url"
        baseUrlBitbucket = applicationProperties."config.atlassian.bitbucket.url"
        baseUrlJenkins = applicationProperties."config.jenkins.url"
        baseUrlOpenshift = applicationProperties."config.openshift.url"
    }

    def getApplicationProperties() {
        return applicationProperties
    }
}
