package jira

import geb.spock.GebReportingSpec
import org.openqa.selenium.JavascriptExecutor
import util.SpecHelper

class JiraBaseSpec extends GebReportingSpec {

    static Properties applicationProperties = new SpecHelper().getApplicationProperties()
    String projectName
    String componentName
    JavascriptExecutor js
    def setup() {
        js = (JavascriptExecutor) driver
        baseUrl = applicationProperties."config.jira.url"
        projectName = applicationProperties."config.project.key"
        componentName = 'demo-app-front-end'
    }
}
