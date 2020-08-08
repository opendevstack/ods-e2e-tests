package org.ods.e2e


import org.ods.e2e.bitbucket.pages.DashboardPage
import org.ods.e2e.bitbucket.pages.LoginPage
import org.ods.e2e.bitbucket.pages.ProjectPage
import org.ods.e2e.bitbucket.pages.RepositoryPage
import org.ods.e2e.jenkins.pages.JenkinsConsoleParametrizedBuildPage
import org.ods.e2e.jenkins.pages.JenkinsJobFolderPage
import org.ods.e2e.openshift.pages.ConsoleDeploymentsPage
import org.ods.e2e.openshift.pages.ConsoleProjectsPage
import org.ods.e2e.openshift.pages.PodsPage
import org.ods.e2e.provapp.pages.HomePage
import org.ods.e2e.provapp.pages.ProvAppLoginPage
import org.ods.e2e.provapp.pages.ProvisionPage
import org.ods.e2e.util.BaseSpec
import org.ods.e2e.util.GitUtil
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

class ODSSpec extends BaseSpec {

    def static projects = [
            default: [
                    name        : 'E2E Test Project',
                    description : 'E2E Test Project',
                    key         : 'E2ECT',
                    type        : 'default',
                    hasJira     : true,
                    hasOpenshift: true,
                    components  : [
                            [componentId: 'component-golang', quickStarter: ProvisionPage.quickstarters.beGolangPlain],
                    ]
            ]
    ]
    def static releaseManagerComponent = [componentId: 'release-manager-component', quickStarter: ProvisionPage.quickstarters.releaseManager]

    // MRO Pipeline created previous releases, it will use the repository created for the releaseManagerComponent
    // in this case project.key-e2e-release-manager
    def static releaseManagerPipelineJob = "mro-pipeline-master"
    def static releaseManagerPipelineComponentName = "mro-pipeline"

    def leVADocsTemplates = [
            'CFTP-1.html.tmpl',
            'CFTP-3.html.tmpl',
            'CFTP-4.html.tmpl',
            'CFTP-5.html.tmpl',
            'CFTR-1.html.tmpl',
            'CFTR-3.html.tmpl',
            'CFTR-4.html.tmpl',
            'CFTR-5.html.tmpl',
            'CSD-1.html.tmpl',
            'CSD-3.html.tmpl',
            'CSD-4.html.tmpl',
            'CSD-5.html.tmpl',
            'DIL.html.tmpl',
            'DTP.html.tmpl',
            'DTR.html.tmpl',
            'footer.inc.html.tmpl',
            'header.inc.html.tmpl',
            'IVP.html.tmpl',
            'IVR.html.tmpl',
            'Overall-Cover.html.tmpl',
            'Overall-TIR-Cover.html.tmpl',
            'RA.html.tmpl',
            'SSDS-1.html.tmpl',
            'SSDS-3.html.tmpl',
            'SSDS-4.html.tmpl',
            'SSDS-5.html.tmpl',
            'TCP.html.tmpl',
            'TCR.html.tmpl',
            'TIP.html.tmpl',
            'TIR.html.tmpl',
            'TRC.html.tmpl',
    ]

    def setup() {
        // We will start with the provisioning app as the base url
        println 'Tests setup'
    }


