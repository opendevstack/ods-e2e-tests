package org.ods.e2e.util

import groovyjarjarcommonscli.MissingArgumentException
import org.ods.e2e.openshift.client.OpenShiftClient
import org.ods.e2e.provapp.util.ProvAppClient

class ProjectHelper {


    public static final String METADATA_YAML_TEMPLATE = '/metadata.yaml.template'

    def provisionProject(def project) {
        if (!project) {
            throw new MissingArgumentException("It's needed to provide the project definition")
        }
        if (!project.projectDefinition) {
            throw new MissingArgumentException("It's needed to provide the project definition")
        }

        // Create project
        def provAppClient = new ProvAppClient()
        def generatedProjectData = provAppClient.createRandomProject(project.projectDefinition)
        println("Generated project data: $generatedProjectData")


        def projectKey = generatedProjectData.projectKey.toLowerCase()
        println("Generated project key: $projectKey")

        // Wait until the project jenkins instance is running
        def openshiftClient = OpenShiftClient.connect("$projectKey-cd")
        openshiftClient.waitForDeployment('jenkins', 0)
        UriHelper.waitURLAvailable(generatedProjectData.platformBuildEngineUrl, 300, 10)

        // Add components
        openshiftClient = OpenShiftClient.connect("$projectKey-dev")
        project?.components.each { component ->
            def data = provAppClient.addComponentsToProject(
                    [projectKey   : projectKey,
                     quickstarters: [[component_type: component.qs, component_id: component.id]]
                    ]
            )
            if (component.type == 'ods') {
                openshiftClient.waitForDeployment(component.id, 0)
            }
        }
        return generatedProjectData
    }

    def generateMetadataYaml(bindings) {
        def text = getClass().getResourceAsStream(METADATA_YAML_TEMPLATE).text
        def engine = new groovy.text.GStringTemplateEngine()
        def template = engine.createTemplate(text).make(bindings)
    }
}
