package org.hyperskill.app.providers.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Provider(
    @SerialName("id")
    val id: Long,
    @SerialName("title")
    val title: String = "",
    @SerialName("description")
    val description: String = ""
)