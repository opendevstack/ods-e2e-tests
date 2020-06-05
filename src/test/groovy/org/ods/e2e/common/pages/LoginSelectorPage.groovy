package org.ods.e2e.common.pages

import geb.Page

class LoginSelectorPage extends Page {
    static url = "/"
    static content = {
        ldapLink(wait: true, required: true) { $("a", title: 'Log in with BI LDAP') }
    }
}
