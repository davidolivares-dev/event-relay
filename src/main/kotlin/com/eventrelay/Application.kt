package com.eventrelay

import com.eventrelay.plugins.configureErrorHandling
import com.eventrelay.plugins.configureRouting
import com.eventrelay.plugins.configureSerialization
import com.eventrelay.repository.InMemoryTopicRepository
import com.eventrelay.repository.TopicRepository
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    val topicRepo: TopicRepository = InMemoryTopicRepository()
    embeddedServer(Netty, port = 8080) {
        configureSerialization()
        configureErrorHandling()
        configureRouting(topicRepo)
    }.start(wait = true)
}