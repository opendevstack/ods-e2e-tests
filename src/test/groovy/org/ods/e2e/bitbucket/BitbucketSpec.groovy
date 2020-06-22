package org.ods.e2e.bitbucket

import geb.spock.GebReportingSpec
import org.ods.e2e.bitbucket.pages.DashboardPage
import org.ods.e2e.bitbucket.pages.LoginPage
import org.ods.e2e.bitbucket.pages.ProjectPage
import org.ods.e2e.bitbucket.pages.RepositoryPage
import org.ods.e2e.util.SpecHelper
import org.openqa.selenium.Dimension
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.Point
import spock.lang.Ignore

@Ignore
class BitbucketSpec extends GebReportingSpec {
    static Properties applicationProperties = new SpecHelper().getApplicationProperties()
    static JavascriptExecutor js

    String projectName
    String componentName

    def setup() {
        driver.manage().window().setSize(new Dimension(1600, 1024))
        driver.manage().window().setPosition(new Point(0, 0))

        js = (JavascriptExecutor) driver
        baseUrl = applicationProperties."config.atlassian.bitbucket.url"
        projectName = applicationProperties."config.project.key".toString().toUpperCase()
    }

    def "login in BitBucket"() {
        given: "We go to the Login Page"
        to LoginPage

        when: "we do login"
        doLogin()

        then: "we are at the Dashboard"
        at DashboardPage
        report('at dashboard page')

        when:
        "Visit project $projectName"
        to ProjectPage, projectName

        then: "We are in the project page"
        currentUrl.endsWith("projects/$projectName/")
        report('at project page')

        when: "We visit one repository"
        def repositories = getRepositoriesInfo()
        def repositoryName = repositories.first().name
        to RepositoryPage, projectName, repositoryName

        then: "we are at the repository page"
        currentUrl.endsWith("projects/$projectName/repos/$repositoryName/browse")
        report('at repository page')

        when: "we click in the commits link"
        commitsLink.click()

        then: "we are at the commit page"
        currentUrl.endsWith("projects/$projectName/repos/$repositoryName/commits")
        report('at repository commits page')
    }
}
