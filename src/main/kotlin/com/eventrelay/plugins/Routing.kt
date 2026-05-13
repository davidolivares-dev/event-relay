package com.eventrelay.plugins

import com.eventrelay.api.topicRoutes
import com.eventrelay.repository.TopicRepository
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting(topicRepo: TopicRepository) {
    routing {
        get("/") { call.respondText("Hello World!\n") }
        topicRoutes(repository = topicRepo)
    }
}
