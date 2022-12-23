package org.hyperskill.app.topics_repetitions.view.model

data class TopicsRepetitionsViewData(
    val repetitionsStatus: RepetitionsStatus,
    val chartData: List<Pair<String, Int>>,
    val chartDescription: String,
    val repeatBlockTitle: String,
    val trackTopicsTitle: String,
    val topicsToRepeatFromCurrentTrack: List<TopicToRepeat>,
    val topicsToRepeatFromOtherTracks: List<TopicToRepeat>,
    val showMoreButtonState: ShowMoreButtonState,
    val topicsToRepeatWillLoadedCount: Int
)
