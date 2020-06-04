package org.ods.e2e.provapp.pages

import geb.Page
import org.ods.e2e.provapp.modules.LoginModule

class ProvAppLoginPage extends Page {
    static url = "/"
    static at = { title == "PROJECT PROVISION" }

    static content = {
        loginLegend (required: true) { $("form[name=f] > fieldset > legend") }
        loginForm { module LoginModule }

    }

    def doLoginProcess() {
        loginForm.fillLoginData()
        loginForm.doLogin()
    }
}
