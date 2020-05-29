package org.ods.e2e.provapp.modules

import geb.Module

class ProjectModifyFormModule extends Module {

    static content = {
        modifyForm(required: true)  { $("#modifyProject") }
        projectSelectControl(required: true)  { $("#projects") }
        quickStarterTable(required: true) { $("#quickstartTable")}
        quickStarterAddGroup(required: true) { $(".form-group.quickstartergroup")}
        startProvisionButton(required: false) { $("#modifySubmit")}
    }

    def doSelectProject(id) {
        projectSelectControl = id
        sleep(5000)
    }

    def doAddQuickStarter(def quickstarterType, def componentId, def componentNumber) {
        def selector = $("select[name=quickstart-type-$componentNumber]" )
        selector.value(quickstarterType)
        def component = $("input[name=quickstart-comp-id-$componentNumber]" )
        component.value(componentId)
    }

    def doStartProvision(){
        startProvisionButton.click()
    }
}
