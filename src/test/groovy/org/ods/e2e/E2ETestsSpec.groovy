package org.ods.e2e

import org.ods.e2e.bitbucket.pages.DashboardPage
import org.ods.e2e.bitbucket.pages.LoginPage
import org.ods.e2e.bitbucket.pages.ProjectPage
import org.ods.e2e.jenkins.pages.JenkinsConsolePage
import org.ods.e2e.jenkins.pages.JenkinsLoginPage
import org.ods.e2e.jenkins.pages.JenkinsLoginSelectorPage
import org.ods.e2e.jira.pages.ComponentPage
import org.ods.e2e.openshift.pages.ConsoleCatalogPage
import org.ods.e2e.openshift.pages.ConsoleProjectsPage
import org.ods.e2e.openshift.pages.OpenShiftLoginPage
import org.ods.e2e.openshift.pages.OpenShiftLoginSelectorPage
import org.ods.e2e.provapp.pages.HomePage
import org.ods.e2e.provapp.pages.ProvAppLoginPage
import org.ods.e2e.provapp.pages.ProvisionPage
import org.ods.e2e.util.BaseSpec

/**
 * It is intend to do an E2E test of ODS.
 * The steps that we want to achive are:
 * - Create a project with the provisioning app
 * - Add components to that project
 * - Check that the needed resources had been created in
 *   - Jenkins, the setup jobs finished succesfully
 *   - BitBucket, contains a project and repositories for each component
 *   - Jira, contains a project and the components needed
 *   - OpenShift, contains the initial projects and the configuration to build and deploy the components.
 *
 *   The needed resources will be specific for the different project types and components.
 */
class E2ETestsSpec extends BaseSpec {

    def projects = [
            mro: [
                    name        : 'Validation Testing 2',
                    key         : 'VTATL2',
                    description : 'Validation Testing 2',
                    type        : 'default',
                    hasJira     : true,
                    hasOpenshift: true,
                    components  : [
                            [componentId: 'demo-app-carts', quickStarter: ProvisionPage.quickstarters.beGolangPlain],
                            [componentId: 'demo-app-catalogue', quickStarter: ProvisionPage.quickstarters.beJavaSpringboot],
                    ]
            ]
    ]

    def 'create mro project'() {
        given: 'We will use the mro project'
        def project = projects.mro
        def projectKey = project.key.toLowerCase()

        // ----------------------------------------
        // Check that we can create a project
        // TODO: When prov app will work
        // ----------------------------------------
        when: 'We create the project'
        baseUrl = baseUrlProvisioningApp
        to ProvAppLoginPage
        doLoginProcess()

        then:
        at HomePage

        when:
        provisionLink.click()
        at ProvisionPage

        and:
        createProject(project, this, true)

        then:
        true

        // ----------------------------------------------
        // Check that we can add components to the project
        // TODO: When prov app will work
        // ----------------------------------------------
        when:
        addComponents(project.key, project.components, this, true)

        then:
        true

        // --------------------------------------------------------------------------------
        // Check that the jobs to initialize the Quick Starters exists in Jenkins
        // and have finished succesfully
        // --------------------------------------------------------------------------------
        when:
        baseUrl = baseUrlJenkins

        and:
        doJenkinsLoginProcess()

        then: 'The project folder exists'
        assert $("#job_$projectKey-cd")

        when: 'Visit the jobs'
        $("#job_$projectKey-cd > td:nth-child(3) > a").click()

        and:
        project.components.each { component ->
            component.jobs = getComponentJobs(projectKey, component.componentId)
        }

        then: 'All the startup jobs finished succesfully'
        project.components.each {
            component ->
                assert component.jobs.find {
                    job -> job.value.odsStartupComponentJob && job.value.success
                }
        }

        // --------------------------------------------------------------------------------
        // Check that the repositories exists in BB and contains the basic setup of the QS
        // TODO: Check the proper setup of the repositories
        // --------------------------------------------------------------------------------
        when: 'Visit Bitbucket'
        baseUrl = baseUrlBitbucket
        to LoginPage

        and: 'we do login'
        doLogin()

        then: 'we are at the Dashboard'
        at DashboardPage
        report('at dashboard page')

        when: 'Visit project $projectKey'
        to ProjectPage, projectKey

        then: 'We are in the project page'
        currentUrl.endsWith("projects/$projectKey")
        report('at project page')

        when: 'We visit one repository'
        def repositories = getRepositoriesInfo()

        then: 'The repositories exits for each component'
        project.components.each { component ->
            assert repositories.findAll { repository -> repository.name == projectKey + '-' + component.componentId }.size() == 1
        }

        // ----------------------------------------
        // Check in OpenShift Project existence
        // TODO: Components existence, bc,dc, etc
        // ----------------------------------------
        when: 'Visit Openshift'
        baseUrl = baseUrlOpenshift

        and: 'and login in Openshift'
        doOpenshiftLoginProcess()

        and: "Visit all project page"
        to ConsoleProjectsPage

        then:
        waitFor { projectList }
        def projects = findProjects(projectKey)
        assert projects
        assert projects.contains(projectKey + '-cd')
        assert projects.contains(projectKey + '-dev')
        assert projects.contains(projectKey + '-test')


        // ----------------------------------------------------
        // Check in Jira Project and components existence
        // TODO: Components existence, bc,dc, etc
        // ----------------------------------------------------
        when: 'Visit Jira'
        baseUrl = baseUrlJira
        def jiraProjectKey = projectKey.toUpperCase()

        and: 'login in Jira'
        to org.ods.e2e.jira.pages.DashboardPage
        loginForm.doLoginProcess()

        and:
        to org.ods.e2e.jira.pages.ProjectPage, jiraProjectKey
        to ComponentPage, jiraProjectKey

        then:
        project.components.each { component ->
            assert existComponent(component.componentId)
        }
    }

    /**
     * The login in Jenkins has an special behavior as we have to go across openshift.
     */
    def doJenkinsLoginProcess() {
        to JenkinsLoginPage
        loginButton.click()

        at(new JenkinsLoginSelectorPage())
        ldapLink.click()

        at OpenShiftLoginPage
        doLogin()

        at JenkinsConsolePage
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
