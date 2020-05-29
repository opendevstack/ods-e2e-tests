package org.ods.e2e.provapp.modules

import geb.Module

class ProvisionOptionChooser  extends Module {

    static content = {
        formChooseOption(required: true) { $("#chooseOption") }
        optionModifyProject(required: true) { $("#optExistingInitiave") }
        optionCreateNewProject(required: true) { $("#optNewProject") }
    }

    def doSelectModifyProject() {
        optionModifyProject.click()
    }
    def doSelectCreateNewProject() {
        optionCreateNewProject.click()
    }
}
