package org.ods.e2e.openshift.client

import com.openshift.internal.util.JBossDmrExtentions
import com.openshift.restclient.ClientBuilder
import com.openshift.restclient.IClient
import com.openshift.restclient.IOpenShiftWatchListener
import com.openshift.restclient.ResourceKind
import com.openshift.restclient.capability.resources.IDeploymentTriggerable
import com.openshift.restclient.model.IConfigMap
import com.openshift.restclient.model.IDeploymentConfig
import com.openshift.restclient.model.IResource
import org.ods.e2e.util.SpecHelper

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class OpenShiftClient {
    private static applicationProperties = new SpecHelper().getApplicationProperties()

    private static builder = {
        ClientBuilder clientBuilder =
        new ClientBuilder(applicationProperties['config.openshift.url'])
            .withUserName(applicationProperties['config.openshift.user.name'])
            .withPassword(applicationProperties['config.openshift.user.password'])
        String proxyHost = applicationProperties['config.proxy.host']
        Integer proxyPort = Integer.valueOf(applicationProperties['config.proxy.port'])
        if( proxyHost?.trim() && proxyPort > 0 ){
            clientBuilder
                    .proxy(new Proxy(Proxy.Type.HTTP,
                            new InetSocketAddress(proxyHost, proxyPort)))
        }
        clientBuilder
    }.call()

    private OpenShiftClient(client) {
        this.client = client
    }

    private final IClient client
    private String project

    def static connect() {
        return new OpenShiftClient(builder.build())
    }

    def static connect(project) {
        return connect().project(project)
    }

    def project(project) {
        this.project = project
        return this
    }

    def getConfigMap(name) {
        return getConfigMap(name, project)
    }

    def getConfigMap(name, project) {
        if (!name) {
            throw new IllegalArgumentException('You must specify a name')
        }
        if (!project) {
            throw new IllegalArgumentException('You must specify a project')
        }

        return client.<IConfigMap> get(ResourceKind.CONFIG_MAP, name, project)
    }

    def update(configMap) {
        return client.update(configMap)
    }

    def deploy(name) {
        deploy(name, project)
    }

    def deploy(name, project) {
        def deployment = getDeploymentConfig(name, project)
        def triggerable = deployment.getCapability(IDeploymentTriggerable.class)
        triggerable.force = true
        triggerable.latest = true
        triggerable.trigger()
    }

    def getLastDeploymentVersion(deploymentConfig) {
        return getLastDeploymentVersion(deploymentConfig, project)
    }

    def getLastDeploymentVersion(deploymentConfig, project) {
        def deployment = getDeploymentConfig(deploymentConfig, project)
        return deployment.latestVersionNumber
    }

    def getDeploymentConfig(name) {
        return getDeploymentConfig(name, project)
    }

    def getDeploymentConfig(name, project) {
        if (!name) {
            throw new IllegalArgumentException('You must specify a DeploymentConfig name')
        }
        if (!project) {
            throw new IllegalArgumentException('You must specify a project')
        }

        return client.<IDeploymentConfig> get(ResourceKind.DEPLOYMENT_CONFIG, name, project)
    }

    def waitForDeployment(String name, Integer lastVersion = 0) {
        return waitForDeployments([name], [lastVersion])
    }

    def waitForDeployments(ArrayList<String> names, ArrayList<Integer> lastVersions) {
        def name = names.join('|')
        def listener = new IOpenShiftWatchListener.OpenShiftWatchListenerAdapter() {
            private CountDownLatch latch = new CountDownLatch(names.size())
            private int[] newVersion = [0] * names.size()
            private boolean[] deleted = lastVersions.collect{ it == 0 }
            private boolean[] ready = [false] * names.size()

            @Override
            void received(IResource resource, IOpenShiftWatchListener.ChangeType change) {
                def matcher = resource.name =~ /($name)-(\d+)-.*/
                def index = -1
                if (resource.kind == ResourceKind.POD && matcher.matches()) {
                    def version = Integer.parseInt(matcher.group(2))
                    index = names.findIndexOf {it == matcher[0][1]}
                    switch (change) {
                        case IOpenShiftWatchListener.ChangeType.ADDED:
                            if (version > newVersion[index]) {
                                newVersion[index] = version
                                ready[index] = resource.isReady()
                            }
                            break
                        case IOpenShiftWatchListener.ChangeType.DELETED:
                            if (version == lastVersions[index]) {
                                deleted[index] = true
                            }
                            break
                        case IOpenShiftWatchListener.ChangeType.MODIFIED:
                            if (version == newVersion[index]) {
                                ready[index] = resource.isReady()
                            }
                            break
                    }
                }
                if (deleted[index] && ready[index] && newVersion[index] > lastVersions[index]) {
                    latch.countDown()
                }
            }

            def await(timeout, timeUnit) {
                return latch.await(timeout, timeUnit)
            }

            def getNewVersion() {
                return newVersion
            }
        }
        def completed
        def watcher = client.watch(project, listener, ResourceKind.POD)
        try {
            completed = listener.await(10, TimeUnit.MINUTES)
        } finally {
            watcher.stop()
        }
        return completed ? listener.getNewVersion() : 0
    }

    def modifyConfigMap(configMap, map) {
        JBossDmrExtentions.set(configMap.getNode(), configMap.getPropertyKeys(), 'data', map)
        return configMap
    }

}
