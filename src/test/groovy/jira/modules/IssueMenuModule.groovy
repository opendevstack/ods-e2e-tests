package jira.modules

import geb.Module
import jira.JiraBaseSpec
import org.openqa.selenium.By

class IssueMenuModule  extends Module {

    def driver

    static content = {
        moreMenu { $("#opsbar-operations_more") }
        moreMenuCreateSubtask { $("#create-subtask > a") }
    }

    def clickCreateSubtask() {
        moreMenu.click()
        sleep(1000)
        def element = driver.findElement(By.cssSelector("#create-subtask > a"))
        JiraBaseSpec.js.executeScript("arguments[0].scrollIntoView();", element);
        moreMenuCreateSubtask.click()
        sleep(2000)
    }

}
