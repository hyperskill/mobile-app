package org.hyperskill.app.search_results.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class SearchResultTargetType {
    @SerialName("topic")
    TOPIC,

    UNKNOWN
}