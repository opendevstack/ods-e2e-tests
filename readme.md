# Example Geb and Gradle Project


## Description

This is an example of incorporating Geb into a Gradle build.

The build is setup to work with Chrome, Chrome headless and html. Have a look at the `build.gradle` and the `src/test/resources/GebConfig.groovy` files.

It contains too the Firefox driver that it is not fully tested.

## Usage

You need to set several environment variables in order to make this work, as it is intended to be used in a container / pod lately.

| Variable | Value |
|---|---|
| E2E_USER | Short name of the user |
| E2E_USER_EMAIL | email of the user (for Jira tools) |
| E2E_USER_PASSWORD | user password |
| JIRA_URL | Url of Jira instance related with the prov app |
| OPENSHIFT_PROJECT | project identifier for prov app |
| OPENSHIFT_PUBLIC_HOST | host where we can locate the prov app |
| OPENSHIFT_CLUSTER | URL of the Openshift Cluster |

The following commands will launch the tests with the individual browsers:

    ./gradlew chromeTest
    ./gradlew htmlunitTest

To run with all, you can run:

    ./gradlew test

Replace `./gradlew` with `gradlew.bat` in the above examples if you're on Windows.

## Tests done

### Provisioning App
`ProvAppSpec.groovy`
* Visit and login to the Provisioning App
* Create a project
* Add a quickstarter to a project *(Pending solve some issues with the Prov app)*

### Jira
`JiraSpec.groovy`
* Visit and login in Jira
* Visit the project summary Page
* Check for specify components

`JiraReleaseManagerSpec.groovy`
* RT_02_001: Check the correctness of calculation – risk assessment without probability of occurrence 
* RT_04_001: Test if a C-CSD document can be created. Start creating an application, use Stories in Jira, amend the Documentation chapter issues and check the issue workflows

### OpenShift
`OpenShiftSpec.groovy`
* Visit and login to Openshift
* Check the existence of namespaces for for a project (-cd, -dev and -test)








