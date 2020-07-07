package org.ods.e2e.openshift.pages

import org.ods.e2e.common.pages.LoginSelectorPage

class OpenShiftLoginSelectorPage extends LoginSelectorPage {
    static at = { title == 'OpenShift Web Console' }

}
