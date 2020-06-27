package org.ods.e2e.jira.pages

import org.ods.e2e.jira.modules.IssueCreationDialogModule

class IssueCreationDialogBasePage extends BasePage {

    static content = {
        issueCreationDialog(wait: true, required: false) { module (new IssueCreationDialogModule(fields: fields)) }
    }
}
