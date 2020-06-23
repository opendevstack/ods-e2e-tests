package org.ods.e2e.jira.pages

import org.ods.e2e.util.SpecHelper

class CreateTechnicalSpecificationTaskIssuePage extends CreateIssuePage {

    static at = { browser.currentUrl.contains('CreateIssue') && $("#issue-create-issue-type").text() == 'Technical Specification Task' }

    static content = {
        systemDesignSpecification(wait: true, required: true) { $("#" + SpecHelper.getFieldId(fields, "Technical Specification Task", "System Design Specification") + "-wiki-edit > textarea") }
        systemDesignSpecificationTextLink(wait: true, required: true) { $("div.jira-wikifield", "field-id":SpecHelper.getFieldId(fields, "Technical Specification Task", "System Design Specification")) }

        softwareDesignSpecification(wait: true, required: true) { $("#" + new SpecHelper().getFieldId(fields, "Technical Specification Task", "Software Design Specification") + "-wiki-edit > textarea") }
        softwareDesignSpecificationTextLink(wait: true, required: true) { $("div.jira-wikifield", "field-id":SpecHelper.getFieldId(fields, "Technical Specification Task", "Software Design Specification")) }
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
