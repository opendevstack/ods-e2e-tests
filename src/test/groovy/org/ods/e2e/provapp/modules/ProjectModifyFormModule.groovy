package org.ods.e2e.provapp.modules

import geb.Module

class ProjectModifyFormModule extends Module {

    static content = {
        modifyForm(required: true) { $("#modifyProject") }
        projectSelectControl(required: true) { $("#projects") }
        quickStarterTable(required: true) { $("#quickstartTable") }
        quickStarterAddGroup(required: true) { $(".form-group.quickstartergroup") }
        addQuickStarterButton(required: true) { $("button.btn-add") }
        startProvisionButton(required: false) { $("#modifySubmit") }
    }

    /**
     * Selects a project
     * @param id Project Id
     */
    def doSelectProject(id) {
        projectSelectControl = id
        sleep(5000)
    }

    /**
     * Adds a quick starter to the project without submitting the form
     * @param quickstarter Type The type of the quick starter
     * @param componentId The id of the component
     * @param componentNumber The number of the component to be added (if we are adding more than one)
     */
    def doAddQuickStarter(def quickstarterType, def componentId, def componentNumber = 1) {
        def selector = $("select[name=quickstart-type-$componentNumber]")
        selector.value(quickstarterType)
        def component = $("input[name=quickstart-comp-id-$componentNumber]")
        component.value(componentId)
    }

    /**
     * Start provisioning the quick starters
     */
    def doStartProvision() {
        startProvisionButton.click()
    }
}
