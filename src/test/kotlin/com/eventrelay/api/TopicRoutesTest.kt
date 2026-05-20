package com.eventrelay.api

import com.eventrelay.domain.Topic
import com.eventrelay.module
import com.eventrelay.repository.InMemoryTopicRepository
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlinx.datetime.Clock
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TopicRoutesTest {

    private fun sampleTopic(
        name: String = "test.topic",
        description: String? = null
    ): Topic {
        val now = Clock.System.now()
        return Topic(
            id = UUID.randomUUID(),
            name = name,
            description = description,
            createdAt = now,
            updatedAt = now
        )
    }

    @Test
    fun `POST topics creates and returns the topic`() = testApplication {
        application {
            module(InMemoryTopicRepository())
        }
        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.post("/topics") {
            contentType(ContentType.Application.Json)
            setBody(CreateTopicRequest(name = "user.signup", description = "User signups"))
        }

        assertEquals(HttpStatusCode.Created, response.status)
        val created: Topic = response.body()
        assertEquals("user.signup", created.name)
        assertEquals("User signups", created.description)
        assertNotNull(created.id)
        assertEquals(created.createdAt, created.updatedAt)
    }

    @Test
    fun `GET topics returns all stored topics`() = testApplication {
        val repo = InMemoryTopicRepository()
        application { module(repo) }
        val client = createClient { install(ContentNegotiation) { json() } }

        val seededA = repo.create(sampleTopic(name = "alpha"))
        val seededB = repo.create(sampleTopic(name = "beta"))

        val response = client.get("/topics")

        assertEquals(HttpStatusCode.OK, response.status)
        val topics: List<Topic> = response.body()
        assertEquals(setOf(seededA, seededB), topics.toSet())
    }

    @Test
    fun `GET topic by id returns 200 with topic when present`() = testApplication {
        val repo = InMemoryTopicRepository()
        application { module(repo) }
        val client = createClient { install(ContentNegotiation) { json() } }

        val seeded = repo.create(sampleTopic(name = "fetchable"))

        val response = client.get("/topics/${seeded.id}")

        assertEquals(HttpStatusCode.OK, response.status)
        val fetched: Topic = response.body()
        assertEquals(seeded, fetched)
    }

    @Test
    fun `GET topic by id returns 404 when not found`() = testApplication {
        application { module(InMemoryTopicRepository()) }
        val client = createClient { install(ContentNegotiation) { json() } }

        val response = client.get("/topics/${UUID.randomUUID()}")

        assertEquals(HttpStatusCode.NotFound, response.status)
        val error: ErrorResponse = response.body()
        assertEquals("not_found", error.error)
    }

    @Test
    fun `DELETE topic returns 204 when present`() = testApplication {
        val repo = InMemoryTopicRepository()
        application { module(repo) }
        val client = createClient { install(ContentNegotiation) { json() } }

        val seeded = repo.create(sampleTopic())

        val response = client.delete("/topics/${seeded.id}")

        assertEquals(HttpStatusCode.NoContent, response.status)
        assertTrue(repo.findById(seeded.id) == null)
    }

    @Test
    fun `POST topics returns 400 validation_failed when name is missing`() = testApplication {
        application { module(InMemoryTopicRepository()) }
        val client = createClient { install(ContentNegotiation) { json() } }

        val response = client.post("/topics") {
            contentType(ContentType.Application.Json)
            setBody("""{"description": "no name"}""")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        val error: ErrorResponse = response.body()
        assertEquals("validation_failed", error.error)
    }

    @Test
    fun `POST topics returns 400 invalid_json when body is malformed`() = testApplication {
        application { module(InMemoryTopicRepository()) }
        val client = createClient { install(ContentNegotiation) { json() } }

        val response = client.post("/topics") {
            contentType(ContentType.Application.Json)
            setBody("{this is not json}")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        val error: ErrorResponse = response.body()
        assertEquals("invalid_json", error.error)
    }
}