package org.ods.e2e.provapp.modules

import geb.Module
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor

class ProjectModifyFormModule extends Module {

    def driver

    static content = {
        modifyForm(wait: true, required: true) { $("#modifyProject") }
        projectSelectControl(wait: true, required: true) { $("#projects") }
        quickStarteDropdown(wait: true, required: true) { $("select.form-control.quickstart-chooser") }
        quickStarterTable(wait: true, required: true) { $("#quickstartTable") }
        quickStarterAddGroup(wait: true, required: true) { $(".form-group.quickstartergroup") }
        addQuickStarterButton(wait: true, required: true) { $("button.btn-add") }
        startProvisionButton(wait: true, required: true) { $("#modifySubmit") }
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
        def css = '#modifySubmit'
        def element = driver.findElement(By.cssSelector(css))
        (driver as JavascriptExecutor).executeScript("arguments[0].scrollIntoView();", element)
        waitFor { !startProvisionButton.hasClass('disabled') }
        element.click()
    }

    /**
     * Retrieve the list of projects that can be selected to modify
     * @return the list of projects
     */
    def getProjects() {
        projectSelectControl.$("option").findResults { project ->
            project.value() ? [key: project.value(), name: project.text()] : null
        }
    }

    /**
     * Having a pattern of project key, it return the next numeric id to be use.
     * @param key The pattern to be use f.e. 'E2ET'
     * @return The Id number
     */
    def getNextId(key) {
        def max = projectSelectControl.$("option").findResults {
            project ->
                project?.value() && project?.value()?.startsWith(key) && (project.value() - key).isInteger() ?
                        (project.value() - key).toInteger() :
                        null
        }.max()

        max ? max + 1 : 1

    }

    def doScrollToEnd() {
        (driver as JavascriptExecutor).executeScript("window.scrollTo(0, document.body.scrollHeight);")
    }
}
