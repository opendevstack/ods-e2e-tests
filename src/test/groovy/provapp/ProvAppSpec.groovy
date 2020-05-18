package provapp

import geb.spock.GebReportingSpec
import provapp.pages.HomePage
import provapp.pages.ProvAppLoginPage
import provapp.pages.ProvisionPage
import util.SpecHelper


class ProvAppSpec  extends GebReportingSpec {
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

    def "can add a component to a existing project"() {
        given: "user go to the login page"
        to ProvAppLoginPage

        and: "user not logged in"
        loginLegend.text() == 'Please Login'

        and: "user introduce the credentials"
        loginForm.fillLoginData()
        report()

        when: "click login"
        loginForm.doLogin()
        report()

        then:
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

        when: "Select a project a project"
        projectModifyForm.doSelectProject("JRPSB")
        report()

        then:
        projectModifyForm.quickStarterTable.classes() == ["table-responsive"]

        when: "Select a quickstarter to add"
        projectModifyForm.doAddQuickStarter("be-golang-plain", "quick-1", 1)
        report()

        and:
        projectModifyForm.doStartProvision()

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

        then:
        $("#resProject").classes().sort() != ["alert", "alert-danger"]
        report()

    }

}