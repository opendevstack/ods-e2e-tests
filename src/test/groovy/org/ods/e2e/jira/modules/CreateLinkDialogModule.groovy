package org.ods.e2e.jira.modules

import geb.Module

/**
 * Support class to create a link in a issue
 */
class CreateLinkDialogModule extends Module {
    // Based in english language
    // TODO: Add as we need other link types
    def static linkType = [
            specifies    : 'specifies',
            isSpecifiedBy: 'is Specified By',
            isTestedBy   : 'is tested by',
            tests        : 'tests',
            mitigates    : 'mitigates',
            isMitigatedBy: 'is Mitigated By'
    ]

    static content = {
        linkTypeSelector { $("#link-type") }
        issueKeysTextArea { $("#jira-issue-keys-textarea") }
        linkButton { $("#link-jira-issue > div.buttons-container.form-footer > div > input") }
        cancelLink { $("#link-jira-issue > div.buttons-container.form-footer > div > a") }
    }
}