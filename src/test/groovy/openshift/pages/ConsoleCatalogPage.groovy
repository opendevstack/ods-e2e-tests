package openshift.pages

import geb.Page

class ConsoleCatalogPage extends Page {
    static url = "/console/catalog"
    static at = { title == 'OpenShift Web Console' }
    static content = {
        catalogSearch { $("div.catalog-search") }
        catalog { $("div.services-items") }
        catalogSummary { $("div.catalog-projects-summary-panel") }
        viewAllLink { $("a.projects-view-all") }
    }
}
