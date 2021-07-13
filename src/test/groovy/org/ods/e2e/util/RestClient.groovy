package org.ods.e2e.util

import kong.unirest.GetRequest
import kong.unirest.HttpRequestWithBody
import kong.unirest.Unirest

class RestClient extends Unirest {
    def static specHelper = new SpecHelper()

    @Override
    static GetRequest get(String url) {
        configConnection()
        return Unirest.get(url)
    }

    @Override
    static HttpRequestWithBody post(String url) {
        configConnection()
        return Unirest.post(url)
    }

    @Override
    static HttpRequestWithBody delete(String url) {
        configConnection()
        return Unirest.delete(url)
    }

    @Override
    static HttpRequestWithBody patch(String url) {
        configConnection()
        return Unirest.patch(url)
    }

    @Override
    static HttpRequestWithBody put(String url) {
        configConnection()
        return Unirest.put(url)
    }

    @Override
    static HttpRequestWithBody request(String method, String url) {
        configConnection()
        return Unirest.request(method, url)
    }

    def static configConnection(){
        String proxyHost = specHelper.applicationProperties."config.proxy.host"
        String proxyPort = specHelper.applicationProperties."config.proxy.port"
        if(proxyHost && proxyPort) {
            config().reset()
            config().proxy(proxyHost, proxyPort.toInteger())
        }
        config().verifySsl(false)
    }
}
