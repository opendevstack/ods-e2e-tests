package org.ods.e2e.jenkins.pages

import geb.Page

class JenkinsConsolePage extends Page {
    static url = "/"
    static at = { title.contains('Jenkins') }

    static content = { jobsTable { $("#projectstatus") } }

    def getComponentCreationJobExistence(def project, def component) {
        $("#job_$project-cd-ods-$component-2x")
    }

    def getAllComponentsJobs(project) {
        jobsTable.$("tr").findAll { it.@id }.
                collectEntries {
                    [it.@id - "job_$project-cd-",
                     [
                             id                    : (it.@id - "job_$project-cd-") - 'ods-qs-',
                             success               : it.hasClass('job-status-blue'),
                             notBuild              : it.hasClass('job-status-nobuilt'),
                             odsStartupComponentJob: it.@id.endsWith('-2x'),
                     ]
                    ]
                }
    }

    def getComponentJobs(project, component) {
        jobsTable.$("tr").findAll { it.@id.contains(component) }.
                collectEntries {
                    [it.@id - "job_$project-cd-",
                     [
                             id                    : (it.@id - "job_$project-cd-") - 'ods-qs-',
                             branch                : ((it.@id - "job_$project-cd-") - 'ods-qs-') - "$component-",
                             success               : it.hasClass('job-status-blue'),
                             notBuild              : it.hasClass('job-status-nobuilt'),
                             odsStartupComponentJob: it.@id.endsWith('-2x'),
                     ]
                    ]
                }
    }
}
