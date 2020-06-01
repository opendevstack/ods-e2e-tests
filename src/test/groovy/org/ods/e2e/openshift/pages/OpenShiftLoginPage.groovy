package org.ods.e2e.openshift.pages

import geb.Page
import org.ods.e2e.util.SpecHelper

class OpenShiftLoginPage extends Page {
    static Properties applicationProperties = new SpecHelper().getApplicationProperties()

    static url = "/login"
    static at = { title == 'Login - OpenShift Container Platform' }
    static content = {
        username { $("input", name: "username") }
        password { $("input", name: "password") }
        loginButton { $("button", type: "submit") }
    }

    def doLogin() {
        username.value(applicationProperties."config.openshift.user.name")
        password.value(applicationProperties."config.openshift.user.password")
        loginButton.click()
        sleep(3000)
    }
}
