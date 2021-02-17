package org.ods.e2e.util

import groovyjarjarcommonscli.MissingArgumentException
import org.ods.e2e.openshift.client.OpenShiftClient
import org.ods.e2e.provapp.util.ProvAppClient

class ProjectProvisionerHelper {


    public static final String METADATA_YAML_TEMPLATE = '/metadata.yaml.template'

    def provisionProject(def project) {
        if (!project) {
            throw new MissingArgumentException("It's needed to provide the project definition")
        }

        def projectDefinition = [
                projectType         : project.type,
                specialPermissionSet: project."admin-user" != null,
                projectAdminUser    : project."admin-user",
                projectAdminGroup   : project."admin-group",
                projectUserGroup    : project."user-group",
                projectReadonlyGroup: project."readonly-user-group",
        ]

        // Create project
        def provAppClient = new ProvAppClient()
        def generatedProjectData = provAppClient.createRandomProject(projectDefinition)
        println "[provisioning-ods] Generated project data: $generatedProjectData"

        def projectKey = generatedProjectData.projectKey.toLowerCase()
        project.key = generatedProjectData.projectKey
        println "[provisioning-ods] Generated project key: $project.key"

        // Wait until the project jenkins instance is running
        def openshiftClient = OpenShiftClient.connect("$projectKey-cd")
        openshiftClient.waitForDeployment('jenkins', 0)
        UriHelper.waitURLAvailable(generatedProjectData.platformBuildEngineUrl, 300, 10)

        // Add components
        addComponents(project.key, project.components)

        return generatedProjectData
    }

    /**
     * Add components to a project
     * @param projectKey - the project key
     * @param components - The components that will be added to the project
     */
    private void addComponents(projectKey, components) {
        def provAppClient = new ProvAppClient()
        def openshiftClient = OpenShiftClient.connect("$projectKey-dev")

        components.each { component ->
            println "[provisioning-ods] provisioning component: $component in project $projectKey)"
            def data = provAppClient.addComponentsToProject(
                    [projectKey   : projectKey,
                     quickstarters: [
                             [component_type: component.value.quickstarter,
                              component_id: component.value.id]
                     ]
                    ]
            )
            if (component.value.type == 'ods') {
                openshiftClient.waitForDeployment(component.value.id, 0)
            }
        }
    }

    def generateMetadataYaml(bindings) {
        def text = getClass().getResourceAsStream(METADATA_YAML_TEMPLATE).text
        def engine = new groovy.text.GStringTemplateEngine()
        def template = engine.createTemplate(text).make(bindings)
    }
}
