package org.ods.e2e.openshift.client


import kong.unirest.Unirest
import org.ods.e2e.util.SpecHelper

class OpenShiftHelper {
    def specHelper = new SpecHelper()

    def existsNamespace(String projectKey) {
        def namespaceFound = false
        if (!projectKey?.trim()) {
            throw new IllegalArgumentException("Error: unable to get namespace $projectKey.")
        }

        Unirest.config().verifySsl(false);
        String openshiftUrl = specHelper.applicationProperties."config.openshift.url"
        openshiftUrl = openshiftUrl.endsWith('/') ? openshiftUrl.substring(0, openshiftUrl.size() - 1) : openshiftUrl
        def response = Unirest.get("$openshiftUrl/api/v1/namespaces/{projectKey}")
                .routeParam("projectKey", projectKey.toLowerCase())
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + specHelper.applicationProperties."config.openshift.token")
                .asString()

        response.ifFailure {
            def message = "Error: unable to get the namaspace. Openshift responded with code: '${response.getStatus()}' and message: '${response.getBody()}'."
            println message
        }
        response.ifSuccess {
            namespaceFound = true
        }
        return namespaceFound
    }

    def watchPods(projectKey, pod) {
        if (!projectKey?.trim()) {
            throw new IllegalArgumentException("Error: unable to get namespace $projectKey.")
        }

        Unirest.config().verifySsl(false);
        String openshiftUrl = specHelper.applicationProperties."config.openshift.url"
        openshiftUrl = openshiftUrl.endsWith('/') ? openshiftUrl.substring(0, openshiftUrl.size() - 1) : openshiftUrl
        def response = Unirest.get("$openshiftUrl/api/v1/watch/namespaces/{projectKey}/pods")
                .routeParam("projectKey", projectKey.toLowerCase())
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + specHelper.applicationProperties."config.openshift.token")
                .asString()

        response.ifFailure {
            def message = "Error: unable to get the namaspace. Openshift responded with code: '${response.getStatus()}' and message: '${response.getBody()}'."
            println message
            return false
        }

        return true
    }
}
