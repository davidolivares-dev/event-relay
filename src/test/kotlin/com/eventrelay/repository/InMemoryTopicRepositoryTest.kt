package com.eventrelay.repository

import com.eventrelay.domain.Topic
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.UUID
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class InMemoryTopicRepositoryTest {

    private fun sampleTopic(
        id: UUID = UUID.randomUUID(),
        name: String = "test.topic",
        description: String? = null,
        createdAt: Instant = Clock.System.now(),
        updatedAt: Instant = createdAt
    ) = Topic(id, name, description, createdAt, updatedAt)

    @Test
    fun `create stores topic and returns it`() {
        val repo = InMemoryTopicRepository()
        val topic = sampleTopic()

        val returned = repo.create(topic)

        assertEquals(topic, returned)
        assertEquals(topic, repo.findById(topic.id))
    }

    @Test
    fun `create throws when id already exists`() {
        val repo = InMemoryTopicRepository()
        val topic = sampleTopic()
        repo.create(topic)

        assertFailsWith<IllegalStateException> {
            repo.create(topic)
        }
    }

    @Test
    fun `findById returns null when topic does not exist`() {
        val repo = InMemoryTopicRepository()
        val topic = sampleTopic()

        assertNull(repo.findById(topic.id))
    }

    @Test
    fun `findById returns topic when present`() {
        val repo = InMemoryTopicRepository()
        val topic = sampleTopic()
        repo.create(topic)

        assertEquals(topic, repo.findById(topic.id))
    }

    @Test
    fun `findAll returns empty list when repo is empty`() {
        val repo = InMemoryTopicRepository()

        assertEquals(0, repo.findAll().size)
    }

    @Test
    fun `findAll returns all stored topics`() {
        val repo = InMemoryTopicRepository()
        val topic = repo.create(sampleTopic())
        val topic2 = repo.create(sampleTopic(name = "test.topic2"))
        val topic3 = repo.create(sampleTopic(name = "test.topic3"))

        assertContains(repo.findAll(), topic)
        assertContains(repo.findAll(), topic2)
        assertContains(repo.findAll(), topic3)
    }

    @Test
    fun `delete returns true and removes when present`() {
        val repo = InMemoryTopicRepository()
        val topic = sampleTopic()
        repo.create(topic)

        assertTrue(repo.delete(topic.id))
    }

    @Test
    fun `delete returns false when absent`() {
        val repo = InMemoryTopicRepository()
        val topic = sampleTopic()

        assertFalse(repo.delete(topic.id))
    }
}