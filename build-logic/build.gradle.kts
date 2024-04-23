plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Gradle plugins
    implementation(libs.kotlinGradle)
    // jOOQ code generation
    compileOnly(platform(libs.springBootDependencies))
    compileOnly(libs.testcontainersPostgres)
    compileOnly(libs.flywayCore)
    compileOnly(libs.jooqCodegen)
    compileOnly(libs.jooqPostgresExtensions)
    compileOnly(libs.postgresJdbc)
    // Workaround for https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}
