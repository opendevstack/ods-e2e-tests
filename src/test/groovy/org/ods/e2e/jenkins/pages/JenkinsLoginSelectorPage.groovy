package org.ods.e2e.jenkins.pages

import org.ods.e2e.common.pages.LoginSelectorPage


class JenkinsLoginSelectorPage extends LoginSelectorPage{
    static at = { browser.currentUrl.contains('oauth/authorize') }
}
