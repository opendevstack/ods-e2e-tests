package org.ods.e2e.jira.pages

class CreateTechnicalSpecificationTaskIssuePage extends CreateIssuePage {
    static systemDesignSpecificationId = 'customfield_11912'
    static softwareDesignSpecificationId = 'customfield_11913'

    static at = { browser.currentUrl.contains('CreateIssue') && $("#issue-create-issue-type").text() == 'Technical Specification Task' }

    static content = {
        systemDesignSpecification(wait: true, required: true) { $("#$systemDesignSpecificationId-wiki-edit > textarea") }
        systemDesignSpecificationTextLink(wait: true, required: true) { $("div.jira-wikifield", "field-id":systemDesignSpecificationId) }

        softwareDesignSpecification(wait: true, required: true) { $("#$softwareDesignSpecificationId-wiki-edit > textarea") }
        softwareDesignSpecificationTextLink(wait: true, required: true) { $("div.jira-wikifield", "field-id":softwareDesignSpecificationId) }
    }

    def createIssue(techSpecification, spec) {
        summary = techSpecification.summary ?: ""
        descriptionTextLink.click()
        description = techSpecification.description ?: ""
        componentsTextArea = techSpecification.component ?: ""
        spec.report("Fill data $techSpecification.summary 1")
        systemDesignSpecificationTextLink.click()
        systemDesignSpecification = techSpecification.systemDesignSpecification
        softwareDesignSpecificationTextLink.click()
        softwareDesignSpecification = techSpecification.softwareDesignSpecification
        spec.report("Fill data $techSpecification.summary 2")
        issueCreateButton.click()
    }

}
