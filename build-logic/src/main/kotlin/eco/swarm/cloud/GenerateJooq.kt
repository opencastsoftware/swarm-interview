package eco.swarm.cloud

import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.workers.WorkerExecutor
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Matchers

abstract class GenerateJooq : DefaultTask() {
  @get:InputFiles
  @get:SkipWhenEmpty
  @get:PathSensitive(PathSensitivity.RELATIVE)
  abstract val migrations: DirectoryProperty

  @get:InputFiles abstract val taskClasspath: ConfigurableFileCollection

  @get:Input abstract val imageName: Property<String>

  @get:Input abstract val inputSchema: Property<String>

  @get:Input abstract val outputPackage: Property<String>

  @get:OutputDirectory abstract val outputDir: DirectoryProperty

  @get:Inject abstract val workerExecutor: WorkerExecutor

  @TaskAction
  fun generate() {
    workerExecutor
      .classLoaderIsolation { classpath.from(taskClasspath) }
      .submit(GenerateJooqAction::class.java) {
        migrations.set(this@GenerateJooq.migrations)
        imageName.set(this@GenerateJooq.imageName)
        inputSchema.set(this@GenerateJooq.inputSchema)
        outputPackage.set(this@GenerateJooq.outputPackage)
        outputDir.set(this@GenerateJooq.outputDir)
      }
  }
}
