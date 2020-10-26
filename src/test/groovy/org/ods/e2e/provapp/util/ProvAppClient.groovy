package org.ods.e2e.provapp.util

import groovy.json.JsonOutput
import kong.unirest.Unirest
import org.ods.e2e.util.SpecHelper

class ProvAppClient {
    def static DEFAULT_PROJECT_NAME_PREFIX = 'test'
    def static RND_ID_BOUND = 100000
    def static RND_ID_FORMAT = '%05d'
    def static RETRIES = 5
    static int NOT_ACCEPTABLE = 406

    def rnd = new SplittableRandom()
    def applicationProperties = new SpecHelper().getApplicationProperties()
    def user = applicationProperties.'config.provisioning-app.user.name'
    def password = applicationProperties.'config.provisioning-app.user.password'
    def provAppURL = applicationProperties.'config.provisioning.url'
    def baseURL = provAppURL + (provAppURL.endsWith('/') ? '' : '/') + 'api/v2'
    def baseProjectURL = "${baseURL}/project"

    /**
     * Checks whether another project with the same name already exists.
     *
     * @param projectName the project name to validate.
     * @return true, if the name is valid, or false, if a project with this name already exists.
     * @throws NullPointerException if you don't specify a projectName
     * @throws RuntimeException if there was an error validating the name.
     */
    def validateProject(projectName) {
        if(!projectName) {
            throw new NullPointerException('You need to specify the project name')
        }
        def url = "${baseProjectURL}/validate?projectName=${projectName}"
        Unirest.config().verifySsl(false);
        def rs = Unirest.get(url)
                .basicAuth(user, password)
                .header("Accept", "application/json")
                .asString()
        if(rs.getStatus() == NOT_ACCEPTABLE) {
            return false
        }
        rs.ifFailure {
            throw new RuntimeException("Error validating project name ${projectName}.\nResponse status: ${rs.status} ${rs.statusText}.\nResponse body: ${rs.body}")
        }
        return true
    }

    /**
     * Checks whether another project with the same key already exists.
     *
     * @param projectKey the key to validate.
     * @return true, if the key is valid, or false, if a project with this key already exists.
     * @throws NullPointerException if you don't specify a projectKey
     * @throws RuntimeException if there was an error validating the key.
     */
    def validateKey(projectKey) {
        if(!projectKey) {
            throw new NullPointerException('You need to specify the project key')
        }
        def url = "${baseProjectURL}/key/validate?projectKey=${projectKey}"
        Unirest.config().verifySsl(false);
        def rs = Unirest.get(url)
                .basicAuth(user, password)
                .header("Accept", "application/json")
                .asString()
        if(rs.getStatus() == NOT_ACCEPTABLE) {
            return false
        }
        rs.ifFailure {
            throw new RuntimeException("Error validating project key ${projectKey}.\nResponse status: ${rs.status} ${rs.statusText}.\nResponse body: ${rs.body}")
        }
        return true
    }

    /**
     * Generates a project key from the given project name.
     * No check is performed whether the key already exists or not.
     *
     * @param name the name from which to generate the project key.
     * @return the generated project key.
     * @throws NullPointerException if you don't specify a projectName
     * @throws RuntimeException if there was an error generating the key.
     */
    def generateKey(name) {
        if(!name) {
            throw new NullPointerException('You need to specify the project name')
        }
        def url = "${baseProjectURL}/key/generate?name=${name}"
        Unirest.config().verifySsl(false);
        def rs = Unirest.get(url)
                .basicAuth(user, password)
                .header("Accept", "application/json")
                .asJson()
        rs.ifFailure {
            throw new RuntimeException("Error generating key from project name ${name}.\nResponse status: ${rs.status} ${rs.statusText}.\nResponse body: ${rs.body.toPrettyString()}")
        }
        def map = rs.body.object.toMap()
        return map.projectKey
    }

