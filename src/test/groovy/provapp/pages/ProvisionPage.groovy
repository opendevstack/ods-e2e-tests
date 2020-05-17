package provapp.pages

import geb.Page
import provapp.modules.ProjectModifyFormModule
import provapp.modules.ProvisionOptionChooser

class ProvisionPage  extends Page {
    static url = "/provision"
    static at = { $("body > div.container > div.starter-template > h1").text() == 'Provision a project' }

    static content = {
        provisionOptionChooser { module ProvisionOptionChooser}
        projectModifyForm { module ProjectModifyFormModule }
    }
}
