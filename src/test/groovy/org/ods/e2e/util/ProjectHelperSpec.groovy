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
                [qs: 'docker-plain', type: 'ods', id: 'docker-plain-one'],
                [qs: 'docker-plain', type: 'ods', id: 'docker-plain-two'],
                [qs: 'release-manager', type: 'release-manager', id: 'relman'],
        ]

        def project = [projectDefinition: projectDefinition,
                       components       : components]

        def projectHelper = new ProjectProvisionerHelper()

        when:
        def projectKey = projectHelper.provisionProject(project)

        then:
        projectKey
    }

    def "jenkins jellper"() {
        def status
        when:
        def jh = new UriHelper()
        status = jh.waitURLAvailable('https://jenkins-tes99-cd.ocp.odsbox.lan')
        then:
        status == 200
    }
}