    /**
     * Test Objective:
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
     *
     * Prerequisites:
     * Be logged in as Provisioning Application administrator, have an OpenShift project, have access to the
     * console/terminal logs of Jenkins, Nexus and SonarQube and have administrator access to the Bitbucket repositories.
     */
    def "FT_01_001"() {
        // -------------------------------------------------------------------------------------------------------------
        // STEP 1: Login to provisioning application with administrator privileges
        //          Result: Login works, within a provisioning and history links
        // -------------------------------------------------------------------------------------------------------------
        given: 'We are logged in the provissioning app'
        def project = projects.default
        baseUrl = baseUrlProvisioningApp

        to ProvAppLoginPage
        doLoginProcess()

        expect: 'We are logged in the provisioning app'
        at HomePage
        report("step 1 - logged in provissioning app")

        // -------------------------------------------------------------------------------------------------------------
        // STEP 2: Click on the provisioning link page
        //          Result: A page with two options: create a new or modify existing project
        // -------------------------------------------------------------------------------------------------------------
        when: 'Click Provision link'
        provisionLink.click()

        then: 'We visit the provisioning page'
        at ProvisionPage
        report("step 2 - visit the provisioning page")

        // -------------------------------------------------------------------------------------------------------------
        // STEP 3: Click on create a new project and provide name
        //          Result: Auto-generated project key
        // -------------------------------------------------------------------------------------------------------------
        when: 'Check for the key we are going to use'
        provisionOptionChooser.doSelectModifyProject()

        and: 'Get the next key'
        def nextId = getNextId(project.key)
        project.name = String.format("$project.name - %02d", nextId)
        project.key = String.format("$project.key%02d", nextId)

        and: 'We open the project creation form'
        provisionOptionChooser.doSelectCreateNewProject()

        and: 'We set the project name'
        projectCreateForm.projectName = project.name

        then:
        waitFor { !projectCreateForm.projectKey.value().isEmpty() }

        report("step 3 - set the project name - autogenerated key")

        // -------------------------------------------------------------------------------------------------------------
        // STEP 4: Click on start provision
        //          Result: Provisioning success message displayed
        // -------------------------------------------------------------------------------------------------------------
        when: 'start provisioning'
        if (!simulate) {
            projectCreateForm.projectKey = project.key
            projectCreateForm.doStartProvision()

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
        }

        // Let give some time to execute background jobs
        sleep(30000)

        then: 'The project has been created'
        simulate ? true : $("#resProject.alert-success").size() == 1

        report("step 4 - project has been created")
        $("#resButton").click()


        // -------------------------------------------------------------------------------------------------------------
        // STEP 5: Go to bitbucket and search for the project with the generated project key
        //          Result: Bitbucket project is available
        // -------------------------------------------------------------------------------------------------------------
        when: 'Visit Bitbucket'
        baseUrl = baseUrlBitbucket
        to LoginPage

        and: 'we do login'
        doLogin()

        then: 'we are at the Dashboard'
        at DashboardPage

        when: 'Visit project'
        to ProjectPage, project.key

        then: 'We are in the project page'
        currentUrl.endsWith("projects/${project.key}/")
        report("step 5 - project in bitbucket")

        // -------------------------------------------------------------------------------------------------------------
        // STEP 6: Go to openshift – and find the new project with its key (-cd). Locate a running Jenkins instance
        //          Result: Find a running Jenkins deployment – click on it and verify that the image used comes from the
        //                  CD namespace
        // -------------------------------------------------------------------------------------------------------------
        when: 'Visit Openshift'
        baseUrl = baseUrlOpenshift

        and: 'and login in Openshift'
        doOpenshiftLoginProcess()

        then: "Visit all project page and check for the projects"
        waitFor('mediumSlow') {
            to ConsoleProjectsPage
            findProjects(project.key).size() > 0
            findProjects(project.key).contains(project.key.toLowerCase() + '-cd')
        }


        when:
        'Visit pods page'
        to PodsPage, project.key.toLowerCase() + '-cd'
        sleep(5000)

        then:
        waitFor('verySlow') {
            getPods().find { pod ->
                pod.name.startsWith('jenkins') &&
                        pod.status == 'Running' &&
                        !pod.isDeployPod &&
                        pod.containersReady == '1/1'
            }

            getPods().find { pod ->
                pod.name.startsWith('webhook') &&
                        pod.status == 'Running' &&
                        !pod.isDeployPod &&
                        pod.containersReady == '1/1'
            }
        }
        report("step 6 - existing jenkins instance for the project")

        // -------------------------------------------------------------------------------------------------------------
        // STEP 7: Return to provisioning app (refresh page) and click on modify existing
        //          Result: Created project with the key and name from step 3 is available
        // -------------------------------------------------------------------------------------------------------------
        when:
        baseUrl = baseUrlProvisioningApp
        to ProvisionPage

        and:
        provisionOptionChooser.doSelectModifyProject()
        projectModifyForm.doSelectProject(project.key)
        report("step 7 - go to provissioning app")

        // -------------------------------------------------------------------------------------------------------------
        // STEP 8: Click on the Quickstarter dropdown list
        //          Result: The component list is displayed
        // -------------------------------------------------------------------------------------------------------------
        and: 'Click on quickstarte dropdownlist'
        projectModifyForm.quickStarteDropdown.click()
        projectModifyForm.doScrollToEnd()
        sleep(1000)
        report("step 8 - Clicked on dropdownlist")

        // -------------------------------------------------------------------------------------------------------------
        // STEP 9: Click on the Quickstarter dropdown list and select a boilerplate “Backend - golang”
        //          Result: The component is added
        // -------------------------------------------------------------------------------------------------------------
        and:
        project.components.eachWithIndex { component, index ->
            projectModifyForm.doAddQuickStarter(component.quickStarter, component.componentId, index + 1)
            projectModifyForm.addQuickStarterButton.click()
        }
        report("step 9 - add quickstarter")

        // -------------------------------------------------------------------------------------------------------------
        // STEP 10: Click on Start Provision
        //          Result: Message the provision is in progress.
        //                  Another message (screen) appears with the links of:
        //                  Jenkins, Bitbucket, Project link, Provisioning jobs, and others.
        // -------------------------------------------------------------------------------------------------------------
        and:
        if (!simulate) {
            projectModifyForm.doStartProvision()
            sleep(15000)

            waitFor {
                $(".modal-dialog").css("display") != "hidden"
                $("#resProject.alert-success")
                $("#resButton").text() == "Close"
            }
            report('step 10 - Status after Quickstarters Addition')
        }

        then:
        'Quickstarter was added'
        simulate ? true : $("#resProject.alert-success")
        report("step 10 - quick starter provisioned")

        // -------------------------------------------------------------------------------------------------------------
        // STEP 11: Go to bitbucket into the above named project and check for your component repository
        // with the name you provided.
        //          Result: The content of the components are available.
        // -------------------------------------------------------------------------------------------------------------
        when:
        'Visit Bitbucket'
        baseUrl = baseUrlBitbucket
        to ProjectPage, project.key

        then:
        'We are in the project page'
        currentUrl.endsWith("projects/${project.key}/")

        when: 'We visit one repository'
        def repositories = getRepositoriesInfo()

        then:
        'The repositories exits for each component'
        project.components.each { component ->
            assert repositories.findAll { repository -> repository.name.toLowerCase() == (project.key + '-' + component.componentId).toLowerCase() }.size() == 1
        }
        report("step 11 - components repositories in bitbucket")

        // -------------------------------------------------------------------------------------------------------------
        // STEP 12: Go to your project’s Jenkins and locate the provision job of the component
        // with the name you provided.
        //          Result: Instance can be found and green (successful)
        // -------------------------------------------------------------------------------------------------------------
        when:
        baseUrl = getJenkinsBaseUrl(project.key)

        and:
        doJenkinsLoginProcess()

        then: 'The project folder exists'
        assert $("#job_${project.key.toLowerCase()}-cd")

        when:
        'Visit the jobs'
        to JenkinsJobFolderPage, project.key

        and:
        project.components.each { component ->
            component.jobs = getComponentJobs(project.key, component.componentId)
        }

        then:
        'The component startup jobs finished succesfully'
        waitFor('verySlow') {
            project.components.each {
                component ->
                    getComponentJobs(project.key, component.componentId).jobs.find {
                        job -> job.value.odsStartupComponentJob && job.value.success
                    }
            }
        }

        report("step 12 - provision job of the component")

        // -------------------------------------------------------------------------------------------------------------
        // STEP 13: Go to your project’s Jenkins and locate the build job of the component named *component-name*-master
        // with the name you provided.
        //          Result: Build job instance found and green.
        //                  A log in Jenkins shows on Console Output:
        //                  - Check out the codesource
        //                  - Usage the corresponding of Jenkin-slave
        //                  - Compiling source code with libraries from Nexus
        //                  - Running available unit test
        //                  - Checking the code quality with Sonarqube
        //                  - Building a docker image
        //                  - Deployment of the docker image
        // -------------------------------------------------------------------------------------------------------------
        and:
        'Checks that exists jobs that are not qs startup jobs for the components'
        waitFor('verySlow') {
            project.components.each {
                component ->
                    getComponentJobs(project.key, component.componentId).jobs.find {
                        job -> !job.value.odsStartupComponentJob && job.value.success
                    }
            }
        }

        report("step 13 - build job of the component")
    }

