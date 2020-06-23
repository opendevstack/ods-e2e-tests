package org.ods.e2e.jira.pages

import geb.Page
import org.ods.e2e.jira.modules.LoginModule
import org.ods.e2e.util.SpecHelper

class DashboardPage extends BasePage {
    static url = "/"
    static at = { title.startsWith("System Dashboard") }

    static content = {
        loginForm(wait: true) { module LoginModule }
    }
}