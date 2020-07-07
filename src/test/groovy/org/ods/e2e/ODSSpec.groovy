package org.ods.e2e


import org.ods.e2e.bitbucket.pages.DashboardPage
import org.ods.e2e.bitbucket.pages.LoginPage
import org.ods.e2e.bitbucket.pages.ProjectPage
import org.ods.e2e.bitbucket.pages.RepositoryPage
import org.ods.e2e.jenkins.pages.JenkinsConsoleParametrizedBuildPage
import org.ods.e2e.jenkins.pages.JenkinsJobFolderPage
import org.ods.e2e.openshift.client.OpenShiftClient
import org.ods.e2e.openshift.pages.ConsoleDeploymentsPage
import org.ods.e2e.openshift.pages.ConsoleProjectsPage
import org.ods.e2e.openshift.pages.ConsoleResourcesConfigMaps
import org.ods.e2e.openshift.pages.PodsPage
import org.ods.e2e.provapp.pages.HomePage
import org.ods.e2e.provapp.pages.ProvAppLoginPage
import org.ods.e2e.provapp.pages.ProvisionPage
import org.ods.e2e.util.BaseSpec
import org.ods.e2e.util.GitUtil
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml

class ODSSpec extends BaseSpec {

    def static OPENDEVSTACK = 'OPENDEVSTACK'
    def static E2E_TEST_BRANCH = 'e2e-test-branch'
    def static E2E_TEST_FILE = 'e2e-tests.txt'
    def static E2E_TEST_QUICKSTARTER = 'e2e-test-quickstarter'

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
        given: 'We are logged in the provisioning app'
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
        project.key = String.format("${project.key}%02d", nextId)

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
        // STEP 8: Click on the Quickstarter dropdown list and select a boilerplate “Frontend implemented with Vue JS”
        //          Result: The component ID is listed: fe-vue
        // -------------------------------------------------------------------------------------------------------------
        and:
        project.components.eachWithIndex { component, index ->
            projectModifyForm.doAddQuickStarter(component.quickStarter, component.componentId, index + 1)
            projectModifyForm.addQuickStarterButton.click()
        }
        report("step 8 - add quickstarter")

