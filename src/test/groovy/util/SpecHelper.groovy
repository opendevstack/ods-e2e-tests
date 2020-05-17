package util

import groovyjarjarcommonscli.MissingArgumentException

import java.util.regex.Pattern

class SpecHelper {

    public Properties getApplicationProperties() {
        def env = System.getenv()

        def properties = new Properties()
        this.getClass().getResource('/application.properties').withInputStream {
            properties.load(it)
        }

        properties.each { key, value ->
            def matcher = value =~ /\$\{(.*?)\}/
            if (matcher.find()) {
                matcher.each { match ->
                    def nameToReplace = match[0]
                    def valueToReplace = env[match[1]]
                    if (!valueToReplace) {
                        println "ERROR: Missing properties in configuration $match[0]"
                        throw IllegalStateException("ERROR: Missing properties in configuration $match[0]")
                    }
                    value = value.replaceAll(Pattern.quote(nameToReplace), valueToReplace)
                    properties[key] = value
                }
            }
        }

        return properties
    }
}
