package jira.pages

import geb.Page

class RiskAssementReportPage extends Page {

    static at = { browser.getCurrentUrl().contains('risk-assessment-report')}
    static content = {
        risksLines { $("#content > div.aui-page-panel > div > section > table > tbody > tr") }
    }

    Map getRiskAssesmentReport() {
        risksLines.collectEntries {
            def url = it.$("td:nth-child(1) > a").getAttribute('href')
            def reqSpec = it.$("td:nth-child(2) > a")
            def reqSpecUrl = reqSpec.getAttribute('href')

            [url.substring(url.lastIndexOf('/') + 1),
             [url                   : url,
              reqSpecId             : reqSpecUrl.substring(reqSpecUrl.lastIndexOf('/') + 1),
              reqSpec               : reqSpec.text(),
              reqSpecUrl            : reqSpecUrl,
              status                : it.$("td:nth-child(3)").text(),
              gxPRelevance          : it.$("td:nth-child(4)").text(),
              severityOfImpact      : it.$("td:nth-child(5)").text(),
              probabilityOfDetection: it.$("td:nth-child(6)").text(),
              riskPriority          : it.$("td:nth-child(7)").text(),
             ]
            ]
        }
    }
}
