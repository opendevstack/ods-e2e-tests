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

    /**
     * Clone a repository in a temporary folder.
     * it ignores the ssl certificate for wrong certificates.
     * @param project the project key
     * @param repository the repository
     * @return the repository
     */
    static cloneRepository(project, repository, branch = null, isODSComponentRepo = true) {
        def gitUrl = baseUrlBitbucket.endsWith('/') ? baseUrlBitbucket : baseUrlBitbucket + '/'
        def repositoryUrl
        if (isODSComponentRepo) {
            repositoryUrl = "${gitUrl}scm/$project/$project-${repository}.git".toLowerCase()
        } else {
            repositoryUrl = "${gitUrl}scm/$project/${repository}.git".toLowerCase()
        }

        File localPath = Files.createTempDirectory("${repository}-").toFile()
        def gitRepository = Git.cloneRepository()
                .setDirectory(localPath)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .setURI(repositoryUrl)
                .setBranch(branch).call()
        gitRepository.close()
        return gitRepository
    }

    /**
     * Commit all modified files
     * @param gitRepository The git repository
     * @param message The commit message
     */
    static commitAddAll(gitRepository, message = 'new commit') {
        println "GitUil: Commit $message"
        gitRepository.commit().setMessage(message).setAll(true).call()
    }

    /**
     * Push commits to a repository
     * @param gitRepository The git repository
     * @param remote The remote repository
     * @return
     */
    static push(gitRepository, remote = 'origin') {
        gitRepository.push()
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .setRemote(remote)
                .call()
    }

    /**
     * Checkout an specific branch.
     * @param gitRepository The git repository.
     * @param branch The branch name.
     * @param createBranch If you want to create the branch if it does not exists.
     * @return
     */
    static checkout(gitRepository, branch, createBranch = false) {
        gitRepository.checkout()
                .setName(branch)
                .setCreateBranch(createBranch)
                .call()
    }

    /**
     * Add files to the repository.
     * @param gitRepository The repository.
     * @param files The files.
     */
    static add(gitRepository, files) {
        gitRepository
                .add()
                .addFilepattern(files)
                .call()
    }
}
