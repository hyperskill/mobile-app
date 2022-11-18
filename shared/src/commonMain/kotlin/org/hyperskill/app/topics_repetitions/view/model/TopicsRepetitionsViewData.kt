package org.hyperskill.app.topics_repetitions.view.model

import org.hyperskill.app.topics_repetitions.domain.model.TopicToRepeat
import org.hyperskill.app.topics_repetitions.view.model.ShowMoreButtonState

data class TopicsRepetitionsViewData(
    val recommendedTopicsToRepeatCount: String,
    val recommendedTopicsToRepeatText: String,
    val chartData: Map<String, Int>,
    val chartDescription: String,
    val repeatBlockTitle: String,
    val trackTopicsTitle: String,
    val topicsToRepeat: List<TopicToRepeat>,
    val showMoreButtonState: ShowMoreButtonState
)
