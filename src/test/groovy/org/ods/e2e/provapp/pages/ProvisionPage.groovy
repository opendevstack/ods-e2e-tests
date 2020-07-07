package org.ods.e2e.provapp.pages

import geb.Page
import org.ods.e2e.provapp.modules.ProjectCreateFormModule
import org.ods.e2e.provapp.modules.ProjectModifyFormModule
import org.ods.e2e.provapp.modules.ProvisionOptionChooser

class ProvisionPage extends Page {

    static quickstarters = [
            beGolangPlain      : 'be-golang-plain',
            beTypescriptExpress: 'be-typescript-express',
            bePythonFlask      : 'be-python-flask',
            beScalaAkka        : 'be-scala-akka',
            beJavaSpringboot   : 'be-java-springboot',
            dsJupyterNotebook  : 'ds-jupyter-notebook',
            dsMlService        : 'ds-ml-service',
            dsRshiny           : 'ds-rshiny',
            feAngular          : 'fe-angular',
            feReact            : 'fe-react',
            feVue              : 'fe-vue',
            feIonic            : 'fe-ionic',
            airflowCluster     : 'airflow-cluster',
            e2eCypress         : 'e2e-cypress',
            dockerPlain        : 'docker-plain',
            releaseManager     : 'release-manager',
            e2eSpockGeb        : 'e2e-spock-geb'
    ]


    static url = "/provision"
    static at = { $("body > div.container > div.starter-template > h1").text() == 'Provision a project' }

    static content = {
        provisionOptionChooser { module ProvisionOptionChooser }
        projectModifyForm { module (new ProjectModifyFormModule(driver: driver)) }
        projectCreateForm(required: false) { module(new ProjectCreateFormModule(driver: driver)) }
    }

    /**
     * Create a project based in a description of the project with the format:
     * [
     *   name: 'Default Test Project',
     *   key: 'DTP1',
     *   description: 'Default Test Project for E2E Tests',
     *   type: 'default',
     *   hasJira: true,
     *   hasOpenshift: true,
     *   components: [
     *           [componentId: 'e2e-test-golang-component' , quickStarter: 'be-golang-plain']
     *  ]
     * @param project The project to create
     * @param spec the spec where it is executed (to report properly)
     * @param simulate If we are not creating the project, only simulating the creation.
     */
    def createProject(project, spec = null, simulate = false) {

        provisionOptionChooser.doSelectCreateNewProject()

        projectCreateForm.projectName = project.name
        projectCreateForm.projectKey = project.key
        projectCreateForm.projectDescription = project.description
        projectCreateForm.projectType = project.type
        projectCreateForm.bugtrackerSpace = project.hasJira
        projectCreateForm.platformRuntime = project.hasOpenshift
        if (spec) spec.report('Project setup')

        if (!simulate) {
            projectCreateForm.startCreationButton.click()
            waitFor {
                $(".modal-dialog").css("display") != "hidden"
            }
            if (spec) spec.report('Creating project')
            waitFor {
                $("#resButton").text() == "Close"
            }
            if (spec) spec.report('Project Created')
        }
    }

    /**
     * Add several Quick Starters to a project
     * @param project Key of the project
     * @param components The quickstarters we want to add
     * @param spec The spec for reporting
     * @param simulate if we simulate the creation
     */
    def addComponents(project, components, spec = null, simulate = false) {
        provisionOptionChooser.doSelectModifyProject()
        projectModifyForm.quickStarterTable.classes().sort() == ["table-responsive", "hidden"].sort()
        projectModifyForm.doSelectProject(project)

        components.eachWithIndex { component, index ->
            projectModifyForm.doAddQuickStarter(component.quickStarter, component.componentId, index + 1)
            projectModifyForm.addQuickStarterButton.click()
        }
        if (spec) {
            spec.report('Add quickstarters')
        }
        if (!simulate) {
            projectModifyForm.doStartProvision()
            waitFor {
                $(".modal-dialog").css("display") != "hidden"
            }
            sleep(5000)
            report('Status after Quickstarters Addition')

            waitFor {
                $("#resButton").text() == "Close"
            }
        }
    }


    /**
     * Having a pattern of project key, it return the next numeric id to be use.
     * @param key The pattern to be use f.e. 'E2ET'
     * @return The Id number
     */
    def getNextId(key) {
        provisionOptionChooser.doSelectModifyProject()
        projectModifyForm.getNextId(key)
    }
}
