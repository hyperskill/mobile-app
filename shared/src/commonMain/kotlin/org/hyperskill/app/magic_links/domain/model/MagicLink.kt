package org.hyperskill.app.magic_links.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a MagicLink.
 *
 * @property id Unique identifier.
 * @property url Authorization URL (example: https://hyperskill.org/magic/<token>/?next=<next_url>).
 */
@Serializable
data class MagicLink(
    @SerialName("id")
    val id: String,
    @SerialName("url")
    val url: String
)