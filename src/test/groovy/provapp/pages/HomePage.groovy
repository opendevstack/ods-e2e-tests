package provapp.pages

import geb.Page

class HomePage extends Page {
    static url = "/home"
    static at = { $("#navbar > ul > li:nth-child(1)").classes() == ['active'] }

    static content = {
        overviewLink(required: true) {$("#navbar > ul > li.active > a")}
        provisionLink(required: true) {$("#navbar > ul > li:nth-child(2) > a")}
        historyLink(required: true) {$("#navbar > ul > li:nth-child(3) > a")}
        aboutLink(required: true) {$("#navbar > ul > li:nth-child(4) > a")}
        logoutLink(required: true) { $("#navbar > ul > li:nth-child(5) > a") }
    }
}