package jira.modules

import geb.Module
import jira.JiraBaseSpec
import org.openqa.selenium.By

class IssueMenuModule  extends Module {

    def driver

    static content = {
        moreMenu(wait:true, required: true) { $("#opsbar-operations_more") }
        moreMenuCreateSubtask(wait:true, required: true) { $("#create-subtask > a") }
    }

    def clickCreateSubtask() {
        sleep(1000)
        moreMenu.click()
        sleep(1000)
        def element = driver.findElement(By.cssSelector("#create-subtask > a"))
        JiraBaseSpec.js.executeScript("arguments[0].scrollIntoView();", element);
        moreMenuCreateSubtask.click()
        sleep(2000)
    }

}
