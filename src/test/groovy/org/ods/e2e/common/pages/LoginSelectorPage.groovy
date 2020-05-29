package org.ods.e2e.common.pages

import geb.Page

class LoginSelectorPage extends Page {
    static url = "/"
    static content = {
        ldapLink { $("a", title: 'Log in with BI LDAP') }
    }
}
