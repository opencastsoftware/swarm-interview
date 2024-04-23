import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.springBoot)
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.kotlin.spring)
  id("eco.swarm.cloud.jooq-kotlin-generation")
  application
}

repositories { mavenCentral() }

group = "eco.swarm.cloud"
version = "0.1.0-SNAPSHOT"

dependencies {
  // Dependency versions
  implementation(platform(libs.springBootDependencies))
  runtimeOnly(platform(libs.springBootDependencies))
  developmentOnly(platform(libs.springBootDependencies))

  // Core dependencies
  implementation(libs.springBootStarter)
  implementation(libs.springBootStarterWebflux)

  // Kotlin coroutines
  implementation(libs.bundles.kotlinCoroutines)

  // Kotlin JSON support
  implementation(libs.jacksonModuleKotlin)

  // Database connectivity
  implementation(libs.springBootStarterDataR2dbc)
  runtimeOnly(libs.postgresJdbc)
  runtimeOnly(libs.postgresR2dbc)
  runtimeOnly(libs.flywayCore)

  // SQL queries
  implementation(libs.springBootStarterJooq)
  implementation(libs.bundles.jooq)

  // Dev tools
  developmentOnly(libs.springBootDevtools)
  developmentOnly(libs.springBootDockerCompose)
}

tasks.bootRun {
  systemProperty("spring.profiles.active", "local")
  systemProperty("org.jooq.no-logo", true)
  systemProperty("org.jooq.no-tips", true)
}

tasks.generateJooq {
  imageName = "postgres:16.2"
  inputSchema = "swarm"
  outputPackage = "eco.swarm.cloud.interview.db"
}

testing {
  suites {
    val test by getting(JvmTestSuite::class) {
      useJUnitJupiter(libs.versions.junit)
      dependencies {
        implementation(libs.springBootStarterTest)
        implementation(libs.kotlinCoroutinesTest)
        implementation(libs.mockk)
        implementation(libs.springMockk)
        implementation(libs.kotest)
        implementation(libs.kotestSpring)
      }
    }

    val integrationTest by registering(JvmTestSuite::class) {
      testType = TestSuiteType.INTEGRATION_TEST
      useJUnitJupiter(libs.versions.junit)
      dependencies {
        implementation(project())
        implementation(platform(libs.springBootDependencies))
        implementation(libs.jooq)
        implementation(libs.springBootStarterTest)
        implementation(libs.kotlinCoroutinesTest)
        implementation(libs.flywayCore)
        implementation(libs.mockk)
        implementation(libs.springMockk)
        implementation(libs.kotest)
        implementation(libs.kotestSpring)
        implementation(libs.kotestTestcontainers)
        implementation(libs.testcontainersR2dbc)
        implementation(libs.testcontainersPostgres)
      }
    }
  }
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(17)
  }
}

tasks.withType<JavaCompile> {
  options.encoding = "UTF-8"
  options.compilerArgs.add("-Xlint")
}

tasks.withType<Test> {
  systemProperty("org.jooq.no-logo", true)
  systemProperty("org.jooq.no-tips", true)
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    compilerOptions { jvmTarget.set(JvmTarget.JVM_17) }
    freeCompilerArgs += "-Xjsr305=strict"
  }
}
