package jira.modules

import geb.Module
import jira.pages.DashboardPage
import util.SpecHelper

class LoginModule  extends Module {

    static Properties applicationProperties = new SpecHelper().getApplicationProperties()


    static content = {
        loginForm { $("#loginform") }
        loginFormUsernameInput { $("#login-form-username") }
        loginFormPasswordInput { $("#login-form-password") }
        loginFormSubmitButton  { $("#login") }
    }

    void fillLoginData() {
        loginFormUsernameInput.value(applicationProperties."config.jira.user.name")
        loginFormPasswordInput.value(applicationProperties."config.jira.user.password")
    }

    void doLogin() {
        loginFormSubmitButton.click()
    }

    void doLoginProcess() {
        sleep(5000)
        fillLoginData()
        doLogin()
        sleep(5000)
    }
}
