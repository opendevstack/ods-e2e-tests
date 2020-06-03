package org.ods.e2e.bitbucket.pages

import geb.Page

class DashboardPage extends Page {
    static url = "/dashboard"

    static at = { browser.currentUrl.contains('dashboard') }
}
