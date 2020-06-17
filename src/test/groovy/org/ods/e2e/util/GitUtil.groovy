package org.ods.e2e.util


import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.URIish
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider

import javax.net.ssl.X509TrustManager
import java.nio.file.Files
import java.security.cert.CertificateException
import java.security.cert.X509Certificate

class TrustEverythingSSLTrustManager implements X509TrustManager {

    @Override
    void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    X509Certificate[] getAcceptedIssuers() {
        return null
    }
}

class GitUtil {
    static Properties applicationProperties = new SpecHelper().getApplicationProperties()
    static baseUrlBitbucket = applicationProperties."config.atlassian.bitbucket.url"
    static username = applicationProperties."config.atlassian.user.name"
    static password = applicationProperties."config.atlassian.user.password"

    // Git repository used in the tests
    static Git gitRepository

    /**
     * Clone a repository in a temporary folder.
     * it ignores the ssl certificate for wrong certificates.
     * @param project the project key
     * @param repository the repository
     * @return the repository location
     */
    static cloneRepository(project, repository) {
        def gitUrl = baseUrlBitbucket.endsWith('/') ? baseUrlBitbucket : baseUrlBitbucket + '/'
        def repositoryUrl = "${gitUrl}scm/$project/$project-${repository}.git".toLowerCase()

        File localPath = Files.createTempDirectory("${repository}-").toFile()

        gitRepository = Git.init().setDirectory(localPath).call()
        def config = gitRepository.getRepository().getConfig()
        config.setBoolean("http", null, "sslVerify", false)
        config.save()
        gitRepository.remoteAdd().setName('origin').setUri(new URIish(repositoryUrl)).call()

        gitRepository.pull()
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .setRemote('origin')
                .setRemoteBranchName('master')
                .call()


        return localPath
    }

    /**
     * Commit all modified files
     * @param message The commit message
     */
    static commitAddAll(message = 'new commit') {
        gitRepository.commit().setMessage(message).setAll(true).call()
    }

    /**
     * Push commits to a repository
     * @param remote The remote repository
     * @return
     */
    static push(remote = 'origin') {
        gitRepository.push()
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .setRemote(remote)
                .call()
    }
}
