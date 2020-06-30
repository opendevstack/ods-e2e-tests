package org.ods.e2e.jira.pages

import geb.Browser
import geb.Page
import org.ods.e2e.util.SpecHelper

class BasePage extends Page {
    protected List fields = []

    @Override
    Page init(Browser browser) {
        fields = new SpecHelper().getJiraFieldsMetadata(new SpecHelper().getApplicationProperties()."config.project.key")

        return super.init(browser)
    }

}