    /**
     * Provision a new project.
     * This method takes named parameters.
     * The only mandatory parameter is the project name.
     * If no project key is specified, the provisioning API will generate it from the project name.
     *
     * @param onlyCheckPreconditions whether to only perform checks (true) or actually create a project (false)
     * @param projectName the mandatory name of the project.
     * @param projectKey the key of the project. Generated from the project name, if not provided.
     * @param description the project description.
     * @param projectType the project type, or default, if not specified.
     * @param cdUser a project-specific CD user name.
     * @param projectAdminUser a project-specific administrator user.
     * @param projectAdminGroup a project-specific administrator group.
     * @param projectUserGroup a project-specific non-privileged user group.
     * @param projectReadonlyGroup a project-specific read-only user group.
     * @param projectRoleForAdminGroup the role ID for the administrator group.
     * @param projectRoleForUserGroup the role ID for the user group.
     * @param projectRoleForReadonlyGroup the role ID for the read-only group.
     * @param specialPermissionSchemeId an existing permission scheme ID to reuse.
     * @param specialPermissionSet whether to use (true) specific user groups and admin user or not (false). Default false.
     * @param platformRuntime whether to create the platform runtime in OpenShift for this project. Default true.
     * @param bugtrackerSpace whether to create the project in Jira for issue tracking. Default true.
     * @return the response from the server converted to a Map
     * @throws NullPointerException if you don't specify a project name.
     * @throws IllegalArgumentException if either the name or the key for the project already exist.
     */
    def createProject(args) {
        if(!args || !args.projectName) {
            throw new NullPointerException('You need to specify at least a project name')
        }
        if(!validateProject(args.projectName)) {
            throw new AlreadyExistsException("The project name already exists: ${args.projectName}")
        }
        if(!args.projectKey) {
            args.projectKey = generateKey(args.projectName)
        }
        if(!validateKey(args.projectKey)) {
            throw new AlreadyExistsException("The project key aleady exists: ${args.projectKey}")
        }
        def url = baseProjectURL
        if(args.onllyCheckPreconditions) {
            url = "${url}?onlyCheckPreconditions=TRUE"
        }
        Unirest.config().verifySsl(false);
        def rs = Unirest.post(url)
                .basicAuth(user, password)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .body(JsonOutput.toJson(args))
                .asJson()
        rs.ifFailure {
            throw new RuntimeException("Error creating project with name ${args.projectName} and key ${args.projectKey}.\nResponse status: ${rs.status} ${rs.statusText}.\nResponse body: ${rs.body.toPrettyString()}")
        }
        return rs.body.object.toMap()
    }

    /**
     * Provision a new project with random name and key.
     * This method takes named parameters.
     * The only mandatory parameter is the project name prefix.
     * If no project name prefix is specified, it defaults to 'test'.
     * This method generates random name and key by appending the same 5-digit random number to the corresponding prefixes.
     * If no project key is specified, it will be generated by the provisioning API from the generated project name.
     *
     * @param onlyCheckPreconditions whether to only perform checks (true) or actually create a project (false)
     * @param projectName the mandatory project name prefix for the project.
     * @param projectKey the key prefix for the project, or projectName, if not provided.
     * @param description the project description.
     * @param projectType the project type, or default, if not specified.
     * @param cdUser a project-specific CD user name.
     * @param projectAdminUser a project-specific administrator user.
     * @param projectAdminGroup a project-specific administrator group.
     * @param projectUserGroup a project-specific non-privileged user group.
     * @param projectReadonlyGroup a project-specific read-only user group.
     * @param projectRoleForAdminGroup the role ID for the administrator group.
     * @param projectRoleForUserGroup the role ID for the user group.
     * @param projectRoleForReadonlyGroup the role ID for the read-only group.
     * @param specialPermissionSchemeId an existing permission scheme ID to reuse.
     * @param specialPermissionSet whether to use (true) specific user groups and admin user or not (false). Default false.
     * @param platformRuntime whether to create the platform runtime in OpenShift for this project. Default true.
     * @param bugtrackerSpace whether to create the project in Jira for issue tracking. Default true.
     * @return the response from the server converted to a Map
     * @throws NullPointerException if you don't specify a project name.
     * @throws RuntimeException if it wasn't possible to generate unique key and name values for the project.
     */
    def createRandomProject(args) {
        if(!args) {
            args = [:]
        }
        if(!args.projectName) {
            args.projectName = DEFAULT_PROJECT_NAME_PREFIX
        }
        args.projectName = args.projectName.trim()
        for(int i = 0; i < RETRIES; i++) {
            def id = generateRndId()
            args.projectName += id
            if(args.projectKey) {
                args.projectKey += id
            }
            try {
                return createProject(args)
            } catch(AlreadyExistsException e) {
            }
        }
        throw new RuntimeException("Could not generate unique name and key for the project for the prefixes ${args.projectName} and ${args.projectKey}")
    }

    /**
     * Generates a random ID betwenn 0, inclusive, and <code>RND_ID_BOUND</code>, exclusive and formats it using <code>RND_ID_FORMAT</code> pattern.
     *
     * @return a string id generated with default parameters.
     */
    def generateRndId() {
        return generateRndId(RND_ID_BOUND, RND_ID_FORMAT)
    }

    /**
     * Generates a random ID betwenn 0, inclusive, and <code>bound</code>, exclusive and formats it using <code>format</code> pattern.
     *
     * @param bound an integer bound for the generated random values.
     * @param format a string with the format pattern to generated the string ID from the random integer value.
     * @return a string id generated with the given parameters.
     */
    def generateRndId(bound, format) {
        def id = rnd.nextInt(bound)
        return String.format( format, id )
    }

    private class AlreadyExistsException extends IllegalArgumentException {
        AlreadyExistsException() {
            super()
        }

        AlreadyExistsException(String s) {
            super(s)
        }

        AlreadyExistsException(String message, Throwable cause) {
            super(message, cause)
        }

        AlreadyExistsException(Throwable cause) {
            super(cause)
        }
    }
}
