package org.hyperskill.app.topics_repetitions.view.model

data class TopicsRepetitionsViewData(
    val recommendedTopicsToRepeatCount: Int,
    val repeatButtonText: String?,
    val chartData: List<Pair<String, Int>>,
    val chartDescription: String,
    val repeatBlockTitle: String,
    val trackTopicsTitle: String,
    val topicsToRepeat: List<TopicToRepeat>,
    val showMoreButtonState: ShowMoreButtonState
)
