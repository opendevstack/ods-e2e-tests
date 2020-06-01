package org.ods.e2e.provapp.modules

import geb.Module

class ProjectCreateFormModule extends Module {

    static projectTypes = [default: 'default',
                           kanban : 'kanban',
                           edp    : 'EDP-project-template']
    static content = {
        createForm(required: true) { $("#createProject") }
        projectName(required: true) { $("#projectName") }
        projectKey(required: true) { $("#projectKey") }
        projectDescription(required: true) { createForm.$("#description") }
        projectType(required: true) { $("#projectType") }
        projectSpecificCdUserCheckbox(required: true) { $("#projectSpecificCdUser") }
        projectSpecificCdUser(required: true) { $("#cdUser") }
        projectSpecialPermissionSetCheckbox(required: true) { $("#specialPermissionSet") }
        projectAdminUser(required: true) { $("#projectAdminUser") }
        projectAdminGroup(required: true) { $("#projectAdminGroup") }
        projectReadonlyGroup(required: true) { $("#projectReadonlyGroup") }
        bugtrackerSpace(required: true) { $("#bugtrackerSpace") }
        platformRuntime(required: true) { $("#platformRuntime") }

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
