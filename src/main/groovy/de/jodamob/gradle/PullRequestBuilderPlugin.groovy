package de.jodamob.gradle

import com.github.kittinunf.fuel.Fuel
import groovy.transform.PackageScope
import kotlin.Pair
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

import java.nio.charset.Charset

class PullRequestBuilderPlugin implements Plugin<Project> {

    @PackageScope static final String GIT_TASK = 'gitCommit'
    @PackageScope static final String PR_TASK = 'gitPullRequest'
    String branchName

    @Override
    void apply(Project project) {
        PullRequestBuilderExtension extension = project.extensions.create('gitPullRequest', PullRequestBuilderExtension, project)

        Task git = createGitTask(project, extension)
        Task gitHub = createPullRequestTask(project, extension)
        gitHub.dependsOn git
    }

    private Task createGitTask(Project project, PullRequestBuilderExtension extension) {
        Task task = project.tasks.create(GIT_TASK)
        task.outputs.upToDateWhen { false }
        task.with {
            group = 'publishing'
            description = 'Commits and pushes changes to git.'
            doFirst {
                branchName = extension.branchSuffix + new Date().getTime().toString()
            }
            doLast {
                "git branch ${branchName}".execute().waitFor()
                "git checkout ${branchName}".execute().waitFor()
                "git add ${extension.source}".execute().waitFor()
                "git commit -m ${extension.message}".execute().waitFor()
                "git push".execute().waitFor()
            }
        }
        return task
    }

    private Task createPullRequestTask(Project project, PullRequestBuilderExtension extension) {
        Task task = project.tasks.create(PR_TASK)
        task.outputs.upToDateWhen { false }
        task.with {
            group = 'publishing'
            description = 'Creates Pull request on github.'
            onlyIf { dependsOnTaskDidWork() }
            doLast {
                println Fuel.post("${extension.githubUri.replaceAll("github.com", "api.github.com/repos") + "/pulls" }")
                .authenticate("${extension.user}", "${extension.accessToken}")
                        .body("{ \"title\" : \"${extension.title}\", " +
                        "\"body\" : \"${extension.message}\"," +
                        "\"head\" : \"${branchName}\"," +
                        "\"base\" : \"${extension.master}\" }", Charset.forName("utf-8"))
                .header(new Pair<String, Object>("Content-Type", "application/json"))
                .response().toString()
            }
        }
        return task
    }
}
