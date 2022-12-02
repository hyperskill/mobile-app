package org.hyperskill.app.topics_repetitions.view.model

sealed interface RepetitionsStatus {
    object AllTopicsRepeated : RepetitionsStatus
    object RecommendedTopicsRepeated : RepetitionsStatus
    data class RecommendedTopicsAvailable(
        val recommendedRepetitionsCount: Int,
        val repeatButtonText: String?
    ) : RepetitionsStatus
}