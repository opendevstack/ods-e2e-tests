package org.ods.e2e

import org.ods.e2e.jenkins.pages.JenkinsConsolePage
import org.ods.e2e.jenkins.pages.JenkinsLoginPage
import org.ods.e2e.jenkins.pages.JenkinsLoginSelectorPage
import org.ods.e2e.openshift.pages.OpenShiftLoginPage
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
        given: 'We will use the default project'
        def project = projects.mro

        when: 'We create the project'
        browser.setBaseUrl(baseUrlProvisioningApp)
        to ProvAppLoginPage
        doLoginProcess()

        then:
        at HomePage

        when:
        provisionLink.click()
        at ProvisionPage

        and:
        createProject(project, this, true)

        // TODO: When prov app will work
        then:
        true

        when:
        addComponents(project.key, project.components, this, true)

        // TODO: When prov app will work
        then:
        true

        when:
        browser.setBaseUrl(baseUrlJenkins)

        and:
        doJenkinsLoginProcess()
        def projectKey = project.key.toLowerCase()

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
    }

    def doJenkinsLoginProcess() {
        to JenkinsLoginPage
        loginButton.click()

        at(new JenkinsLoginSelectorPage())
        ldapLink.click()

        at OpenShiftLoginPage
        doLogin()

        at JenkinsConsolePage
    }

}
