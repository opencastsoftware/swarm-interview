package eco.swarm.cloud.interview.controllers

import eco.swarm.cloud.interview.models.Location
import eco.swarm.cloud.interview.models.LocationId
import eco.swarm.cloud.interview.repositories.LocationsRepository
import kotlinx.coroutines.flow.toList
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/locations")
class LocationsController(
  private val repository: LocationsRepository
) {

  @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
  suspend fun getLocations(): List<Location> {
    return repository.getLocations().toList()
  }

  @GetMapping("/{locationId}", produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE])
  suspend fun getLocationById(@PathVariable locationId: LocationId): Location {
    return repository.getLocationById(locationId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
  }
}