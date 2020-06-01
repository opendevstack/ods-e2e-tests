package org.ods.e2e.jira.modules

import geb.Module
import org.ods.e2e.jira.JiraBaseSpec
import org.openqa.selenium.By

class IssueMenuModule extends Module {
    def driver
    def cancelButtonId = 'action_id_131'
    def confirmDoRButtonId = 'action_id_111'
    def implementButtonId = 'action_id_91'
    def confirmDoDId = 'action_id_141'
    def documentButtonId = 'action_id_61'
    def defineButtonId = 'action_id_221'

    static content = {
        moreMenu(wait: true, required: true) { $("#opsbar-operations_more") }
        moreMenuCreateSubtask(wait: true, required: true) { $("#create-subtask > a") }
        transitionButtons(wait: true, required: true) { $("#opsbar-opsbar-transitions") }
        transitionButtonsCancel(wait: true, required: true) { transitionButtons.$("#$cancelButtonId") }
        transitionButtonsConfirmDoR(wait: true, required: true) { transitionButtons.$("#$confirmDoRButtonId") }
        transitionButtonsImplement(wait: true, required: true) { transitionButtons.$("#$implementButtonId") }
        transitionButtonsIConfirmDoD(wait: true, required: true) { transitionButtons.$("#$confirmDoDId") }
        transitionButtonDocument(wait:true, required:true) { transitionButtons.$("#$documentButtonId")}
        transitionButtonDefine(wait:true, required:true) { transitionButtons.$("#$defineButtonId")}

    }

    def clickCreateSubtask() {
        sleep(1000)
        moreMenu.click()
        sleep(1000)
        def element = driver.findElement(By.cssSelector("#create-subtask > a"))
        JiraBaseSpec.js.executeScript("arguments[0].scrollIntoView();", element)
        moreMenuCreateSubtask.click()
        sleep(2000)
    }

}
