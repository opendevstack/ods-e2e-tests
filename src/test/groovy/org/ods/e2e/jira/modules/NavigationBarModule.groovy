package org.ods.e2e.jira.modules

import geb.Module

class NavigationBarModule extends Module {
    static content = {
        createLink(wait: true) { $("#create_link") }
    }
}
