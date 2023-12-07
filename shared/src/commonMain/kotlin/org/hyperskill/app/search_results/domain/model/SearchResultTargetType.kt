package org.hyperskill.app.search_results.domain.model

enum class SearchResultTargetType(val value: String) {
    TOPIC("topic");

    companion object {
        private val VALUES: Array<SearchResultTargetType> = values()

        fun getByValue(value: String): SearchResultTargetType? =
            VALUES.firstOrNull { it.value == value }
    }
}