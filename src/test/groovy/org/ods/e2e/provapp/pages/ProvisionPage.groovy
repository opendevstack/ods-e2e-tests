package org.ods.e2e.provapp.pages

import geb.Page
import org.ods.e2e.provapp.modules.ProjectModifyFormModule
import org.ods.e2e.provapp.modules.ProjectCreateFormModule
import org.ods.e2e.provapp.modules.ProvisionOptionChooser

class ProvisionPage  extends Page {

    static url = "/provision"
    static at = { $("body > div.container > div.starter-template > h1").text() == 'Provision a project' }

    static content = {
        provisionOptionChooser { module ProvisionOptionChooser}
        projectModifyForm { module ProjectModifyFormModule }
        projectCreateForm(required: false) { module ProjectCreateFormModule }
    }
}
