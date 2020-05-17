package jenkins

import geb.spock.GebReportingSpec
import util.SpecHelper

class JenkinsSpec extends GebReportingSpec {

    static Properties applicationProperties = new SpecHelper().getApplicationProperties()

    def setup() {
        baseUrl = applicationProperties."config.jenkins.url"
    }


    def "can login to Jenkins"() {

    }

}
