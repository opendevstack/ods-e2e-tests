package jenkins.pages

import common.pages.LoginSelectorPage

class JenkinsLoginSelectorPage extends LoginSelectorPage{
    static at = {title == 'Login - OpenShift Container Platform'}
}
