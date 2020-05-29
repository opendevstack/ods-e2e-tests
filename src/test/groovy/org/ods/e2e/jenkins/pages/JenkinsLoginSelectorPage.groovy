package org.ods.e2e.jenkins.pages

import org.ods.e2e.common.pages.LoginSelectorPage


class JenkinsLoginSelectorPage extends LoginSelectorPage{
    static at = {title == 'Login - OpenShift Container Platform'}
}
