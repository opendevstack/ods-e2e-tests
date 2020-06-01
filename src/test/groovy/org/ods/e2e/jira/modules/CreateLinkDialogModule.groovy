package org.ods.e2e.jira.modules

import geb.Module

/**
 * Support class to create a link in a issue
 */
class CreateLinkDialogModule extends Module {
    // TODO: Has to change depending the language
    // TODO: Add as we need other link types
    def static linkType = [
            specifies: 'specifies',
    ]

    static content = {
        linkTypeSelector { $("#link-type") }
        issueKeysTextArea { $("#jira-issue-keys-textarea") }
        linkButton { $("#link-jira-issue > div.buttons-container.form-footer > div > input") }
        cancelLink { $("#link-jira-issue > div.buttons-container.form-footer > div > a") }
    }
}