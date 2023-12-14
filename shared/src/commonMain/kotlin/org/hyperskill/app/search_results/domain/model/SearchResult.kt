package org.hyperskill.app.search_results.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    @SerialName("target_type")
    val targetType: SearchResultTargetType = SearchResultTargetType.UNKNOWN,
    @SerialName("target_id")
    val targetId: Long
)