    /**
     * Test Objective:
     * The purpose of this test case is to demonstrate the functionality to orchestrate multiple repositories with the
     * deployment, and  to automatically produce LeVA documentation. This test will use the Provisioning
     * Application to create the necessary components in Atlassian and Openshift in a way it can demonstrate the entire
     * test to answer the required functionalities:
     * - to present evidences that ODS MRO orchestrates multiples repositories.
     * - to present evidence that ODS MRO provide services to automate LeVA documentation generation.
     *
     * Prerrequisites
     * Be logged in as Provisioning Application administrator, have a OpenShift project, have access to the console/terminal logs
     * of Jenkins, Nexus and SonarQube and have administrator access to the Bitbucket repositories. FT_01_001 executed
     * successfully.
     *
     *  STEP 1:	Login to provisioning app – pick the created project and provision a new component based on the
     *          release-manager quickstarter.
     *
     *          Result: With your bitbucket project a new repository with the release manager is available
     *
     *          Notes:
     *                We first need to check that the Jenkins jobs had finalized properly to provision the release manager
     *                before continuing with the steps of the test
     *
     *  STEP 2:	Checkout / git clone the release manager repository and open metadata.yml
     *
     *           Result: Repository cloned, metadata.yml is available
     *
     *  STEP 3:	Add a new repository section into metadata.yml pointing to the component repository created earlier
     *          (see perreq.) – commit and push.
     *
     *          Result: New build of release manager in Jenkins visible, including a new build & deployment of your
     *                  component.
     *
     *          Notes:
     *               By default the Release Manager does not have a Webhook to jenkins, so we need to trigger manually the build process
     *
     *          3.1 Modify the repository section and push it
     *          3.2 Trigger Jenkins build of the release manager.
     *          3.3 Check jenkins status for jobs of Release Manager and component
     *          3.4 When both jobs finalized, check in openshift that there's a deployment of the component
     *
     *  STEP 4:	Go to bitbucket opendevstack project and locate ods-document-generation-templates
     *
     *          Result: Repository is available and within document templates for LeVa can be found.
     *
     *  STEP 5:	Go to openshift – locate your project’s cd project (name-cd) and check for a running instance of Document generation service
     *
     *          Result: Document Generation service pod is available.
     */
    def "FT_01_003"() {
        // -------------------------------------------------------------------------------------------------------------
        // STEP 1:	Login to provisioning app – pick the created project and provision a new component based on the
        //          release-manager quickstarter
        //          Result: With your bitbucket project a new repository with the release manager is available
        // -------------------------------------------------------------------------------------------------------------
        given: 'We login in the provision app'
        def project = projects.default
        project.components.add(releaseManagerComponent)

        and:
        baseUrl = baseUrlProvisioningApp

        and:
        to ProvAppLoginPage
        doLoginProcess()

        expect: 'We are logged in the provisioning app'
        at HomePage

        when: 'Click Provision link'
        provisionLink.click()

        then: 'We visit the provisioning page'
        at ProvisionPage

        when:
        provisionOptionChooser.doSelectModifyProject()
        projectModifyForm.doSelectProject(project.key)

        and:
        projectModifyForm.doAddQuickStarter(releaseManagerComponent.quickStarter, releaseManagerComponent.componentId)
        report('step 1: Quick starter to add')

        projectModifyForm.addQuickStarterButton.click()

        and:
        if (!simulate) {
            projectModifyForm.doStartProvision()
            sleep(5000)
            waitFor('extremelySlow') {
                $(".modal-dialog").css("display") != "hidden"
                $("#resProject.alert-success")
                $("#resButton").text() == "Close"
            }
        }

        then: 'Quickstarter was added'
        simulate ? true : $("#resProject.alert-success")
        report('step 1: Quick starter added')

        // -------------------------------------------------------------------------------------------------------------
        // We first need to check that the Jenkins jobs had finalized properly for the release manager
        // Before continuing with the steps of the test
        // -------------------------------------------------------------------------------------------------------------
        when: 'visit Jenkins'
        baseUrl = getJenkinsBaseUrl(project.key)
        doJenkinsLoginProcess()

        and: 'Retrieve the jobs related with the Release Manager deploy'
        to JenkinsJobFolderPage, project.key
        if (activateAutorefreshLink) {
            activateAutorefreshLink.click()
        }

        then: 'There must be a job to create the Release Manager Component'
        waitFor() {
            getComponentJobs(project.key, releaseManagerComponent.componentId).find {
                job -> job.value.odsStartupComponentJob
            }
        }

        and: 'Wait if the job is still running'
        waitFor('verySlow') {
            getComponentJobs(project.key, releaseManagerComponent.componentId).find {
                job -> job.value.odsStartupComponentJob && job.value.success
            }
        }

        // -------------------------------------------------------------------------------------------------------------
        // End checking jenkins status
        // -------------------------------------------------------------------------------------------------------------

        when: 'Visit Bitbucket'
        baseUrl = baseUrlBitbucket
        to LoginPage

        and: 'we do login'
        doLogin()

        then: 'we are at the Dashboard'
        at DashboardPage

        when: 'Visit project'
        to ProjectPage, project.key

        then: 'We are in the project page'
        currentUrl.endsWith("projects/${project.key}/")
        report("step 1 - project in bitbucket")

        when: 'We visit one repository'
        def repositories = getRepositoriesInfo()

        then: 'The repositories exits for the release manager'
        assert repositories.findAll { repository ->
            repository.name.toLowerCase() == (project.key + '-' + releaseManagerComponent.componentId).toLowerCase()
        }.size() == 1

        report("step 1 - release manager repository in bitbucket")


        // -------------------------------------------------------------------------------------------------------------
        // STEP 2:	Checkout / git clone the release manager repository and open metadata.yml
        //          Result: Repository cloned, metadata.yml is available
        // -------------------------------------------------------------------------------------------------------------
        when:
        def repositoryFolder = GitUtil.cloneRepository(projects.default.key, releaseManagerComponent.componentId)

        and:
        DumperOptions options = new DumperOptions()
        options.setPrettyFlow(true)
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK)
        Yaml parser = new Yaml(options)

