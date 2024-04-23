package eco.swarm.cloud

import org.flywaydb.core.Flyway
import org.gradle.workers.WorkAction
import org.jooq.codegen.GenerationTool
import org.jooq.meta.jaxb.*
import org.jooq.meta.jaxb.Target
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.utility.DockerImageName

abstract class GenerateJooqAction : WorkAction<GenerateJooqParameters> {
  override fun execute() {
    try {
      val dockerImage =
        DockerImageName.parse(parameters.imageName.get())
          .asCompatibleSubstituteFor("postgres")

      PostgreSQLContainer(dockerImage)
        .waitingFor(Wait.forListeningPort())
        .also { it.start() }
        .use {
          Flyway.configure()
            .locations("filesystem:${parameters.migrations.asFile.get()}")
            .dataSource(it.jdbcUrl, it.username, it.password)
            .defaultSchema(parameters.inputSchema.get())
            .load()
            .migrate()

          val jooqJdbc =
            Jdbc().withUrl(it.jdbcUrl).withUsername(it.username).withPassword(it.password)

          val jooqGeneratorDatabase =
            Database()
              .withName("org.jooq.meta.postgres.PostgresDatabase")
              .withInputSchema(parameters.inputSchema.get())
              .withExcludes("flyway_.*")

          val jooqGeneratorTarget =
            Target()
              .withDirectory(parameters.outputDir.asFile.get().toString())
              .withPackageName(parameters.outputPackage.get())

          val jooqGeneratorGenerate = Generate().withRecordsImplementingRecordN(true)

          val jooqGenerator =
            Generator()
              .withName("org.jooq.codegen.KotlinGenerator")
              .withDatabase(jooqGeneratorDatabase)
              .withGenerate(jooqGeneratorGenerate)
              .withTarget(jooqGeneratorTarget)

          val jooqConfig =
            Configuration()
              .withJdbc(jooqJdbc)
              .withGenerator(jooqGenerator)
              .withLogging(Logging.WARN)

          GenerationTool.generate(jooqConfig)
        }
    } catch (exc: Exception) {
      throw RuntimeException(exc.message)
    }
  }
}
