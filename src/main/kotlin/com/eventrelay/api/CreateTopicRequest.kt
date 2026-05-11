package com.eventrelay.api

import kotlinx.serialization.Serializable

@Serializable
data class CreateTopicRequest(
    val name: String,
    val description: String? = null
)
