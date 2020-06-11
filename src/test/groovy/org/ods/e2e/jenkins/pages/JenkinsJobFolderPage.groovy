package org.ods.e2e.jenkins.pages

class JenkinsJobFolderPage extends JenkinsConsolePage {

    static url = '/job/'

    /**
     * Adapt the url to get to the jenkins job/jobfolder page
     *     https://jenkins-url.am.boehringer.com/job/e2et3-cd/
     * @param args must contain 1 arg, folderKey
     */
    String convertToPath(Object[] args) {
        def folder = args[0].toString().toLowerCase()
        args ? "$folder/" : ''
    }
}
