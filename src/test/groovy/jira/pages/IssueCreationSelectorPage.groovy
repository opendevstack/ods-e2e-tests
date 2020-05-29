package jira.pages

import geb.Page
import org.openqa.selenium.By
import org.openqa.selenium.interactions.Actions

class IssueCreationSelectorPage extends Page {

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

    static at = { browser.currentUrl.contains('CreateIssue!default.jspa') }

    static content = {
        issueTypeSelector { $("#issuetype-single-select > .icon") }
        nextButton { $("#issue-create-submit") }
    }

    def selectIssueOfType(type) {
        waitFor { $("#issuetype-single-select > .icon") }
        issueTypeSelector.click()
        def selector = $("li.aui-list-item-li-$type")
        if (selector.size()) {
            Actions builder = new Actions(driver);
            builder.moveToElement(selector.firstElement()).perform()
            driver.findElement(By.cssSelector("li.aui-list-item-li-$type")).click()
        } else {
            issueTypeSelector.click()
        }
    }

}
