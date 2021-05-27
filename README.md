# Random Coffee LMRU

## Development

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