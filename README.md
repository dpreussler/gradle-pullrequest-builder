# gradle-pullrequest-builder

[![Travis](https://img.shields.io/travis/dpreussler/gradle-pullrequest-builder.svg?style=flat-square)](https://travis-ci.org/dpreussler/gradle-pullrequest-builder)

## Purpose

An Github Pull Request builder.
Commits unchanged files to Git (local git installation needed) and creates Pull request.
Meant as part of CI jobs that need to change code.

### Applying the Plugin

```groovy
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.de.jodamob.gradle:gradle-pullrequest-builder:1.0.RC1"
  }
}

apply plugin: "de.jodamob.gradle.github"
```

### Configuration


```groovy
gitPullRequest {
    // where to publish to (repo must exist)
    githubUri = 'https://github.com/dpreussler/gradle-pullrequest-builder'

    // github user name to use
    user = 'dpreussler'

    // github password or better access token
    accessToken = '12345678901234567890'

    // the name the branch should start with (will be added by timestamp to avoid collisions)
    branchSuffix = 'TICKET-1234_automatic'

    // the target branch to send pull request to
    master = 'develop'

    // optional, the folder to commit all new or changed files from (default=src)
    source = 'app/src'

    // optional, the title of the pull request
    title = 'TICKET-1234: automatic pull request'

    // optional, the message of the commit
    message = 'TICKET-1234: automatic pull request'

}
```

### Tasks and Execution

Generally, you'll just run `gitPullRequest`

* `gitPullRequest` - runs 'gitCommit' and then creates Pull Request
* `gitCommit` - commits and pushes



Licensed under MIT license
(c) 2017 Danny Preussler
