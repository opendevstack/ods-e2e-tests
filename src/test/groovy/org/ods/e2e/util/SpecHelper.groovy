package org.ods.e2e.util

import groovy.json.JsonSlurperClassic
import kong.unirest.Unirest

import java.util.regex.Matcher
import java.util.regex.Pattern

class SpecHelper {

    static Properties properties

    public Properties getApplicationProperties() {

        if (properties == null) {
            properties = initProperties()
        }

        return properties
    }

    private Properties initProperties() {
        def env = System.getenv()

        def properties = new Properties()
        this.getClass().getResource('/application.properties').withInputStream {
            properties.load(it)
        }

        def raiseError = false
        properties.each { key, value ->
            def matcher = value =~ /\$\{(.*?)\}/
            if (matcher.find()) {
                matcher.each { match ->
                    def nameToReplace = match[0]
                    def valueToReplace = env[match[1]]
                    if (!valueToReplace) {
                        println "ERROR: Missing properties in configuration $match[0]"
                        raiseError = true
                    } else {
                        value = value.replaceAll(Pattern.quote(nameToReplace), Matcher.quoteReplacement(valueToReplace))
                        properties[key] = value
                    }
                }
            }
        }
        if (raiseError) {
            throw IllegalStateException("ERROR: Missing properties in configuration $match[0]")
        }

        return properties
    }

    public List getJiraFieldsMetadata(String projectKey) {
        if (!projectKey?.trim()) {
            throw new IllegalArgumentException("Error: unable to get fields metadata from Jira. 'projectKey' is undefined.")
        }

        Unirest.config().verifySsl(false);

        def response = Unirest.get(applicationProperties."config.atlassian.jira.url" + "/rest/api/2/issue/createmeta?expand=projects.issuetypes.fields&projectKeys={projectKey}")
                .routeParam("projectKey", projectKey.toUpperCase())
                .basicAuth(applicationProperties."config.atlassian.user.name", applicationProperties."config.atlassian.user.password")
                .header("Accept", "application/json")
                .asString()

        response.ifFailure {
            def message = "Error: unable to get fields metadata from Jira. Jira responded with code: '${response.getStatus()}' and message: '${response.getBody()}'."

            if (response.getStatus() == 404) {
                message = "Error: unable to get fields metadata from Jira. Jira could not be found at: " + applicationProperties."config.atlassian.jira.url"
            }

            throw new RuntimeException(message)
        }

        return new JsonSlurperClassic().parseText(response.getBody()).projects.first().issuetypes.collect { issueType ->
            [
                    issueType: issueType.name,
                    fields   : issueType.fields.collect { field ->
                        [
                                name: field.value.name,
                                id  : field.value.fieldId
                        ]
                    }
            ]
        }
    }

    public static String getFieldId(List fields, String issueType, String fieldName) {
        Map issue = fields.find { it.issueType == issueType }
        Map field = issue.fields.find { it.name == fieldName }

        return field.id
    }

    public List getJiraTransitionsMetadata(String issueKey) {
        if (!issueKey?.trim()) {
            throw new IllegalArgumentException("Error: unable to get transitions metadata from Jira. 'issueKey' is undefined.")
        }

        Unirest.config().verifySsl(false);

        def response = Unirest.get(applicationProperties."config.atlassian.jira.url" + "/rest/api/2/issue/{issueKey}/transitions")
                .routeParam("issueKey", issueKey.toUpperCase())
                .basicAuth(applicationProperties."config.atlassian.user.name", applicationProperties."config.atlassian.user.password")
                .header("Accept", "application/json")
                .asString()

        response.ifFailure {
            def message = "Error: unable to get transitions metadata from Jira. Jira responded with code: '${response.getStatus()}' and message: '${response.getBody()}'."

            if (response.getStatus() == 404) {
                message = "Error: unable to get transitions metadata from Jira. Jira could not be found at: " + applicationProperties."config.atlassian.jira.url"
            }

            throw new RuntimeException(message)
        }

        return new JsonSlurperClassic().parseText(response.getBody()).transitions.collect { transition ->
            [
                    id  : transition.id,
                    name: transition.name
            ]
        }
    }

    public String getTransitionId(List transitions, String name) {
        Map transition = transitions.find { it.name.toLowerCase() == name.toLowerCase() }

        return "action_id_" + transition?.id
    }

    public String getTransitionId(String issueKey, String name) {
        return getTransitionId(getJiraTransitionsMetadata(issueKey), name)
    }

    public String getUser() {
        return getApplicationProperties()."config.atlassian.user.name"
    }

    public String getPassword() {
        return getApplicationProperties()."config.atlassian.user.password"
    }

    public String getBaseURL() {
        return getApplicationProperties()."config.atlassian.jira.url"
    }
}
