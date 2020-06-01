package org.ods.e2e.openshift.pages

import geb.Page

class ConsoleProjectsPage extends Page {
    static url = '/console/projects'

    static content = {
        projectList(wait: true) { $(".projects-list") }
        projectTitles(wait: true) { $(".tile-target") }
    }

    def getProjects() {
        projectTitles.collect { it.text() }
    }

    def findProjects(String name) {
        projectTitles.findAll { it.text().contains(name.toLowerCase()) }
                .collect { it.text() }
    }
}
