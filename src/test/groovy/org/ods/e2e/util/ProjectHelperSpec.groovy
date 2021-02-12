package org.ods.e2e.util

import spock.lang.Specification

class ProjectHelperSpec extends Specification {

    def "create project with components"() {
        given:
        def projectDefinition = [
                projectType         : 'EDProject',
                specialPermissionSet: true,
                projectAdminUser    : 'project-admin',
                projectAdminGroup   : 'project-admins',
                projectUserGroup    : 'project-team-members',
                projectReadonlyGroup: 'project-readonly-users',
        ]

        def components = [
                [component_type: 'docker-plain', component_id: 'docker-plain-one'],
                [component_type: 'docker-plain', component_id: 'docker-plain-two'],
        ]

        def project = [projectDefinition: projectDefinition,
                       components       : components]

        def projectHelper = new ProjectHelper()

        when:
        def projectKey = projectHelper.provisionProject(project)

        then:
        projectKey

    }
}
