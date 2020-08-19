@Library('ods-jenkins-shared-library@master') _

node {
    properties([[$class: 'BuildConfigProjectProperty', name: '', namespace: '', resourceVersion: '', uid: ''],
                    parameters([
                            choice(
                                   name: 'ENV'
                                 , choices: ['prod', 'dev', 'box']
                                 , description: 'Environment against which to run the E2E tests')
                          , string(
                                   name: 'ODS_PROJECT'
                                 , defaultValue: 'e2etests'
                                 , description: 'ID of the ODS project for the E2E tests')
                    ])
        ])
}

def extraVars

odsComponentPipeline(
  imageStreamTag: "${SLAVE_IMAGE}",
  projectId: "${ODS_PROJECT}",
  componentId: 'ods-e2e-test',
  branchToEnvironmentMapping: [
    'master': 'dev',
    // 'release/': 'test'
  ]
) {
    environment {
        OPENSHIFT_PROJECT="${ODS_PROJECT}-cd"
        CERT_BUNDLE_PATH='/etc/pki/ca-trust/source/anchors/ssl_truststore.pem'
        TRUSTSTORE_PATH='build/truststore.jks'
    }
    stage('Set trust stores up') {
        sh('update-ca-trust force-enable')
        sh("cp certs/ssl_truststore.pem \"${CERT_BUNDLE_PATH}\"")
        sh('update-ca-trust extract')
        sh("keytool -import -noprompt -trustcacerts -file \"${CERT_BUNDLE_PATH}\" -alias ssl_truststore -keystore \"${TRUSTSTORE_PATH}\" -storepass changeit")
    }
    stage('Run tests') {
        withEnv(readProperties(file: "env-${ENV}.properties").collect(key, value -> key + '=' + value) {
            withCredentials([[$class: 'UsernamePasswordMultiBinding'
                            , credentialsId: "${OPENSHIFT_PROJECT}-${ATLASSIAN_CREDENTIALS}"
                            , usernameVariable: 'ATLASSIAN_USER'
                            , passwordVariable: 'ATLASSIAN_PASSWORD']]) {
                withCredentials([[$class: 'UsernamePasswordMultiBinding'
                                , credentialsId: "${OPENSHIFT_PROJECT}-${JENKINS_CREDENTIALS}"
                                , usernameVariable: 'JENKINS_USER'
                                , passwordVariable: 'JENKINS_PASSWORD']]) {
                    withCredentials([[$class: 'UsernamePasswordMultiBinding'
                                    , credentialsId: "${OPENSHIFT_PROJECT}-${NEXUS_CREDENTIALS}"
                                    , usernameVariable: 'NEXUS_USER'
                                    , passwordVariable: 'NEXUS_PASSWORD']]) {
                        withCredentials([[$class: 'UsernamePasswordMultiBinding'
                                        , credentialsId: "${OPENSHIFT_PROJECT}-${OPENSHIFT_CREDENTIALS}"
                                        , usernameVariable: 'OPENSHIFT_USER'
                                        , passwordVariable: 'OPENSHIFT_PASSWORD']]) {
                            withCredentials([[$class: 'UsernamePasswordMultiBinding'
                                            , credentialsId: "${OPENSHIFT_PROJECT}-${PROV_APP_CREDENTIALS}"
                                            , usernameVariable: 'PROV_APP_USER'
                                            , passwordVariable: 'PROV_APP_PASSWORD']]) {
                                sh('./gradlew chromeHeadlessTest --tests org.ods.e2e.ODSSpec --system-prop "javax.net.ssl.trustStore=${HOME}/truststores/cacerts" --system-prop "javax.net.ssl.trustStorePassword=changeit"')
                            }
                        }
                    }
                }
            }
        }
    }
}