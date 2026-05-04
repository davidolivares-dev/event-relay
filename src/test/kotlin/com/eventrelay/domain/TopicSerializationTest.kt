package com.eventrelay.domain

import kotlinx.datetime.Clock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertEquals

import kotlin.test.assertEquals

class TopicSerializationTest {
    @Test
    fun `topic round trips through json`() {
        val now = Clock.System.now()
        val topic = Topic(id = UUID.randomUUID(), name = "test", description = "test", createdAt = now, updatedAt = now)

        val json: String = Json.encodeToString(topic)
        val parsed: Topic = Json.decodeFromString<Topic>(json)

        assertEquals(expected = topic, actual = parsed)
    }
}