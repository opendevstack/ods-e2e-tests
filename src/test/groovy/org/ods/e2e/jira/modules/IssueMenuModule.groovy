package org.ods.e2e.jira.modules

import geb.Module
import org.ods.e2e.jira.JiraBaseSpec
import org.openqa.selenium.By

/**
 * Support Module for the More menu of the issues
 */
class IssueMenuModule extends Module {
    def driver
    def cancelButtonId = 'action_id_131'
    def confirmDoRButtonId = 'action_id_111'
    def implementButtonId = 'action_id_91'
    def confirmDoDId = 'action_id_141'
    def documentButtonId = 'action_id_61'
    def defineButtonId = 'action_id_221'
    def tstImplementButtonId = 'action_id_121'
    def tstConformDoDId = 'action_id_111'
    def reopenButtonDocumentChapterId = 'action_id_211'

    static content = {
        moreMenu(wait: true, required: true) { $("#opsbar-operations_more") }
        moreMenuCreateSubtask(wait: true, required: true) { $("#create-subtask > a") }
        moreMenuLink(wait: true, required: true) { $("#link-issue > a") }
        transitionButtons(wait: true, required: true) { $("#opsbar-opsbar-transitions") }
        transitionButtonsCancel(wait: true, required: true) { transitionButtons.$("#$cancelButtonId") }
        transitionButtonsConfirmDoR(wait: true, required: true) { transitionButtons.$("#$confirmDoRButtonId") }
        transitionButtonsImplement(wait: true, required: true) { transitionButtons.$("#$implementButtonId") }
        transitionButtonsIConfirmDoD(wait: true, required: true) { transitionButtons.$("#$confirmDoDId") }
        transitionButtonDocument(wait: true, required: true) { transitionButtons.$("#$documentButtonId") }
        transitionButtonDefine(wait: true, required: true) { transitionButtons.$("#$defineButtonId") }
        transitionButtonTstImplement(wait: true, required: true) { transitionButtons.$("#$tstImplementButtonId") }
        transitionButtonsTstConfirmDoD(wait: true, required: true) { transitionButtons.$("#$tstConformDoDId") }
        transitionButtonsReopenDocumentChapter(wait: true, required: true) { transitionButtons.$("#$reopenButtonDocumentChapterId") }
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
