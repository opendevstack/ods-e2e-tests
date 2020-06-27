package org.ods.e2e.jira.modules

import geb.Module
import org.openqa.selenium.By
import org.openqa.selenium.interactions.Actions

class IssueTypeSelectorModule extends Module {
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
        sleep(2000)
    }
}
