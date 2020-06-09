package org.ods.e2e

import org.ods.e2e.bitbucket.pages.DashboardPage
import org.ods.e2e.bitbucket.pages.LoginPage
import org.ods.e2e.bitbucket.pages.ProjectPage
import org.ods.e2e.openshift.pages.*
import org.ods.e2e.provapp.pages.HomePage
import org.ods.e2e.provapp.pages.ProvAppLoginPage
import org.ods.e2e.provapp.pages.ProvisionPage
import org.ods.e2e.util.BaseSpec

class ODSSpec extends BaseSpec {
    def simulate = true

    def projects = [
            default: [
                    name        : 'E2E Test Project4',
                    description : 'E2E Test Project',
                    key         : 'E2ET3',
                    type        : 'default',
                    hasJira     : true,
                    hasOpenshift: true,
                    components  : [
                            [componentId: 'component-vue', quickStarter: ProvisionPage.quickstarters.feVue],
                    ]
            ]
    ]

    def setup() {
        baseUrl = baseUrlProvisioningApp
    }

    def "PLAY"() {
        given:
        def project = projects.default
        project.key = 'e2et3'

        when: 'Visit Openshift'
        baseUrl = baseUrlOpenshift

        and: 'and login in Openshift'
        doOpenshiftLoginProcess()

        and: "Visit all project page"
        to ConsoleProjectsPage

        then:
        waitFor { projectList }
        def projects = findProjects(project.key)
        assert projects
        assert projects.contains(project.key + '-cd')

        when: 'Visit pods page'
        to PodsPage, project.key + '-cd'

        and:
        def pods = getPods()

        then:
        assert pods.find { pod -> pod.name.startsWith('jenkins') && pod.status == 'Running' }
    }

    /**
     * The purpose of this test case is to present a level of evidences that the use of Provisioning Application,
     * in combination with Boilerplates and infrastructure elements like Jenkins, SonarQube and Nexus answer
     * the following specifications:
     * - to present evidences that Jenkins, SonarQube and Nexus Artifact is available to build and deploy applications
     * as docker images.
     * - to present evidences that boilerplates can be listed on the Provisioning Application
     * - to present evidences that Jenkins slaves compile code based on the boilerplates
     * - to present evidences that using boilerplates, the ODS will provide means of standardized build,
     * quality assurance and deployment
     * - to present evidences that Provisioning Application creates the Atlassian and Openshift components based on
     * boilerplate
     * - to present evidences the data of provision components are stored on the filesystem
     */
    def "FT_01_001"() {
        // STEP 1: Login to provisioning application with administrator privileges
        given: 'We are logged in the provissioning app'
        def project = projects.default
        to ProvAppLoginPage
        doLoginProcess()

        expect: 'We are logged in the provisioning app'
        at HomePage

        // STEP 2: Click on the provisioning link page
        when: 'Click Provision link'
        provisionLink.click()

        then: 'We visit the provisioning page'
        at ProvisionPage

        // STEP 3: Click on create a new project and provide name
        when: 'We open the project creation form'
        provisionOptionChooser.doSelectCreateNewProject()

        and: 'We set the project name'
        projectCreateForm.projectName = project.name
        waitFor {
            projectCreateForm.projectKey.value()
        }

        // STEP 4: Click on start provision
        and: 'start provisioning'
        if (!simulate) {
            project.key = projectCreateForm.projectKey.value()

            projectCreateForm.startCreationButton.click()
            sleep(30000)

            // wait until project is created
            waitFor {
                $(".modal-dialog").css("display") != "hidden"
                $("#resProject.alert-success")
                $("#resButton").text() == "Close"
            }
            project.jiraUrl = $("#dataJiraUrl > a").text()
            project.confluenceUrl = $("#dataConfluenceUrl > a").text()
            project.jenkinsUrl = $("#dataJenkinsUrl > a").text()
            project.bitbucketUrl = $("#dataBitbucketUrl > a").text()
            project.provJobUrl = $("#dataJobUrls > a").text()
            report('Project created')
        }

        then: 'The project has been created'
        simulate ? true: $("#resProject.alert-success")

        // STEP 5: Go to bitbucket and search for the project with the generated project key
        when: 'Visit Bitbucket'
        baseUrl = baseUrlBitbucket
        to LoginPage

        and: 'we do login'
        doLogin()

        then: 'we are at the Dashboard'
        at DashboardPage
        report('at dashboard page')

        when: 'Visit project'
        to ProjectPage, project.key

        then: 'We are in the project page'
        currentUrl.endsWith("projects/${project.key}")
        report('bitbucket at project page')

        // STEP 6: Go to openshift – and find the new project with its key (-cd). Locate a running Jenkins instance
        when: 'Visit Openshift'
        baseUrl = baseUrlOpenshift

        and: 'and login in Openshift'
        doOpenshiftLoginProcess()

        and: "Visit all project page"
        to ConsoleProjectsPage
        waitFor(10000) { projectList }
        def projects = findProjects(project.key)

        then:
        assert projects
        assert projects.contains(project.key.toLowerCase() + '-cd')

                when: 'Visit pods page'
                to PodsPage, project.key.toLowerCase() + '-cd'
                sleep(5000)

                and:
        def pods = getPods()

        then:
        assert pods.find { pod -> pod.name.startsWith('jenkins') && pod.status == 'Running' }

        // STEP 7: Return to provisioning app (refresh page) and click on modify existing
        when:
        baseUrl = baseUrlProvisioningApp
        to ProvisionPage

        and:
        provisionOptionChooser.doSelectModifyProject()
        projectModifyForm.doSelectProject(project.key)

        // STEP 8: Click on the Quickstarter dropdown list and select a boilerplate “Frontend implemented with Vue JS”
        and:
        project.components.eachWithIndex { component, index ->
            projectModifyForm.doAddQuickStarter(component.quickStarter, component.componentId, index + 1)
            projectModifyForm.addQuickStarterButton.click()
        }
        report('Add quickstarters')

        // STEP 9: Click on Start Provision
        and:
        if (!simulate) {
            projectModifyForm.doStartProvision()
            sleep(15000)

            waitFor {
                $(".modal-dialog").css("display") != "hidden"
                $("#resProject.alert-success")
                $("#resButton").text() == "Close"
            }
            report('Status after Quickstarters Addition')
        }

        then: 'Quickstarter was added'
        simulate ? true: $("#resProject.alert-success")

        // STEP 10: Go to bitbucket into the above named project and check for your component repository with the name you provided.
        // STEP 11: Go to your project’s Jenkins and locate the provision job of the component
        // STEP 12: Go to your project’s Jenkins and locate the build job of the component – named component - name-master
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
        waitFor { $("#projectName ~ div.with-errors").text() }
        report('Project cannot be used')

        then: 'The ODS should display a message indicating this actions is not acceptable'
        assert $('#projectName ~ div.with-errors').text()

    }

    def doOpenshiftLoginProcess() {
        to OpenShiftLoginSelectorPage
        waitFor {
            title == 'Login - OpenShift Container Platform'
            ldapLink
        }
        ldapLink.click()

        at OpenShiftLoginPage
        doLogin()

        at ConsoleCatalogPage
    }
}
