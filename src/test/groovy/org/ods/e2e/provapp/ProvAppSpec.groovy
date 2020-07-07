package org.ods.e2e.provapp

import org.ods.e2e.provapp.pages.HomePage
import org.ods.e2e.provapp.pages.ProvAppLoginPage
import org.ods.e2e.provapp.pages.ProvisionPage
import org.ods.e2e.util.BaseSpec
import org.ods.e2e.util.SpecHelper

class ProvAppSpec extends BaseSpec {
    static Properties applicationProperties = new SpecHelper().getApplicationProperties()

    def static project
    def static component

    def static projects = [
            default: [
                    name        : 'E2E Test Project',
                    description : 'E2E Test Project',
                    type        : 'default',
                    hasJira     : true,
                    hasOpenshift: true,
                    components  : [
                            [componentId: 'component-golang', quickStarter: ProvisionPage.quickstarters.beGolangPlain],
                    ]
            ]
    ]

    def setup() {
        baseUrl = applicationProperties."config.provisioning.url"
        project = projects.default
        component = project.components.first()
    }

    def "can login to the ProvApp"() {
        given: "user go to the login page"
        to ProvAppLoginPage

        and: "user not logged in"
        loginLegend.text() == 'Please Login'

        and: "user introduce the credentials"
        loginForm.fillLoginData()

        when: "click login"
        loginForm.doLogin()

        then:
        at HomePage
        assert overviewLink
        assert historyLink
        assert aboutLink
        assert provisionLink
        assert logoutLink
        report()
    }

    def "can create a project"() {
        given: "We are logged in the provissioning app"
        to ProvAppLoginPage
        doLoginProcess()

        expect:
        at HomePage

        when: "click Create Project link"
        provisionLink.click()

        then: "We are at the provision page"
        at ProvisionPage
        report()

        and: "The provission Chooser is displayed"
        assert provisionOptionChooser
        assert provisionOptionChooser.optionModifyProject
        assert provisionOptionChooser.optionCreateNewProject

        when: 'Select to modify project'
        provisionOptionChooser.doSelectModifyProject()

        and: 'Get the project list'
        def nextId = getNextId('E2ECT')

        and: "Select to create project"
        provisionOptionChooser.doSelectCreateNewProject()
        report()

        then: "The project creation form is displayed"
        assert projectCreateForm

        when: "Fill the Project Form"
        projectCreateForm.projectName = String.format("$project.name - %02d", nextId)
        projectCreateForm.projectDescription = project.description
        projectCreateForm.projectType = project.type

        and: "Choose to create a Jira / Confluence space"
        projectCreateForm.bugtrackerSpace = project.hasJira

        and: "Choose to create a Openshift Project"
        projectCreateForm.platformRuntime = project.hasOpenshift

        and: 'set generated key'
        waitFor { !projectCreateForm.projectKey.value().isEmpty() }
        project.key = String.format('E2ECT%02d', nextId)
        projects.default.key = project.key
        projectCreateForm.projectKey = project.key

        and: "Click on provision"
        if (!simulate) {
            projectCreateForm.doStartProvision()
        }

        and:
        if (!simulate) {
            waitFor {
                $(".modal-dialog").css("display") != "hidden"
            }
            sleep(5000)
            report()
        }

        and:
        if (!simulate) {
            waitFor {
                $("#resButton").text() == "Close"
            }
        }

        then:
        if (!simulate) $("#resProject.alert-success")
        report()
    }

    def "can add a component to an existing project"() {
        given: "We are logged in the provissioning app"
        to ProvAppLoginPage
        doLoginProcess()

        expect:
        at HomePage

        when: "click Provision link"
        provisionLink.click()
        report()

        then: "We visit the provisioning page"
        at ProvisionPage

        and:
        assert provisionOptionChooser
        assert provisionOptionChooser.optionModifyProject
        assert provisionOptionChooser.optionCreateNewProject

        when: "Select to modify project"
        provisionOptionChooser.doSelectModifyProject()
        report()

        then:
        assert projectModifyForm
        assert projectModifyForm.modifyForm
        assert projectModifyForm.projectSelectControl
        assert projectModifyForm.quickStarterTable
        assert projectModifyForm.quickStarterAddGroup
        projectModifyForm.quickStarterTable.classes().sort() == ["table-responsive", "hidden"].sort()

        when: "Select a project"
        projectModifyForm.doSelectProject(project.key)
        report()

        then: "Project modification form is displayed"
        projectModifyForm.quickStarterTable.classes() == ["table-responsive"]

        when: "Select a quickstarter to add"
        projectModifyForm.doAddQuickStarter(component.quickStarter, component.componentId, 1)
        report()

        and: "Provision the QS"
        if (!simulate) {
            projectModifyForm.doStartProvision()
        }

        and: "Wait until the provissioning process is running"
        if (!simulate) {
            waitFor {
                $(".modal-dialog").css("display") != "hidden"
            }
            sleep(5000)
            report()
        }
        and: "Wait for the provissioning process has finished"
        if (!simulate) {
            waitFor {
                $("#resButton").text() == "Close"
            }
        }

        then: "Component is added"
        if (!simulate) {
            $("#resProject.alert-success").size() == 1
        }
        report()
    }

}