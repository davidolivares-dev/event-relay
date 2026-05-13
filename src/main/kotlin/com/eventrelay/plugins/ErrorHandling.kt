@file:OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
package com.eventrelay.plugins

import com.eventrelay.api.ErrorResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import kotlinx.serialization.MissingFieldException
import kotlinx.serialization.SerializationException

fun Application.configureErrorHandling() {
    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            val rootCause = generateSequence(cause as Throwable?) { it.cause }.last()
            val errorCode = when (rootCause) {
                is MissingFieldException -> "validation_failed"
                is SerializationException -> "invalid_json"
                else -> "bad_request"
            }
            call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse(
                    error = errorCode,
                    message = rootCause.message ?: cause.message ?: "Bad request"
                )
            )
        }
        exception<IllegalStateException> { call, cause ->
            call.respond(
                HttpStatusCode.Conflict,
                ErrorResponse(
                    error = "conflict",
                    message = cause.message ?: "Conflict"
                )
            )
        }
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                ErrorResponse(
                    error = "internal_error",
                    message = "An unexpected error occurred"
                )
            )
        }
        status(HttpStatusCode.NotFound) { call, status ->
            call.respond(status, ErrorResponse(error = "not_found", message = "Resource not found"))
        }
        status(HttpStatusCode.BadRequest) { call, status ->
            call.respond(status, ErrorResponse(error = "bad_request", message = "Bad request"))
        }
    }
}