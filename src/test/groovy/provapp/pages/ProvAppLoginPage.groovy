package provapp.pages

import geb.Page
import provapp.modules.LoginModule

class ProvAppLoginPage extends Page {
    static url = "/"
    static at = { title == "PROJECT PROVISION" }

    static content = {
        loginLegend (required: true) { $("form[name=f] > fieldset > legend") }
        loginForm { module LoginModule }

    }
}
