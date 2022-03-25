package org.hyperskill.app.step.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TableOfContentsEntry(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    val title: String
)