        def metadataYml = parser.load(("$repositoryFolder/metadata.yml" as File).text)

        then:
        assert metadataYml

        // -------------------------------------------------------------------------------------------------------------
        // STEP 3:	Add a new repository section into metadata.yml pointing to the component repository created earlier
        //          (see perreq.) – commit and push
        //
        //          Result: New build of release manager in Jenkins visible, including a new build & deployment of your
        //          component.
        //
        //          3.1 Modify the repository section and push it
        //          3.2 Trigger Jenkins build of the release manager.
        //          3.3 Check jenkins status for jobs of Release Manager and component
        //          3.4 When both jobs finalized, check in openshift that there's a deployment of the component
        // -------------------------------------------------------------------------------------------------------------


        // -------------------------------------------------------------------------------------------------------------
        //      3.1 Modify the repository section and push it
        // -------------------------------------------------------------------------------------------------------------
        when: 'Edit metadata.yml'
        def metadataRepositories = metadataYml.getAt('repositories')
        if (metadataRepositories == null) {
            metadataYml.putAt('repositories', [])
            metadataRepositories = metadataYml.getAt('repositories')
        }
        def component = project.components.first()
        metadataRepositories.putAt(metadataRepositories.size, [
                id  : component.componentId.toLowerCase(),
                name: "$project.key-$component.componentId".toLowerCase(), type: 'ods'])

