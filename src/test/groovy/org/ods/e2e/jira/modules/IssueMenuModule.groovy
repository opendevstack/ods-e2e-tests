package org.ods.e2e.jira.modules

import geb.Module
import org.ods.e2e.jira.JiraBaseSpec
import org.ods.e2e.util.SpecHelper
import org.openqa.selenium.By

/**
 * Support Module for the More menu of the issues
 */
class IssueMenuModule extends Module {
    def driver
    def issue

    static content = {
        moreMenu(wait: true, required: true) { $("#opsbar-operations_more") }
        moreMenuCreateSubtask(wait: true, required: true) { $("#create-subtask > a") }
        moreMenuLink(wait: true, required: true) { $("#link-issue > a") }
        transitionButtons(wait: true, required: true) { $("#opsbar-opsbar-transitions") }
        transitionButtonsCancel(wait: true, required: true) { transitionButtons.$("#" + new SpecHelper().getTransitionId(issue, "cancel")) }
        transitionButtonsConfirmDoR(wait: true, required: true) { transitionButtons.$("#" + new SpecHelper().getTransitionId(issue, "confirm DoR")) }
        transitionButtonsImplement(wait: true, required: true) { transitionButtons.$("#" + new SpecHelper().getTransitionId(issue, "implement")) }
        transitionButtonsIConfirmDoD(wait: true, required: true) { transitionButtons.$("#" + new SpecHelper().getTransitionId(issue, "confirm DoD")) }
        transitionButtonTstImplement(wait: true, required: true) { transitionButtons.$("#" + new SpecHelper().getTransitionId(issue, "implement")) }
        transitionButtonsTstConfirmDoD(wait: true, required: true) { transitionButtons.$("#" + new SpecHelper().getTransitionId(issue, "confirm DoD")) }
        // Document chapter flow
        transitionButtonDocument(wait: true, required: true) { transitionButtons.$("#" + new SpecHelper().getTransitionId(issue, "document")) }
        transitionButtonDefine(wait: true, required: true) { transitionButtons.$("#" + new SpecHelper().getTransitionId(issue, "define")) }
        transitionButtonsReopenDocumentChapter(wait: 2, required: false) { transitionButtons.$("#" + new SpecHelper().getTransitionId(issue, "reopen")) }
    }

    /**
     * Select an item in the more Menu and perform the action
     * @param css - The selector of the menu Item
     * @param action - Action to perform
     */
    def selectMoreMenuItem(css, Closure action) {
        sleep(1000)
        moreMenu.click()
        sleep(1000)
        def element = driver.findElement(By.cssSelector(css))
        JiraBaseSpec.js.executeScript("arguments[0].scrollIntoView();", element)
        action()
        sleep(2000)
    }

    def clickCreateSubtask() {
        selectMoreMenuItem("#create-subtask > a") {
            moreMenuCreateSubtask.click()
        }
    }

    def clickLink() {
        selectMoreMenuItem("#link-issue > a") {
            moreMenuLink.click()
        }
    }
}
