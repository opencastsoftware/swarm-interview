package eco.swarm.cloud

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.workers.WorkParameters
import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging
import org.jooq.meta.jaxb.Matchers

interface GenerateJooqParameters : WorkParameters {
  @get:InputFiles
  @get:SkipWhenEmpty
  @get:PathSensitive(PathSensitivity.RELATIVE)
  val migrations: DirectoryProperty

  @get:Input val imageName: Property<String>

  @get:Input val inputSchema: Property<String>

  @get:Input val outputPackage: Property<String>

  @get:OutputDirectory val outputDir: DirectoryProperty
}
