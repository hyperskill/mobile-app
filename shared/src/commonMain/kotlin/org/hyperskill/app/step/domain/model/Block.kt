package org.hyperskill.app.step.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Block(
    @SerialName("name")
    val name: String,
    @SerialName("text")
    val text: String,
    @SerialName("table_of_contents")
    val tableOfContents: List<TableOfContentsEntry>
)
