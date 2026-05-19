package com.eventrelay

import com.eventrelay.plugins.configureErrorHandling
import com.eventrelay.plugins.configureRouting
import com.eventrelay.plugins.configureSerialization
import com.eventrelay.repository.InMemoryTopicRepository
import com.eventrelay.repository.TopicRepository
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun Application.module(topicRepo: TopicRepository) {
    configureSerialization()
    configureErrorHandling()
    configureRouting(topicRepo)
}

fun main() {
    val topicRepo: TopicRepository = InMemoryTopicRepository()
    embeddedServer(Netty, port = 8080) {
        module(topicRepo)
    }.start(wait = true)
}