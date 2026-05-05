package com.eventrelay.repository

import com.eventrelay.domain.Topic
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryTopicRepository : TopicRepository {

    private val topics = ConcurrentHashMap<UUID, Topic>()

    override fun create(topic: Topic): Topic {
        val existingTopic = topics.putIfAbsent(topic.id, topic)
        if (existingTopic != null) {
            throw IllegalStateException("Topic with id ${topic.id} already exists")
        }
        return topic
    }

    override fun findById(id: UUID): Topic? {
        return topics[id]
    }

    override fun findAll(): List<Topic> {
        return topics.values.toList()
    }

    override fun delete(id: UUID): Boolean {
        return topics.remove(id) != null
    }
}