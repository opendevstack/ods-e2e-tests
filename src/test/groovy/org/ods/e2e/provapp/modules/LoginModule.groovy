package org.ods.e2e.provapp.modules

import geb.Module
import org.ods.e2e.util.SpecHelper

class LoginModule  extends Module {

    static Properties applicationProperties = new SpecHelper().getApplicationProperties()

    static content = {
        loginForm { $("form[name=f]") }
        loginFormUsernameInput { loginForm.$("input[name=username]") }
        loginFormPasswordInput { loginForm.$("input[name=password]") }
        loginFormSubmitButton  { loginForm.$("button[type=submit]") }
    }

    void fillLoginData() {
        loginFormUsernameInput.value(applicationProperties."config.provisioning-app.user.name")
        loginFormPasswordInput.value(applicationProperties."config.provisioning-app.user.password")
    }

    void doLogin() {
        loginFormSubmitButton.click()
    }
}
