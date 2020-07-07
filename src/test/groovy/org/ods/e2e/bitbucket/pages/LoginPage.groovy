package org.ods.e2e.bitbucket.pages

import geb.Page
import org.ods.e2e.util.SpecHelper

class LoginPage extends Page {

    static Properties applicationProperties = new SpecHelper().getApplicationProperties()

    static url = "/login?nosso"

    static content = {
        usernameInput(wait: true, required: true) { $("#j_username") }
        passwordInput(wait: true, required: true) { $("#j_password") }
        submitButton(wait: true, required: true) { $("#submit") }
    }

    def doLogin() {
        usernameInput = applicationProperties."config.atlassian.user.name"
        passwordInput = applicationProperties."config.atlassian.user.password"
        submitButton.click()
    }
}
