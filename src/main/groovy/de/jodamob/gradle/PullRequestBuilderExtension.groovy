package de.jodamob.gradle

import org.gradle.api.Project

class PullRequestBuilderExtension {

    String githubUri
    String user
    String accessToken
    String branchSuffix
    String master
    String source = 'src'
    String title = 'automated pull request'
    String message = 'this is a generated pull request'

    public PullRequestBuilderExtension(Project project) {
    }
}
