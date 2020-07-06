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

import java.sql.Timestamp
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class OpenShiftClient {
    private static applicationProperties = new SpecHelper().getApplicationProperties()
    private static builder = new ClientBuilder(applicationProperties['config.openshift.url'])
            .withUserName(applicationProperties['config.openshift.user.name'])
            .withPassword(applicationProperties['config.openshift.user.password'])

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

    def waitForDeployment(name, lastVersion) {
        return waitForDeployment(name, lastVersion, project)
    }

    def waitForDeployment(name, lastVersion, project) {
        System.out.println(new Timestamp(System.currentTimeMillis()))
        def listener = new IOpenShiftWatchListener.OpenShiftWatchListenerAdapter() {
            private CountDownLatch latch = new CountDownLatch(1)
            private int newVersion = 0
            private boolean deleted = false
            private boolean ready = false

            @Override
            void received(IResource resource, IOpenShiftWatchListener.ChangeType change) {
                if (resource.kind == ResourceKind.POD && resource.getAnnotation('openshift.io/deployment-config.name') == name) {
                    def version = Integer.parseInt(resource.getAnnotation('openshift.io/deployment-config.latest-version'))
                    switch (change) {
                        case IOpenShiftWatchListener.ChangeType.ADDED:
                            newVersion = version
                            ready = resource.isReady()
                            break;
                        case IOpenShiftWatchListener.ChangeType.DELETED:
                            if (version == lastVersion) {
                                deleted = true
                            }
                            break;
                        case IOpenShiftWatchListener.ChangeType.MODIFIED:
                            if (version == newVersion) {
                                ready = resource.isReady()
                            }
                    }
                }
                if (deleted && ready && newVersion > lastVersion) {
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
            completed = listener.await(5, TimeUnit.MINUTES)
        } finally {
            watcher.stop()
        }
        System.out.println(new Timestamp(System.currentTimeMillis()))
        return completed ? listener.getNewVersion() : 0
    }

    def modifyConfigMap(configMap, map) {
        JBossDmrExtentions.set(configMap.getNode(), configMap.getPropertyKeys(), 'data', map)
        return configMap
    }

}
