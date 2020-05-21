package jira.modules

import geb.Module
import org.openqa.selenium.By

class IssueMenuModule  extends Module {

    static content = {
        moreMenu { $("#opsbar-operations_more") }
        moreMenuCreateSubtask { $("#create-subtask > a") }
    }
}