        // -------------------------------------------------------------------------------------------------------------
        // STEP 9: Click on Start Provision
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
            report('Status after Quickstarters Addition')
        }

        then: 'Quickstarter was added'
        simulate ? true : $("#resProject.alert-success")
        report("step 9 - quick starter provisioned")

        // -------------------------------------------------------------------------------------------------------------
        // STEP 10: Go to bitbucket into the above named project and check for your component repository
        // with the name you provided.
        //          Result: The content of the components are available.
        // -------------------------------------------------------------------------------------------------------------
        when: 'Visit Bitbucket'
        baseUrl = baseUrlBitbucket
        to ProjectPage, project.key

        then: 'We are in the project page'
        currentUrl.endsWith("projects/${project.key}/")

        when: 'We visit one repository'
        def repositories = getRepositoriesInfo()

        then: 'The repositories exits for each component'
        project.components.each { component ->
            assert repositories.findAll { repository -> repository.name.toLowerCase() == (project.key + '-' + component.componentId).toLowerCase() }.size() == 1
        }
        report("step 10 - components repositories in bitbucket")

        // -------------------------------------------------------------------------------------------------------------
        // STEP 11: Go to your project’s Jenkins and locate the provision job of the component
        // with the name you provided.
        //          Result: Instance can be found and green (successful)
        // -------------------------------------------------------------------------------------------------------------
        when:
        baseUrl = getJenkinsBaseUrl(project.key)

        and:
        doJenkinsLoginProcess()

        then: 'The project folder exists'
        assert $("#job_${project.key.toLowerCase()}-cd")

        when: 'Visit the jobs'
        to JenkinsJobFolderPage, project.key

        and:
        project.components.each { component ->
            component.jobs = getComponentJobs(project.key, component.componentId)
        }

        then: 'The component startup jobs finished succesfully'
        waitFor('verySlow') {
            project.components.each {
                component ->
                    getComponentJobs(project.key, component.componentId).jobs.find {
                        job -> job.value.odsStartupComponentJob && job.value.success
                    }
            }
        }

        report("step 11 - provision job of the component")

        // -------------------------------------------------------------------------------------------------------------
        // STEP 12: Go to your project’s Jenkins and locate the build job of the component named *component-name*-master
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
        and: 'Checks that exists jobs that are not qs startup jobs for the components'
        waitFor('verySlow') {
            project.components.each {
                component ->
                    getComponentJobs(project.key, component.componentId).jobs.find {
                        job -> !job.value.odsStartupComponentJob && job.value.success
                    }
            }
        }

        report("step 12 - build job of the component")
    }

    /**
     * Test Objective: To provide evidences that a new boilerplate software updates can be easily added to ODS,
     * and be available for use.
     *
     * Prerequisites: Be logged in as Provisioning Application administrator, have a OpenShift project, have access
     * to the console/terminal logs of Jenkins, Nexus and SonarQube and have administrator access to the Bitbucket
     * repositories including the OpenDevStack one.
     */
    def "FT_01_002"() {
        // STEP 1: Go to Bitbucket ODS project – into repository ods-quickstarters
        //         Result: Project and repository available
        given:
        baseUrl = baseUrlBitbucket

        when:
        to LoginPage

        and: 'we do login'
        doLogin()

        then: 'we are at the Dashboard'
        at DashboardPage

        when: 'Visit project'
        to RepositoryPage, OPENDEVSTACK, 'ods-quickstarters'

        then: 'Project and repository available'
        currentUrl.endsWith('OPENDEVSTACK/repos/ods-quickstarters/browse')
        report('step 1 - Project and repository available')

        // STEP 2: Pick one quickstarter repository and create a branch from master – add, and commit a file into /files
        //         Result: Repository with master branch found, branch modified and committed/pushed
        when: 'Checkout project'
        def quickstarter = 'fe-angular'
        def gitRepository = GitUtil.cloneRepository(OPENDEVSTACK, 'ods-quickstarters', baseBranchBitbucket, false)

        and: 'Create a branch'
        GitUtil.checkout(gitRepository, E2E_TEST_BRANCH, true)

        and: 'Add a file into /files'
        def directory = gitRepository.repository.getWorkTree()
        def filesPath = "$quickstarter/files".toString()
        def testFilePath = "$filesPath/$E2E_TEST_FILE"
        new File("$directory/$testFilePath").text = 'Test file for FT_01_002'

        and: 'Commit and push file'
        GitUtil.add(gitRepository, testFilePath)
        GitUtil.commitAddAll(gitRepository, 'Added test file')
        GitUtil.push(gitRepository)

        then: 'Push successful if no exception'
        true
        report("step 2 - Repository with master branch found, branch modified and committed/pushed.")

        // STEP 3: Go to Openshift prov-test project and open available config maps
        //         Result: Configuration maps available – namely application.properties
        when: 'Visit Openshift'
        baseUrl = baseUrlOpenshift

        and: 'and login in Openshift'
        doOpenshiftLoginProcess()

        and: 'Visit the config maps of the provisioning application'
        to ConsoleResourcesConfigMaps, provisioningAppProject

        and: 'Retrieve the configMaps'
        def configMaps = getConfigMaps()

        then:
        configMaps.findAll { configMap -> configMap.name == quickstartersConfigMap }.size() == 1
        report("step 3 - Configuration maps available – namely application.properties")

        // STEP 4: In the application.properties config map copy the configuration lines from the quickstarter
        //         you pick from the step 2
        //         Result: Existing Quickstarter / boilerplace configuration available

        when: 'Read configMap'
        def client = OpenShiftClient.connect(provisioningAppProject)
        def configMap = client.getConfigMap(quickstartersConfigMap)
        def configMapData = configMap.getData()

        and: 'Read the properties'
        def propertyFile = configMapData.properties
        def propertyBackup = propertyFile
        def properties = new Properties()
        properties.load(new StringReader(propertyFile))

        and: 'Obtain existing properties'
        def tmpProps = properties.findAll {
            key, value ->
                key.startsWith("jenkinspipeline.quickstarter.$quickstarter.")
        }

        then:
        !tmpProps.isEmpty()
        report("step 4 - Existing Quickstarter / boilerplace configuration available")

        // STEP 5: Replace the key with a name of your choice and add the branch identifier and Jenkins file path as
        //         documented in the application.properties – click save
        //         Result: Config map can be saved

        when: 'Add quickstarter properties'
        tmpProps = tmpProps.collectEntries {
            key, value ->
                def concreteProperty = key.substring("jenkinspipeline.quickstarter.$quickstarter.".length())
                def newKey = "jenkinspipeline.quickstarter.${E2E_TEST_QUICKSTARTER}.${concreteProperty}".toString()
                [(newKey): value]
        }
        properties.putAll(tmpProps)
        properties.setProperty("jenkinspipeline.quickstarter.${E2E_TEST_QUICKSTARTER}.branch".toString(), E2E_TEST_BRANCH)
        properties.setProperty("jenkinspipeline.quickstarter.${E2E_TEST_QUICKSTARTER}.jenkinsfile".toString(),
                "$E2E_TEST_QUICKSTARTER/Jenkinsfile".toString())

        and: 'Update config map'
        def sw = new StringWriter()
        properties.store(sw, null)
        propertyFile = sw.toString()
        configMapData.properties = propertyFile
        client.modifyConfigMap(configMap, configMapData)

        and: 'Save modified config map'
        configMap = client.update(configMap)

        and: 'Retrieve the config map again'
        configMapData = configMap.getData()
        def newContent = configMapData.get('properties')

        then: 'We correctly retrieve the updated data'
        newContent == propertyFile
        report("step 5 - Config map can be saved")

        // STEP 6: From the console or thru OC cli - redeploy the provision application in prov-test
        //         Result: New deployment of provision app shown in console and new pod available

        when: 'Get deployments'
        def lastVersion = client.getLastDeploymentVersion(provisioningAppDeployCfg)

        and: 'Redeploy the provisioning app'
        client.deploy(provisioningAppDeployCfg)

        and: 'Wait for deployment'
        def newVersion = client.waitForDeployment(provisioningAppDeployCfg, lastVersion)
        sleep(30000)

        then: 'New deployment exists'
        newVersion > lastVersion
        report("step 6 - New deployment of provision app shown in console and new pod available.")

        // STEP 7: Login and pick modify existing project / and locate the new quickstarter
        //         Result: New quickstarter available in the list in provision application

        when: 'We are logged in the provissioning app'
        baseUrl = baseUrlProvisioningApp
        to ProvAppLoginPage
        doLoginProcess()

        and: 'We are in the provisioning page'
        def project = projects.default
        to ProvisionPage

        and: 'We have selected modify project'
        provisionOptionChooser.doSelectModifyProject()

        then: 'The new quickstarter available in the list'
        projectModifyForm.getProjects().find {
            entry -> entry.key == project.key
        }
        report("step 7 - New quickstarter available in the list in provision application.")


        // STEP 8: Go to bitbucket, locate the new repository and locate the file added in step 2
        //         Result: Repository and file available

        when: 'Visit bitbucket to grab evidences of adding files'
        baseUrl = baseUrlBitbucket
        to RepositoryPage, OPENDEVSTACK, 'ods-quickstarters', filesPath, [at: E2E_TEST_BRANCH]

        and: 'Get the existing files'
        def files = getFiles()

        then: 'Test file exists'
        files.find { file -> file.name == E2E_TEST_FILE }
        report("step 8 - Repository and file available.")

        cleanup: 'Restore original contents of the config map'
        configMapData.put('properties', propertyBackup)
        client.modifyConfigMap(configMap, configMapData)
        client.update(configMap)

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
        def gitRepository = GitUtil.cloneRepository(projects.default.key, releaseManagerComponent.componentId)
        def repositoryFolder = gitRepository.repository.getWorkTree()

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
        GitUtil.commitAddAll(gitRepository,'New component added')

        and: 'Push it to the repository'
        GitUtil.push(gitRepository,'origin')

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
