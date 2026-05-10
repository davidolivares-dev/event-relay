package com.eventrelay.api

import com.eventrelay.domain.Topic
import com.eventrelay.repository.TopicRepository
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import kotlinx.datetime.Clock
import java.util.UUID

fun Route.topicRoutes(repository: TopicRepository) {
    post("/topics") {
        val request = call.receive<CreateTopicRequest>()
        val now = Clock.System.now()
        val topic = Topic(
            id = UUID.randomUUID(),
            name = request.name,
            description = request.description,
            createdAt = now,
            updatedAt = now
        )
        val saved = repository.create(topic)
        call.respond(status = HttpStatusCode.Created, message = saved)
    }
    get("/topics") {
        val topics = repository.findAll()
        call.respond(topics)
    }
    get("/topics/{id}") {
        val idParam = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
        val id = try {
            UUID.fromString(idParam)
        } catch (e: IllegalArgumentException) {
            return@get call.respond(HttpStatusCode.BadRequest)
        }
        val topic = repository.findById(id) ?: return@get call.respond(HttpStatusCode.NotFound)
        call.respond(topic)
    }
    delete("/topics/{id}") {
        val idParam = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
        val id = try {
            UUID.fromString(idParam)
        } catch (e: IllegalArgumentException) {
            return@delete call.respond(HttpStatusCode.BadRequest)
        }
        val deleted = repository.delete(id)
        if (deleted) {
            call.respond(HttpStatusCode.NoContent)
        } else {
            call.respond(HttpStatusCode.NotFound)
        }
    }
}