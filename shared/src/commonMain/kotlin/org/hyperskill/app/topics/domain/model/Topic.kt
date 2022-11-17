package org.hyperskill.app.topics.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Topic(
    @SerialName("id")
    val id: Long,
    @SerialName("theory")
    val theory: Long,
    @SerialName("title")
    val title: String,
)
