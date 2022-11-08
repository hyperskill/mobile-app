package org.hyperskill.app.discussions.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents a Discussion.
 *
 * @property id Unique identifier.
 * @property commentsIds An list of comments ids.
 */
@Serializable
data class Discussion(
    @SerialName("id")
    val id: Long,
    @SerialName("comments")
    val commentsIds: List<Long>
)
