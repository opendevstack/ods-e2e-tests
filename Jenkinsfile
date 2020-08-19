@Library('ods-jenkins-shared-library@production') _

node {
    properties([[$class: 'BuildConfigProjectProperty', name: '', namespace: '', resourceVersion: '', uid: ''],
                    parameters([
                            choice(
                                   name: 'ENV'
                                 , choices: ['dev', 'prod', 'box']
                                 , description: 'Environment against which to run the E2E tests')
                          , string(
                                   name: 'ODS_PROJECT'
                                 , defaultValue: 'edpc'
                                 , description: 'ID of the ODS project for the E2E tests')
                    ])
        ])
}

def extraVars

odsPipeline(
  image: "docker-registry.default.svc:5000/cd/jenkins-slave-python:2.x",
  projectId: "${ODS_PROJECT}",
  componentId: 'e2etests',
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
        withEnv(readProperties(file: "env-${ENV}.properties").collect {
            entry -> entry.key + '=' + entry.value
        }) {
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