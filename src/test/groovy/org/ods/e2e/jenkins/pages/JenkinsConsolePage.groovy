package org.ods.e2e.jenkins.pages

import geb.Page

class JenkinsConsolePage extends Page {
    static url = "/"
    static at = { title.contains('Jenkins') }

    static content = {
        jobsTable { $("#projectstatus") }
        activateAutorefreshLink(required: false) { $("#right-top-nav > div > a", href: contains('true')) }
        deActivateAutorefreshLink(required: false) { $("#right-top-nav > div > a", href: contains('false')) }
    }

    def getComponentCreationJobExistence(def project, def component) {
        $("#job_$project-cd-ods-$component-2x")
    }

    def getAllComponentsJobs(project) {
        def projectKey = project.toLowerCase()
        jobsTable.$("tr").findAll { it.@id }.
                collectEntries {
                    [it.@id - "job_$projectKey-cd-",
                     [
                             id                    : (it.@id - "job_$projectKey-cd/") - 'ods-qs-',
                             success               : it.hasClass('job-status-blue'),
                             notBuild              : it.hasClass('job-status-nobuilt'),
                             odsStartupComponentJob: it.@id.contains('ods-qs-'),
                             running               : it.hasClass('job-status-blue-anime') || it.hasClass('job-status-red-anime'),
                     ]
                    ]
                }
    }

    def getComponentJobs(project, component) {
        def projectKey = project.toLowerCase()
        jobsTable.$("tr").findAll { it.@id.contains(component) }.
                collectEntries {
                    [it.@id - "job_$projectKey-cd-",
                     [
                             id                    : (it.@id - "job_$projectKey-cd/") - 'ods-qs-',
                             branch                : ((it.@id - "job_$projectKey-cd/") - 'ods-qs-') - "$component-",
                             success               : it.hasClass('job-status-blue'),
                             notBuild              : it.hasClass('job-status-nobuilt'),
                             odsStartupComponentJob: it.@id.contains('ods-qs-'),
                             running               : it.hasClass('job-status-blue-anime') ||
                                     it.hasClass('job-status-red-anime') ||
                                     it.hasClass('job-status-nobuilt-anime'),
                     ]
                    ]
                }
    }

    /**
     * Wait for all jobs to start, must be working at the same time
     * @param jobs
     */
    def waitJobsStart(jobs) {
        if (activateAutorefreshLink) {
            activateAutorefreshLink.click()
        }

        waitFor('extremelySlow') {
            jobs.every {
                job ->
                    $("tr", id: contains(job),
                            class: contains(~/job-status-blue-anime|job-status-red-anime|job-status-nobuilt-anime/)).size() == 1
            }
        }
        if (deActivateAutorefreshLink) deActivateAutorefreshLink.click()
    }

    /**
     * Wait for all jobs to stop, must be stopped at the same time
     * @param jobs
     */
    def waitJobsFinalize(jobs) {
        if (activateAutorefreshLink) {
            activateAutorefreshLink.click()
        }
        waitFor('extremelySlow') {
            jobs.every {
                job ->
                    $("tr", id: contains(job),
                            class: contains(~/job-status-blue-anime|job-status-red-anime|job-status-nobuilt-anime/)).size() == 0
            }
        }
        if (deActivateAutorefreshLink) deActivateAutorefreshLink.click()

    }
    /**
     * Waits until a job has finished or the timeout
     * @param job
     */
    def waitForJobTermination(job) {
        if (activateAutorefreshLink) {
            activateAutorefreshLink.click()
        }
        waitFor('extremelySlow') {
            $("tr", id: contains(job), class: contains(~/job-status-blue-anime|job-status-red-anime/)).size() != 1
        }
    }

    def triggerBuild(project) {
        to JenkinsConsoleParametrizedBuildPage, project.key, releaseManagerPipelineJob
        if (parameters) fillData(parameters)
        submitButton.click()
    }
}
