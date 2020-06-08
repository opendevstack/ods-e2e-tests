package org.ods.e2e.provapp.modules

import geb.Module

class ProjectCreateFormModule extends Module {

    static projectTypes = [default: 'default',
                           kanban : 'kanban',
                           edp    : 'EDP-project-template']
    static content = {
        createForm(wait:true, required: true) { $("#createProject") }
        projectName(wait:true, required: true) { $("#projectName") }
        projectKey(wait:true, required: true) { $("#projectKey") }
        projectDescription(wait:true, required: true) { createForm.$("#description") }
        projectType(wait:true, required: true) { $("#projectType") }
        projectSpecificCdUserCheckbox(wait:true, required: true) { $("#projectSpecificCdUser") }
        projectSpecificCdUser(wait:true, required: true) { $("#cdUser") }
        projectSpecialPermissionSetCheckbox(wait:true, required: true) { $("#specialPermissionSet") }
        projectAdminUser(wait:true, required: true) { $("#projectAdminUser") }
        projectAdminGroup(wait:true, required: true) { $("#projectAdminGroup") }
        projectReadonlyGroup(wait:true, required: true) { $("#projectReadonlyGroup") }
        bugtrackerSpace(wait:true, required: true) { $("#bugtrackerSpace") }
        platformRuntime(wait:true, required: true) { $("#platformRuntime") }

        startCreationButton(wait: true) { createForm.$("#createProject > fieldset > button") }
    }

    def doSelectProject(id) {
        projectSelectControl = id
        sleep(5000)
    }

    def doChooseProjectType(def projectType) {
        projectType.value(projectType)
    }

    def doStartProvision() {
        startCreationButton.click()
    }
}
