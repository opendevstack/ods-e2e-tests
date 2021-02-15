package org.ods.e2e.util

import kong.unirest.Unirest
import org.ods.e2e.util.waiting.Wait

class UriHelper {
    def static DEFAULT_TIMEOUT = 60
    def static DEFAULT_INTERVAL = 5

    public static waitURLAvailable(String url, timeout = DEFAULT_TIMEOUT, interval = DEFAULT_INTERVAL) {
        Unirest.config().verifySsl(false)
        def wait = new Wait(timeout, interval)
        wait.waitFor({
            Unirest.get(url).asJson().getStatus() != 503
        })
        return Unirest.get(url).asJson().getStatus()
    }
}
