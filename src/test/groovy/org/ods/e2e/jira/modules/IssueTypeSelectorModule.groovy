package org.ods.e2e.jira.modules

import geb.Module
import org.openqa.selenium.By
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.interactions.Actions

class IssueTypeSelectorModule extends Module {
    WebDriver driver


    static issueType = [
            releaseStatus             : 'release-status',
            technicalSpecificationTask: 'technical-specification-task',
            documentationChapter      : 'documentation-chapter',
            epic                      : 'epic',
            story                     : 'story',
            bug                       : 'bug',
            documentation             : 'documentation',
            test                      : 'test',
    ]

    static content = {
        projectSelect { $('#project-single-select') }
        issueTypeSelector(wait: true, required: true) { $('#issuetype-single-select > .icon') }
    }

    def selectIssueOfType(type) {
        def selector = '#summary'
        waitFor { $(selector) }
        WebElement element = driver.findElement(By.cssSelector(selector))
        element.sendKeys(Keys.UP)
        waitFor { $("#issuetype-single-select > .icon") }
        issueTypeSelector.click().click().click()
        selector = $("li.aui-list-item-li-$type")
        if (selector.size()) {
            Actions builder = new Actions(driver)
            builder.moveToElement(selector.firstElement()).perform()
            driver.findElement(By.cssSelector("li.aui-list-item-li-$type")).click()
        } else {
            issueTypeSelector.click()
        }
        sleep(2000)
    }
}
