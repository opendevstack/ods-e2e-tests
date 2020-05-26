package jira

import geb.spock.GebReportingSpec
import org.openqa.selenium.Dimension
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Point
import util.SpecHelper

class JiraBaseSpec extends GebReportingSpec {

    static Properties applicationProperties = new SpecHelper().getApplicationProperties()
    static JavascriptExecutor js

    String projectName
    String componentName
    def setup() {
        driver.manage().window().setSize(new Dimension(1600,1024))
        driver.manage().window().setPosition(new Point(0,0))

        js = (JavascriptExecutor) driver
        baseUrl = applicationProperties."config.jira.url"
        projectName = applicationProperties."config.project.key"
        componentName = 'demo-app-front-end'
    }
}
