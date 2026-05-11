package com.eventrelay

import com.eventrelay.api.topicRoutes
import com.eventrelay.repository.InMemoryTopicRepository
import com.eventrelay.repository.TopicRepository
import io.ktor.server.application.call
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.application.install

fun main() {
    val topicRepo: TopicRepository = InMemoryTopicRepository()
    embeddedServer(factory = Netty, port = 8080) {
        install(ContentNegotiation) { json() }
        routing {
            get("/") { call.respondText("Hello World!\n") }
            topicRoutes(repository = topicRepo)
        }
    }.start(wait = true)
}
