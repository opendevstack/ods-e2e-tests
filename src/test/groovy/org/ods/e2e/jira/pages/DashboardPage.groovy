package org.ods.e2e.jira.pages

import geb.Page
import org.ods.e2e.jira.modules.LoginModule

class DashboardPage extends Page {
    static url = "/"
    static at = { browser.currentUrl.endsWith('Dashboard.jspa') }

    static content = {
        loginForm(wait: true) { module LoginModule }
    }
}