        and: 'Save metada.yml'
        parser.dump(metadataYml, new FileWriter("$repositoryFolder/metadata.yml"))

        and: 'Commit the file'
        GitUtil.commitAddAll('New component added')

        and: 'Push it to the repository'
        GitUtil.push('origin')

        // -------------------------------------------------------------------------------------------------------------
        //       3.2 Trigger Jenkins build of the release manager.
        // -------------------------------------------------------------------------------------------------------------
        and:
        baseUrl = getJenkinsBaseUrl(project.key)

        def parameters = [environment: 'dev', version: 'WIP',]
        to JenkinsConsoleParametrizedBuildPage, project.key, releaseManagerPipelineJob
        fillData(parameters)
        report('step 3.2 - trigger build')

        submitButton.click()

        // -------------------------------------------------------------------------------------------------------------
        //       3.3 Check jenkins status for Release Manager
        // -------------------------------------------------------------------------------------------------------------
        and:
        to JenkinsJobFolderPage, project.key
        def jobs = [releaseManagerPipelineComponentName]

        then: 'Wait both to start and finalize'
        waitJobsStart(jobs)
        waitJobsFinalize(jobs)

        then: 'And finalize succesfully'
        getComponentJobs(project.key, releaseManagerPipelineComponentName).every {
            job -> job.value.success
        }
        report('step 3.3 - jobs executed')

