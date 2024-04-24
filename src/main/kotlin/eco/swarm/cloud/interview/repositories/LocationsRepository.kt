package eco.swarm.cloud.interview.repositories

import eco.swarm.cloud.interview.db.tables.records.LocationsRecord
import eco.swarm.cloud.interview.db.tables.references.LOCATIONS
import eco.swarm.cloud.interview.models.Location
import eco.swarm.cloud.interview.models.LocationId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class LocationsRepository(
  private val ctx: DSLContext
) {
  fun getLocations(): Flow<Location> {
    return ctx.selectFrom(LOCATIONS).asFlow().mapNotNull(::toLocation)
  }

  suspend fun getLocationById(locationId: LocationId): Location? {
    return ctx.selectFrom(LOCATIONS)
      .where(LOCATIONS.ID.eq(locationId.value))
      .awaitFirstOrNull()
      ?.let(::toLocation)
  }

  private fun toLocation(record: LocationsRecord): Location? {
    return record.id?.let { id ->
      record.nickname?.let { nickname ->
        Location(LocationId(id), nickname)
      }
    }
  }
}