package eco.swarm.cloud.interview.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import eco.swarm.cloud.interview.models.Location
import eco.swarm.cloud.interview.models.LocationId
import eco.swarm.cloud.interview.repositories.LocationsRepository
import io.kotest.core.spec.style.WordSpec
import io.mockk.coEvery
import io.mockk.every
import kotlinx.coroutines.flow.asFlow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ProblemDetail
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*

@WebFluxTest(controllers = [LocationsController::class])
class LocationsControllerTest : WordSpec() {
  @Autowired
  private lateinit var webClient: WebTestClient

  @Autowired
  private lateinit var objectMapper: ObjectMapper

  @MockkBean
  private lateinit var locationsRepository: LocationsRepository

  init {
    "LocationsController" `when` {
      "GET /locations is called with an empty database" should {
        "return Ok with an empty list of locations" {
          val locations = emptyList<Location>()

          every { locationsRepository.getLocations() } returns locations.asFlow()

          webClient.get()
            .uri("/locations")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .json(objectMapper.writeValueAsString(locations))
        }
      }

      "GET /locations is called with a populated database" should {
        "return Ok with a list of locations" {
          val locations = listOf(
            Location(LocationId(UUID.randomUUID()), "Laura's House"),
            Location(LocationId(UUID.randomUUID()), "Gabriel's House"),
            Location(LocationId(UUID.randomUUID()), "Husain's House"),
            Location(LocationId(UUID.randomUUID()), "Chen's House"),
          )

          every { locationsRepository.getLocations() } returns locations.asFlow()

          webClient.get()
            .uri("/locations")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectHeader()
            .contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .json(objectMapper.writeValueAsString(locations))
        }
      }

      "GET /locations/<id> is called with a valid ID, but there is no matching location" should {
        "return Not Found" {
          val locationId = LocationId(UUID.randomUUID())
          val notFoundError = ProblemDetail.forStatus(HttpStatus.NOT_FOUND)

          coEvery { locationsRepository.getLocationById(locationId) } returns null

          webClient.get()
            .uri("/locations/${locationId.value}")
            .exchange()
            .expectStatus()
            .isNotFound
            .expectBody()
            .json(objectMapper.writeValueAsString(notFoundError))
        }
      }

      "GET /locations/<id> is called with a valid ID that matches a location" should {
        "return Ok with the location" {
          val locationId = LocationId(UUID.randomUUID())
          val location = Location(locationId, "Laura's House")

          coEvery { locationsRepository.getLocationById(locationId) } returns location

          webClient.get()
            .uri("/locations/${locationId.value}")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody()
            .json(objectMapper.writeValueAsString(location))
        }
      }

      "GET /locations/<id> is called with an invalid ID" should {
        "return Bad Request" {
          val badRequestError = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)

          webClient.get()
            .uri("/locations/bla")
            .exchange()
            .expectStatus()
            .is4xxClientError
            .expectBody()
            .json(objectMapper.writeValueAsString(badRequestError))
        }
      }
    }
  }
}