        // -------------------------------------------------------------------------------------------------------------
        //       3.4 When both jobs finalized, check in openshift that there's a deployment of the component
        // -------------------------------------------------------------------------------------------------------------
        when: 'Visit Openshift'
        baseUrl = baseUrlOpenshift

        and: 'and login in Openshift'
        doOpenshiftLoginProcess()

        and: 'Visit deployment page'
        to ConsoleDeploymentsPage, project.key.toLowerCase() + '-dev'
        sleep(5000)

        and: 'We get the deployments'
        def deployments = getDeployments()

        then: 'There is a deployment for the component'
        assert deployments.find { deployment ->
            deployment.name == component.componentId
        }
        report('step 3.4 - exists component deployment')

        // -------------------------------------------------------------------------------------------------------------
        // STEP 4:	Go to bitbucket opendevstack project and locate ods-document-generation-templates
        //          Result: Repository is available and within document templates for LeVa can be found.
        // -------------------------------------------------------------------------------------------------------------
        when: 'Visit project OPENDEVSTACK'
        baseUrl = baseUrlBitbucket
        to ProjectPage, 'OPENDEVSTACK'

        then: 'We are in the project page'
        currentUrl.endsWith("projects/OPENDEVSTACK/")

        when: 'Get repositories'
        def odsRepositories = getRepositoriesInfo()

        then: 'Then ods-document-generation-templates exists'
        assert odsRepositories.findAll {
            repository -> repository.name.toLowerCase() == 'ods-document-generation-templates'
        }.size() == 1

        when:
        to RepositoryPage, 'OPENDEVSTACK', 'ods-document-generation-templates', 'templates', [at: 'release/v1.0']

        and: 'Get the existing files'
        def templates = getFiles()

        then: 'exists all the files needed for the LeVADocs templates'
        leVADocsTemplates.each {
            leVAtemplate -> templates.contains(leVAtemplate)
        }
        report("step 4 - Repository is available and within document templates for LeVa can be found.")

        // -------------------------------------------------------------------------------------------------------------
        // STEP 5:	Go to openshift – locate your project’s cd project (name-cd) and check for a running instance of
        //          Document generation service
        //          Result: Document Generation service pod is available
        // -------------------------------------------------------------------------------------------------------------
        when: 'Visit Openshift'
        baseUrl = baseUrlOpenshift

        and: 'Visit pods page'
        to PodsPage, project.key.toLowerCase() + '-cd'
        sleep(5000)

        and:
        def pods = getPods()

        then:
        assert pods.find { pod -> pod.name.startsWith('docgen') && pod.status == 'Running' }
        report("step 5 - Document Generation service pod is available")
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

    def cleanup() {
        println 'Cleanup method cookies'
        cleanupAllCookies(projects.default.key)
    }
}
