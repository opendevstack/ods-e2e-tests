package org.ods.e2e.jira.modules

import geb.Module
import org.ods.e2e.util.SpecHelper

class DocumentChapterDialogModule extends Module {
    def driver
    def fields

    static content = {
        dialog(required: true, wait:true) { $('#edit-issue-dialog') }
        edpContentEditor(wait: true, required: true) { $("#edit-issue-dialog #" + SpecHelper.getFieldId(fields, "Documentation Chapter", "EDP Content")) }
        edpHeadingNumber(wait: true, required: true) { $("#edit-issue-dialog #" + SpecHelper.getFieldId(fields, "Documentation Chapter", "EDP Heading Number")) }
        edpContent(wait: true, required: true) { $("#edit-issue-dialog #" + SpecHelper.getFieldId(fields, "Documentation Chapter", "EDP Content") + "-wiki-edit > textarea") }
        submitButton { $("#edit-issue-dialog #edit-issue-submit") }
    }
}
