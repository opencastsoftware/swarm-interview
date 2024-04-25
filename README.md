# swarm-interview

This repository contains the take-home test exercises for Swarm developer interviews.

The exercises involve making changes to the HTTP API in the `src` folder.

This readme file details how to get set up for development and build the project.

The exercises can be found [here](./EXERCISES.md).

## Prerequisites

We've tried to make sure that it's as easy as possible to set up this repository.

We offer several options, from easiest to most difficult:

### 1. Set up via Gitpod

This option requires the least setup, as it opens a remote workspace with all of the required dependencies already configured.

First, install [JetBrains Gateway](https://www.jetbrains.com/remote-development/gateway/), and connect to [Gitpod](https://gitpod.io).

Next, use [this workspace snapshot link]() to connect to the workspace.

The workspace should open in IntelliJ. You will need to give IntelliJ some time to index dependencies - the downside of remote workspaces is that they can be a little slow!

### 2. Set up via Development Containers

This option requires that you have [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed for the best experience. We have had mixed results with other container runtimes like Colima and Podman.

If you're using [IntelliJ](https://www.jetbrains.com/idea/) you can start a [Development Container](https://containers.dev/) using the [devcontainer.json](./.devcontainer/devcontainer.json) file in the repository.

Please see the [IntelliJ documentation](https://www.jetbrains.com/help/idea/connect-to-devcontainer.html), which describes their Dev Container support.

### 3. Manually

To set up manually, you'll need to install the following things:

#### IntelliJ IDEA

We strongly recommend that you use [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/) for the best experience when editing this project.

#### Java JDK

This project is a [Spring Boot](https://spring.io/projects/spring-boot) project built with [Gradle](https://gradle.org/).

You will need a Java 17 JDK installed in order to build this project.

You can install a JDK [manually](https://adoptium.net/temurin/releases/), via [an installer like Homebrew](https://adoptium.net/installation/) or use a tool like [SDKMAN!](https://sdkman.io/jdks#tem) to manage the installation for you.

#### Container runtime

The project uses [Docker Compose](https://docs.docker.com/compose/) to run the project locally and [Testcontainers](https://testcontainers.com/) for integration testing.

As a result, you'll need to install a Docker-compatible container runtime, such as [Docker Desktop](https://www.docker.com/products/docker-desktop/), [Colima](https://github.com/abiosoft/colima) or [Podman](https://podman.io/).

Please see the [Testcontainers documentation](https://java.testcontainers.org/supported_docker_environment/#using-colima) for advice on configuring Testcontainers to work with Colima and Podman. Testcontainers should work out of the box with Docker Desktop.

## Project structure

* `gradle/libs.versions.toml` - a Gradle [version catalog](https://docs.gradle.org/current/userguide/platforms.html#sub:conventional-dependencies-toml) declaring the project dependencies
* `src` - the source folder for the interview exercise app, a Spring Boot microservice written in Kotlin

## Common build tasks

The Gradle wrapper script `./gradlew` can be used to run Gradle tasks.

> [!NOTE]
> The `--info` flag is not necessary in the command examples below, but enables extra logging which may make it easier to understand what has happened when builds or tests fail.

### Build the project
To build and run all tests:

```shell
$ ./gradlew build --info
```

### Run tests
To run all tests:

```shell
$ ./gradlew check --info
```

### Run only unit tests
To run only unit tests:

```shell
$ ./gradlew test --info
```

### Run only integration tests
To run only integration tests:

```shell
$ ./gradlew integrationTest --info
```

### Run the application

To run the app:

```shell
$ ./gradlew bootRun --info
```

## Unit testing

Tests are written using a Kotlin test framework called [Kotest](https://kotest.io).

Quick start documentation for Kotest can be found [here](https://kotest.io/docs/quickstart).

If you are using IntelliJ as your IDE, we recommend that you install the [Kotest Plugin](https://kotest.io/docs/intellij/intellij-plugin.html).
