package eco.swarm.cloud.interview.repositories

import eco.swarm.cloud.interview.models.LocationId
import io.kotest.core.extensions.install
import io.kotest.core.spec.style.WordSpec
import io.kotest.extensions.testcontainers.JdbcDatabaseContainerExtension
import io.kotest.matchers.shouldBe
import io.r2dbc.spi.ConnectionFactories
import kotlinx.coroutines.flow.toList
import org.flywaydb.core.Flyway
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.PostgreSQLR2DBCDatabaseContainer
import org.testcontainers.containers.wait.strategy.Wait
import java.util.*

class LocationsRepositoryTest : WordSpec() {
  init {
    val postgres = PostgreSQLContainer("postgres:16.2")
      .waitingFor(Wait.forListeningPort())

    install(JdbcDatabaseContainerExtension(postgres))

    beforeSpec {
      Flyway.configure()
        .locations("classpath:db/migration/common")
        .dataSource(postgres.jdbcUrl, postgres.username, postgres.password)
        .defaultSchema("swarm")
        .load()
        .migrate()
    }

    "LocationsRepository".`when` {
      val r2dbcOptions = PostgreSQLR2DBCDatabaseContainer.getOptions(postgres)
      val r2dbcConnection = ConnectionFactories.get(r2dbcOptions)
      val ctx = DSL.using(r2dbcConnection, SQLDialect.POSTGRES)
      val repo = LocationsRepository(ctx)

      "getLocations is called" should {
        "return a list of locations" {
          repo.getLocations().toList() shouldBe emptyList()
        }
      }

      "getLocationById is called with a valid ID, but there is no matching location" should {
        "return null" {
          repo.getLocationById(LocationId(UUID.randomUUID())) shouldBe null
        }
      }
    }
  }
}