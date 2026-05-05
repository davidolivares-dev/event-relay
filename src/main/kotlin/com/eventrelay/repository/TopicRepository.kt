package com.eventrelay.repository

import com.eventrelay.domain.Topic
import java.util.UUID

interface TopicRepository {
    fun create(topic: Topic): Topic
    fun findById(id: UUID): Topic?
    fun findAll(): List<Topic>
    fun delete(id: UUID): Boolean
}