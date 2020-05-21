package provapp

import geb.spock.GebReportingSpec
import provapp.modules.ProjectCreateFormModule
import provapp.pages.HomePage
import provapp.pages.ProvAppLoginPage
import provapp.pages.ProvisionPage
import util.SpecHelper


class ProvAppSpec extends GebReportingSpec {
    static Properties applicationProperties = new SpecHelper().getApplicationProperties()

    def setup() {
        baseUrl = applicationProperties."config.provisioning.url"
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

        when: "Select to create project"
        provisionOptionChooser.doSelectCreateNewProject()
        report()

        then: "The project creation form is displayed"
        assert projectCreateForm

        when: "Fill the Project Form"
        projectCreateForm.projectName = 'project-name'
        projectCreateForm.projectKey = 'PROJECTKEY'
        projectCreateForm.projectDescription = 'E2E - Test Project'
        projectCreateForm.projectType = ProjectCreateFormModule.projectTypes.kanban

        and: "Choose to create a Jira / Confluence space"
        projectCreateForm.bugtrackerSpace = true

        and: "Choose to create a Openshift Project"
        projectCreateForm.platformRuntime = true

        and: "Click on provision"
        projectCreateForm.startCreationButton.click()

        and:
        waitFor {
            $(".modal-dialog").css("display") != "hidden"
        }
        sleep(5000)
        report()
        and:
        waitFor {
            $("#resButton").text() == "Close"
        }

        // TODO: Finish when the provapp is working again
        then:
        false
        report()
    }

    def "can add a component to an existing project"() {
        given: "We are logged in the provissioning app"
        doLoginProcess()

        expect:
        at HomePage

        when: "click Provision link"
        provisionLink.click()
        report()

        then:
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
        projectModifyForm.doSelectProject("JRPSB")
        report()

        then: "Project modification form is displayed"
        projectModifyForm.quickStarterTable.classes() == ["table-responsive"]

        when: "Select a quickstarter to add"
        projectModifyForm.doAddQuickStarter("be-golang-plain", "quick-1", 1)
        report()

        and: "Provision the QS"
        projectModifyForm.doStartProvision()

        and: "Wait until the provissioning process is running"
        waitFor {
            $(".modal-dialog").css("display") != "hidden"
        }
        sleep(5000)
        report()

        and: "Wait for the provissioning process has finish"
        waitFor {
            $("#resButton").text() == "Close"
        }

        // TODO: Finish when the provapp is working again
        then: "Component is added"
        false
        report()
    }



    def doLoginProcess() {
        to ProvAppLoginPage
        loginLegend.text() == 'Please Login'
        loginForm.fillLoginData()
        loginForm.doLogin()
        report()
    }

}