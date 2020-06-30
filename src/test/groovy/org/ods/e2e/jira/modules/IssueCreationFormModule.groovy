package org.ods.e2e.jira.modules

import geb.Module

class IssueCreationFormModule extends Module {

    static content = {
        summary { $("#summary") }
        description { $ "#description" }
        descriptionTextLink(wait: true, required: true) { $("div.jira-wikifield", "field-id": "description") }
        componentsTextArea { $("#components-textarea") }
        componentsSelectIcon { $("#components-multi-select > .icon") }
        issueCreateButton { $('#create-issue-submit') }
    }
}
