package org.ods.e2e.provapp.modules

import geb.Module
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor

class ProjectCreateFormModule extends Module {
    def driver

    static projectTypes = [default: 'default',
                           kanban : 'kanban',
                           edp    : 'EDProject']
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
        projectUserGroup(wait:true, required: true) { $("#projectUserGroup") }
        projectReadonlyGroup(wait:true, required: true) { $("#projectReadonlyGroup") }
        bugtrackerSpace(wait:true, required: true) { $("#bugtrackerSpace") }
        platformRuntime(wait:true, required: true) { $("#platformRuntime") }

        startCreationButton(wait: true, required: false) { createForm.$('#createProject > fieldset > button') }
    }

    def doSelectProject(id) {
        projectSelectControl = id
        sleep(5000)
    }

    def doChooseProjectType(def projectType) {
        projectType.value(projectType)
    }

    def doStartProvision() {
        def css = '#createProject > fieldset > button'
        def element = driver.findElement(By.cssSelector(css))
        (driver as JavascriptExecutor).executeScript("arguments[0].scrollIntoView();", element)
        waitFor { !startCreationButton.hasClass('disabled') }
        element.click()
    }
}
