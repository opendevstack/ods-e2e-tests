# Automated test cases for ODS

## Description
Suite of test cases for OpenDevStack and the Release Manager.
The build is setup to work with Chrome, Chrome headless and html. Have a look at the `build.gradle` and the `src/test/resources/GebConfig.groovy` files.

It contains too the Firefox driver that it is not fully tested as currently the provissioning app does not work with Firefox

## Usage
You need to set several environment variables in order to make this work, as it is intended to be used in a container / pod lately.

This can be done using the script provided:
```sh
$ . ./set-env.sh
```

| Variable              | Value                            | Description                                                                  |
|-----------------------|----------------------------------|----------------------------------------------------------------------------- |
| PROV_APP_USER         | openshift                        | Provisioning app user name                                                   |
| PROV_APP_PASSWORD     | openshift                        | Provisioning app password                                                    |
| ATLASSIAN_USER        | openshift                        | Atlassian user name                                                          |
| ATLASSIAN_PASSWORD    | openshift                        | Atlassian password                                                           |
| JENKINS_USER          | developer                        | Jenkins user name                                                            |
| JENKINS_PASSWORD      | any                              | Jenkins password                                                             |
| OPENSHIFT_USER        | developer                        | Openshift user name                                                          |
| OPENSHIFT_PASSWORD    | any                              | Openshift password                                                           |
| PROV_APP_NAME         | openshift                        | Name of the deployment of the provisioning app                               |
| JIRA_URL              | http://jira.odsbox.lan:8080/     | Url of Jira instance related with the prov app                               |
| OPENSHIFT_PROJECT     | edpp                             | project identifier for prov app in the preliminary tests(jira tests)         |
| OPENSHIFT_PUBLIC_HOST | ocp.odsbox.lan                   | host where we can locate the prov app                                        |
| OPENSHIFT_CLUSTER     | https://ocp.odsbox.lan:8443/     | URL of the Openshift Cluster                                                 |
| BITBUCKET_URL         | http://bitbucket.odsbox.lan:7990 | Url of Bitbucket instance                                                    |
| SIMULATE              | false                            | Specify (true or false)  if we skip the creation of project, components, etc |

The following commands will launch the tests with the individual browsers:

```shell script
# Headless tests, to use chrome without displaying the browser.
$ ./gradlew chromeHeadlessTest --tests "org.ods.e2e.ODSSpec"


# A test spec
$ ./gradlew chromeTest --tests "org.ods.e2e.ODSSpec"
# A specific test
$ ./gradlew chromeTest --tests "org.ods.e2e.ODSSpec.FT_01_003"
```

Replace `./gradlew` with `gradlew.bat` in the above examples if you're on Windows.

## Tests being implemented
### Open Dev Stack
```ODSSpec.groovy```
- [x] FT_01_001 - Use the Provisioning app to create a project and add a Quickstarter.
- [ ] FT_01_002 - To provide evidences that a new boilerplate software updates can be easily added to ODS, and be available for use.
- [x] FT_01_003 - Orchestrate repositories and create LeVA Docs.
- [x] FT_01_005 - Prevent OpenDevStack to delete projects on Atlassian application.

