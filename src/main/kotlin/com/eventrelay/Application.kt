package com.eventrelay

import io.ktor.server.application.call
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(factory = Netty, port = 8080) {
        routing {
            get("/") {
                call.respondText("Hello World!\n")
            }
        }
    }.start(wait = true)
}
