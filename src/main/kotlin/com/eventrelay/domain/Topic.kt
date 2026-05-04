package com.eventrelay.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class Topic(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val description: String?,
    @Serializable(with = InstantIso8601Serializer::class)
    val createdAt: Instant,
    @Serializable(with = InstantIso8601Serializer::class)
    val updatedAt: Instant
)
