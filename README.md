# Random Coffee LMRU

**Why**: 

**What**:

## Summary

## Concept

## Getting started / Prerequisites / Dependencies

The following items should be installed in your system:

* git command line tool (https://help.github.com/articles/set-up-git)
* your favorite IDE – [IDEA](https://www.jetbrains.com/idea/) preferably
* Java 15 or newer – we are using [Liberica 15](https://bell-sw.com/pages/downloads/) on production
* kotlinc-jvm 1.5 or newer – [IDEA Kotlin plugin](https://plugins.jetbrains.com/plugin/6954-kotlin) fits ideally (update if needed)
* maven 3.6 – IDE plugin or [standalone](https://spring.io/guides/gs/maven/)
* docker for local run

## Development

> Before start to developing you must to copy file '.env.example' to '.env.dev' and set your environments 

1. Build application
    ```shell
    mvn clean package
    ```
2. Build docker image
    ```shell
    docker build -f Dockerfile \
    -t docker-local-cr.art.lmru.tech/random-coffee:$(mvn org.apache.maven.plugins:maven-help-plugin:evaluate -Dexpression=project.version -q -DforceStdout) \
    -t docker-local-cr.art.lmru.tech/random-coffee:latest .  
    ```
3. Run docker container
   ```shell
   # run all containers
   docker compose --env-file=.env.dev up -d
   # or run only application with logging into stdout
   docker compose --env-file=.env.dev up random-coffee
   # or run only application without logs
   docker compose --env-file=.env.dev up -d random-coffee
   ```

## Release process

1. Checkout to branch 'develop'
2. Run command for start release process and create the branch 'release'
    ```shell
    mvn clean jgitflow:release-start
    ```
3. Run command for finish release process and merge changes into the branch 'master' 
    ```shell
    mvn jgitflow:release-finish -DpushReleases=true -DnoDeploy=true 
    ```
4. Checkout to the branch 'master'
5. Build application
    ```shell
    mvn clean spring-boot:build-info package
    ```
6. Build docker image
    ```shell
    docker build -f Dockerfile \
    -t docker-local-cr.art.lmru.tech/random-coffee:$(mvn org.apache.maven.plugins:maven-help-plugin:evaluate -Dexpression=project.version -q -DforceStdout) \
    -t docker-local-cr.art.lmru.tech/random-coffee:stable .  
    ```
7. Push image to docker registry
   ```shell
   docker login -u <your ldap> docker-local-cr.art.lmru.tech
   docker push docker-local-cr.art.lmru.tech/random-coffee:stable
   docker push docker-local-cr.art.lmru.tech/random-coffee:<stable image with release version>
   ```