package org.ods.e2e.jira.pages

import geb.Page

class CreateIssuePage extends Page {
    static at = { browser.currentUrl.contains('CreateIssue') }

    static content = {
        summary { $("#summary") }
        description { $ "#description" }
        descriptionTextLink(wait: true, required: true) { $("div.jira-wikifield", "field-id":"description") }
        componentsTextArea { $("#components-textarea") }
        componentsSelectIcon { $("#components-multi-select > .icon") }
        issueCreateButton { $("#issue-create-submit") }
    }
}
