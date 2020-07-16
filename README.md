# Automated test cases for ODS

## Description
Suite of test cases for *OpenDevStack*.
The tests are prepared to work with Chrome and Chrome Headless. Currently the Provisioning app is not prepared to work with Firefox, but the driver is included in the tests for future releases.
You can have a look at the `build.gradle` and `src/test/resources/GebConfig.groovy` files to check the current configuration of Geb.


## Usage
You need to set several environment variables in order to make this work, as it is intended to use in a container / pod lately.

| Variable                | Value                            | Description                                                                  |
|-------------------------|----------------------------------|----------------------------------------------------------------------------- |
| QUICKSTARTERS_CONFIGMAP | quickstarters.properties         | ConfigMap containing quickstarter configuration.                             |
| PROV_APP_PROJECT        | ods                              | Id of the OpenShift project of the provisioning app                          |
| PROV_APP_DEPLOY_CFG     | ods-provisioning-app             | Name of the deployment config of the provisioning app                        |
| PROV_APP_NAME           | prov-app-ods                     | Name of the provisioning app, as it appears in the public URL.               |
| PROV_APP_USER           | openshift                        | Provisioning app user name                                                   |
| PROV_APP_PASSWORD       | openshift                        | Provisioning app password                                                    |
| ATLASSIAN_USER          | openshift                        | Atlassian user name                                                          |
| ATLASSIAN_PASSWORD      | openshift                        | Atlassian password                                                           |
| JENKINS_USER            | developer                        | Jenkins user name                                                            |
| JENKINS_PASSWORD        | any                              | Jenkins password                                                             |
| OPENSHIFT_USER          | developer                        | Openshift user name                                                          |
| OPENSHIFT_PASSWORD      | any                              | Openshift password                                                           |
| JIRA_URL                | http://jira.odsbox.lan:8080/     | Url of Jira instance related with the prov app                               |
| OPENSHIFT_PROJECT       | edpp                             | project identifier for prov app in the preliminary tests(jira tests)         |
| OPENSHIFT_PUBLIC_HOST   | ocp.odsbox.lan                   | host where we can locate the prov app                                        |
| OPENSHIFT_CLUSTER       | https://ocp.odsbox.lan:8443/     | URL of the Openshift Cluster                                                 |
| BITBUCKET_URL           | http://bitbucket.odsbox.lan:7990 | Url of Bitbucket instance                                                    |
| BITBUCKET_BASE_BRANCH   | master                           | Branch of the HEAD branch of the OpenShift project. Not always master.       |
| SIMULATE                | false                            | Specify (true or false)  if we skip the creation of project, components, etc |

Depending on the way you'll run the tests, there are 2 files that will help you to prepare those variables:

### Using make

If you execute the test using the provided ```Makefile```, you can use the existing ```.env``` that will be imported by *make* to setup the environment.   

To run the tests you'll only need to execute
```sh
# Default test target with chrome headlesss
$ make test

# Using Chrome
$ make testChrome
``` 
### Running manually the tests
If you want to have more control and launch manually using the gradle wrapper, we provide you a sample script to set the variables, that in bash you can use as follow:
```sh
$ . ./set-env.sh
```


The following commands will launch the tests with the individual browsers:

```sh
# Headless tests, to use chrome without displaying the browser.
$ ./gradlew chromeHeadlessTest --tests "org.ods.e2e.ODSSpec"

# A single test spec
$ ./gradlew chromeTest --tests "org.ods.e2e.ODSSpec"

# A   specific test
$ ./gradlew chromeTest --tests "org.ods.e2e.ODSSpec.FT_01_001"
```

Replace `./gradlew` with `gradlew.bat` in the above examples if you're on Windows.

>Note: Test are prepared to be executed sequentially to reuse information generated from previous test. 