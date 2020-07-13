# Automated test cases for ODS

## Description
Suite of test cases for *OpenDevStack*.
The tests are prepared to work with Chrome and Chrome Headless. Currently the Provisioning app is not prepared to work with Firefox, but the driver is included in the tests for future releases.
You can have a look at the `build.gradle` and `src/test/resources/GebConfig.groovy` files to check the current configuration of Geb.


## Usage
You need to set several environment variables in order to make this work, as it is intended to use in a container / pod lately.

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
| OPENSHIFT_PUBLIC_HOST | .ocp.odsbox.lan                  | host where we can locate the prov app                                        |
| OPENSHIFT_CLUSTER     | https://ocp.odsbox.lan:8443/     | URL of the Openshift Cluster                                                 |
| BITBUCKET_URL         | http://bitbucket.odsbox.lan:7990 | Url of Bitbucket instance                                                    |
| SIMULATE              | false                            | Specify (true or false)  if we skip the creation of project, components, etc |

### Environment variables setup
To get the information needed to run the tests there are 3 steps that override the previous one:
1. If the test project is co-located with the ods-configuration folder, the process will retrieve the values that exists in the ods-core.env file to obtain the urls of Openshift, Jira, Bitbucket.
2. There could be a .env file in the root folder of the test repository that can provide the other variables needed, as passwords, if it will simulate the creation of the projects/components, etc.   
   The values we obtained in the previous step can be override in this file, so you can for example set the JIRA_URL variable in this file too.   
   In the case that the test project is in a computer different from the one used to install ODS this file will be the main source of information to setup the tests.
3. The third and last way is to set all these parameters in environment variables. These ones will override the previous values and are the last point to set the parameters for the tests.

### Running the tests
Depending on the way you'll run the tests, there are 2 files that will help you to prepare those variables:

#### Using make

If you execute the test using the provided ```Makefile```, you can use the existing ```.env``` that will be imported by *make* to setup the environment.   

To run the tests you'll only need to execute
```sh
# Default test target with chrome headlesss
$ make test

# Using Chrome
$ make testChrome
``` 
#### Running manually the tests
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
