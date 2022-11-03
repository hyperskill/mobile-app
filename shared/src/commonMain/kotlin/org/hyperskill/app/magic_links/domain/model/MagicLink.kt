package org.hyperskill.app.magic_links.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MagicLink(
    @SerialName("id")
    val id: String,
    @SerialName("url")
    val url: String
)
