package org.hyperskill.app.search_results.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    @SerialName("target_type")
    internal val targetTypeValue: String,
    @SerialName("target_id")
    val targetId: Long
) {
    val targetType: SearchResultTargetType?
        get() = SearchResultTargetType.getByValue(targetTypeValue)
}