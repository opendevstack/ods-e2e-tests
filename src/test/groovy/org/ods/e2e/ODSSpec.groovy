package org.ods.e2e

import org.ods.e2e.provapp.pages.HomePage
import org.ods.e2e.provapp.pages.ProvAppLoginPage
import org.ods.e2e.provapp.pages.ProvisionPage
import org.ods.e2e.util.BaseSpec

class ODSSpec extends BaseSpec {

    def setup() {
        baseUrl = baseUrlProvisioningApp
    }

    /**
     * The purpose of this test case is to demonstrate the functionality to prevent OpenDevStack
     * to delete projects on Atlassian application.
     */
    def "FT_01_005"() {
        // STEP 1: Login to provisioning app and pick a name of a project that will be displayed after
        //         clicking on Modify existing project
        given: 'We are logged in the provissioning app'
        to ProvAppLoginPage
        doLoginProcess()

        expect: 'We are logged in the provisioning app'
        at HomePage

        when: 'Click Provision link'
        provisionLink.click()

        then: 'We visit the provisioning page'
        at ProvisionPage

        when: 'Select to modify project'
        provisionOptionChooser.doSelectModifyProject()

        and: 'Get the project list'
        def projects = projectModifyForm.getProjects()

        and: 'Get a random project'
        Random rnd = new Random()
        def project = projects[rnd.nextInt(projects.size)]
        projectModifyForm.doSelectProject(project.key)
        report('project selected')

        // STEP 2: Select the option to create a new project using the same name picked on the step 2.
        and: 'We open the project creation form'
        provisionOptionChooser.doSelectCreateNewProject()

        and: 'Use the previously selected name'
        projectCreateForm.projectKey = project.key
        projectCreateForm.projectName = project.key
        waitFor { $("#projectName ~ div.with-errors").text()}
        report('Project cannot be used')

        then: 'The ODS should display a message indicating this actions is not acceptable'
        assert $('#projectName ~ div.with-errors').text()

    }
}
