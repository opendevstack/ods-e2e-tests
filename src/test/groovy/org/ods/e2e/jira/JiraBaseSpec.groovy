package org.ods.e2e.jira

import org.ods.e2e.util.BaseSpec

class JiraBaseSpec extends BaseSpec {

    String projectName
    String componentName

    def setup() {
        baseUrl = baseUrlJira
        projectName = applicationProperties."config.project.key".toString().toUpperCase()
        componentName = 'demo-app-front-end'
    }
}
