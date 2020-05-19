package openshift.pages

import geb.Page

class LoginSelectorPage extends Page {
    static url = "/"
    //static at = { title == "Login - OpenShift Contaner Platform"}
    static content = {
        ldapLink { $("a", title: "Log in with BI LDAP") }
    }
}
