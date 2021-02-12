package org.ods.e2e.util

import groovyjarjarcommonscli.MissingArgumentException
import org.ods.e2e.openshift.client.OpenShiftClient
import org.ods.e2e.provapp.util.ProvAppClient

class ProjectHelper {

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
        println generatedProjectData

        def projectKey = generatedProjectData.projectKey.toLowerCase()
        println("Project generated: $projectKey")

        // Wait until the project jenkins instance is running
        def openshiftClient = OpenShiftClient.connect("$projectKey-cd")
        openshiftClient.waitForDeployment('jenkins', 0)

        // Add components
        openshiftClient = OpenShiftClient.connect("$projectKey-dev")
        project?.components.each { component ->
            def data = provAppClient.addComponentsToProject(
                    [projectKey   : projectKey,
                     quickstarters: [component]
                    ]
            )
            openshiftClient.waitForDeployment(component.component_id, 0)
        }
        return generatedProjectData
    }
}
