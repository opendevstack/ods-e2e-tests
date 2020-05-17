package provapp.modules

import geb.Module

class LoginModule  extends Module {
    static content = {
        loginForm { $("form[name=f]") }
        loginFormUsernameInput { loginForm.$("input[name=username]") }
        loginFormPasswordInput { loginForm.$("input[name=password]") }
        loginFormSubmitButton  { loginForm.$("button[type=submit]") }
    }

    void fillLoginData() {
        loginFormUsernameInput.value(applicationProperties."config.user.name")
        loginFormPasswordInput.value(applicationProperties."config.user.password")
    }

    void doLogin() {
        loginFormSubmitButton.click()
    }
}
