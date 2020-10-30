package org.ods.e2e.util

import org.eclipse.jgit.api.Git
import org.eclipse.jgit.lib.Ref;
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
     * Push tags to a repository
     * @param gitRepository The git repository
     * @param remote The remote repository
     * @return
     */
    static pushTag(gitRepository, remote = 'origin') {
        gitRepository.push()
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .setRemote(remote)
                .setPushTags()
                .setForce(true)
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

    static deleteBranch(Git gitRepository, branch, remote = false) {
        def branches = [branch]
        if (remote) {
            branches << 'origin/' + branch
        }
        gitRepository.branchDelete().setBranchNames(branches as String[]).call()
    }

    /**
     * Delete tag.
     * @param gitRepository The repository.
     * @param tag The tag.
     * @param remote delete remote.
     */
    static deleteTag(Git gitRepository, tag, remote = true) {
        def tags = [tag]
        if (remote) {
            tags << 'origin/' + tag
        }

        gitRepository.tagDelete().setTags(tags as String[]).call()
    }

    /**
     * Create tag.
     * @param gitRepository The repository.
     * @param tag The tag.
     * @param message The message.
     */
    static createTag(Git gitRepository, tag, message) {
        gitRepository.tag().setName(tag).setMessage(message).call()
    }

    /**
     * Get tags.
     * @param gitRepository The repository.
     * @return the Tags
     */
    static getTags(Git gitRepository) {
        List<Ref> call = gitRepository.tagList().call()
        def tags = []
        for (Ref ref : call) {
            println("Tag: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName())
            tags << ref.getName()
        }
        return tags
    }
}
