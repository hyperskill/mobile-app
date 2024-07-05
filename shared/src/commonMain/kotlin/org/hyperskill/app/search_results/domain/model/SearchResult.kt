package org.hyperskill.app.search_results.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.content_type.domain.model.ContentType

@Serializable
data class SearchResult(
    @SerialName("target_type")
    val targetType: ContentType = ContentType.UNKNOWN,
    @SerialName("target_id")
    val targetId: Long
)