package org.ods.e2e.jira.pages

import geb.Page
import org.ods.e2e.jira.modules.LoginModule
import org.ods.e2e.util.SpecHelper

class DashboardPage extends BasePage {
    static url = "/"
    static at = { browser.currentUrl.endsWith('Dashboard.jspa') }

    static content = {
        loginForm(wait: true) { module LoginModule }
    }
}