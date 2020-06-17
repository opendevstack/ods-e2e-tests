package org.ods.e2e


import org.ods.e2e.bitbucket.pages.DashboardPage
import org.ods.e2e.bitbucket.pages.LoginPage
import org.ods.e2e.bitbucket.pages.ProjectPage
import org.ods.e2e.bitbucket.pages.RepositoryPage
import org.ods.e2e.jenkins.pages.JenkinsJobFolderPage
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

    def projects = [
            default: [
                    name        : 'E2E Test Project',
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
    def releaseManagerComponent = [componentId: 'project-release-manager', quickStarter: ProvisionPage.quickstarters.releaseManager]

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
        baseUrl = baseUrlProvisioningApp
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
        // STEP 1: Login to provisioning application with administrator privileges
        given: 'We are logged in the provissioning app'
        def project = projects.default
        to ProvAppLoginPage
        doLoginProcess()

        expect: 'We are logged in the provisioning app'
        at HomePage
        report("step 1 - logged in provissioning app")

        // STEP 2: Click on the provisioning link page
        when: 'Click Provision link'
        provisionLink.click()

        then: 'We visit the provisioning page'
        at ProvisionPage
        report("step 2 - visit the provisioning page")

        // STEP 3: Click on create a new project and provide name
        when: 'We open the project creation form'
        provisionOptionChooser.doSelectCreateNewProject()

        and: 'We set the project name'
        projectCreateForm.projectName = project.name
        waitFor {
            projectCreateForm.projectKey.value()
        }
        report("step 3 - set the project name")

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
        }
        then: 'The project has been created'
        simulate ? true : $("#resProject.alert-success")
        report("step 4 - project has been created")


        // STEP 5: Go to bitbucket and search for the project with the generated project key
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
        report("step 6 - existing jenkins instance for the project")

        // STEP 7: Return to provisioning app (refresh page) and click on modify existing
        when:
        baseUrl = baseUrlProvisioningApp
        to ProvisionPage

        and:
        provisionOptionChooser.doSelectModifyProject()
        projectModifyForm.doSelectProject(project.key)
        report("step 7 - go to provissioning app")

        // STEP 8: Click on the Quickstarter dropdown list and select a boilerplate “Frontend implemented with Vue JS”
        and:
        project.components.eachWithIndex { component, index ->
            projectModifyForm.doAddQuickStarter(component.quickStarter, component.componentId, index + 1)
            projectModifyForm.addQuickStarterButton.click()
        }
        report("step 8 - add quickstarter")

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
        simulate ? true : $("#resProject.alert-success")
        report("step 9 - quick starter provisioned")

        // STEP 10: Go to bitbucket into the above named project and check for your component repository
        // with the name you provided.
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

        // STEP 11: Go to your project’s Jenkins and locate the provision job of the component
        when:
        baseUrl = getJenkinsBaseUrl(project.key)

        and:
        doJenkinsLoginProcess()

        then: 'The project folder exists'
        assert $("#job_${project.key.toLowerCase()}-cd")

        when: 'Visit the jobs'
        to JenkinsJobFolderPage, "${project.key}-cd"

        and:
        project.components.each { component ->
            component.jobs = getComponentJobs(project.key, component.componentId)
        }

        then: 'The component startup jobs finished succesfully'
        project.components.each {
            component ->
                assert component.jobs.find {
                    job -> job.value.odsStartupComponentJob && job.value.success
                }
        }
        report("step 11 - provision job of the component")

        // STEP 12: Go to your project’s Jenkins and locate the build job of the component – named component - name-master
        and: 'Checks that exists jobs that are not qs startup jobs for the components'
        project.components.each {
            component ->
                assert component.jobs.find {
                    job -> !job.value.odsStartupComponentJob
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
        def gitRepository = GitUtil.cloneRepository(OPENDEVSTACK, 'ods-quickstarters', false)

        and: 'Create a branch'
        GitUtil.checkout(gitRepository, E2E_TEST_BRANCH, true)

        and: 'Add a file into /files'
        def directory = gitRepository.repository.getWorkTree()
        new File("$directory/fe-angular/files/$E2E_TEST_FILE").text = 'Testing file for FT_01_002'

        and: 'Commit and push file'
        GitUtil.add(gitRepository, "fe-angular/files/$E2E_TEST_FILE")
        GitUtil.commitAddAll(gitRepository, 'Added test file')
        GitUtil.push(gitRepository)

        and: 'Visit bitbucket to grab evidences of adding files'
        to RepositoryPage, OPENDEVSTACK, 'ods-quickstarters', 'fe-angular/files', [at: E2E_TEST_BRANCH]

        and: 'Get the existing files'
        def files = getFiles()

        then: 'exists all the files needed for the LeVADocs templates'
        files.find { file -> file.name == E2E_TEST_FILE }
        report("step 2 - Repository with master branch found, branch modified and committed/pushed.")

        // STEP 3: Go to Openshift prov-test project and open available config maps
        //         Result: Configuration maps available – namely application.properties
        when: 'Visit Openshift'
        baseUrl = baseUrlOpenshift

        and: 'and login in Openshift'
        doOpenshiftLoginProcess()

        and: 'Visit the config maps of the provisioning application'
        to ConsoleResourcesConfigMaps, 'prov-test'

        and: 'Retrieve the configMaps'
        def configMaps = getConfigMaps()

        then:
        configMaps.findAll { configMap -> configMap.name == 'application.properties' }.size() == 1


        // STEP 4: In the application.properties config map copy the configuration lines from the quickstarter
        //         you pick from the step 2
        //         Result: Existing Quickstarter / boilerplace configuration available

        // STEP 5: Replace the key with a name of your choice and add the branch identifier and Jenkins file path as
        //         documented in the application.properties – click save
        //         Result: Config map can be saved

        // STEP 6: From the console or thru OC cli - redeploy the provision application in prov-test
        //         Result: New deployment of provision app shown in console and new pod available

        // STEP 7: Login and pick modifiy existing project / and locate the new quickstarter
        //         Result: New quickstarter available in the list in provision application

        // STEP 8: Go to bitbucket, locate the new repository and locate the file added in step 2
        //         Result: Repository and file available
    }

    /**
     * Test Objective:
     * The purpose of this test case is to demonstrate the functionality to orchestrate multiple repositories with the
     * deployment, and      * to automatically produce LeVA documentation. This test will use the Provisioning
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
     */
    def "FT_01_003"() {
        // STEP 1:	Login to provisioning app – pick the created project and provision a new component based on the release-manager quickstarter
        //          Result: With your bitbucket project a new repository with the release manager is available
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
            sleep(15000)

            waitFor {
                $(".modal-dialog").css("display") != "hidden"
                $("#resProject.alert-success")
                $("#resButton").text() == "Close"
            }
        }

        then: 'Quickstarter was added'
        simulate ? true : $("#resProject.alert-success")
        report('step 1: Quick starter added')

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

        // STEP 2:	Checkout / git clone the release manager repository and open metadata.yml
        //          Result: Repository cloned, metadata.yml is available
        when:
        def gitRepository = GitUtil.cloneRepository(projects.default.key, releaseManagerComponent.componentId)

        and:
        DumperOptions options = new DumperOptions()
        options.setPrettyFlow(true)
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK)
        Yaml parser = new Yaml(options)

        def metadataYml = parser.load(("$repositoryFolder/metadata.yml" as File).text)

        then:
        assert metadataYml

        // STEP 3:	Add a new repository section into metadata.yml pointing to the component repository created earlier (see perreq.) – commit and push
        //          Result: New build of release manager in Jenkins visible, including a new build & deployment of your component.
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
        GitUtil.commitAddAll(gitRepository, 'New component added')

        and: 'Push it to the repository'
        GitUtil.push(gitRepository, 'origin')

        // TODO: Finalize when having a working QS
        then:
        true


        // STEP 4:	Go to bitbucket opendevstack project and locate ods-document-generation-templates
        //          Result: Repository is available and within document templates for LeVa can be found.
        when: 'Visit project OPENDEVSTACK'
        to ProjectPage, OPENDEVSTACK

        then: 'We are in the project page'
        currentUrl.endsWith("projects/OPENDEVSTACK/")

        when: 'Get repositories'
        def odsRepositories = getRepositoriesInfo()

        then: 'Then ods-document-generation-templates exists'
        assert odsRepositories.findAll {
            repository -> repository.name.toLowerCase() == 'ods-document-generation-templates'
        }.size() == 1

        when:
        to RepositoryPage, OPENDEVSTACK, 'ods-document-generation-templates', 'templates', [at: 'release/v1.0']

        and: 'Get the existing files'
        def templates = getFiles()

        then: 'exists all the files needed for the LeVADocs templates'
        leVADocsTemplates.each {
            leVAtemplate -> templates.contains(leVAtemplate)
        }
        report("step 4 - Repository is available and within document templates for LeVa can be found.")

        // STEP 5:	Go to openshift – locate your project’s cd project (name-cd) and check for a running instance of Document generation service
        //          Result: Document Generation service pod is available
        when: 'Visit Openshift'
        baseUrl = baseUrlOpenshift

        and: 'and login in Openshift'
        doOpenshiftLoginProcess()

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
}
