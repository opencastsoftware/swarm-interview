import eco.swarm.cloud.GenerateJooq
import java.time.Instant
import org.gradle.accessors.dm.LibrariesForLibs
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging

plugins { kotlin("jvm") }

// Workaround for https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

val TaskContainer.generateJooq: TaskProvider<GenerateJooq>
  get() = named<GenerateJooq>("generateJooq")

val generateJooqConfig = configurations.create("generateJooq")

dependencies {
  generateJooqConfig(platform(libs.springBootDependencies))
  generateJooqConfig(libs.testcontainersPostgres)
  generateJooqConfig(libs.flywayCore)
  generateJooqConfig(libs.jooqCodegen)
  generateJooqConfig(libs.jooqPostgresExtensions)
  generateJooqConfig(libs.postgresJdbc)
}

val generateJooq by tasks.registering(GenerateJooq::class) {
  taskClasspath.from(generateJooqConfig)
  migrations.set(layout.projectDirectory.dir("src/main/resources/db/migration"))
  outputDir.set(layout.buildDirectory.dir("generated-src/jooq"))
}

tasks.compileKotlin { dependsOn(generateJooq) }

sourceSets { main { kotlin { srcDir(layout.buildDirectory.dir("generated-src/jooq")) } } }
