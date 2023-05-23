package org.hyperskill.app.android.topics_repetitions.view.model

import org.hyperskill.app.topics_repetitions.view.model.ShowMoreButtonState
import org.hyperskill.app.topics_repetitions.view.model.TopicToRepeat

data class TopicsRepetitionListState(
    val repeatBlockTitle: String,
    val trackTopicsTitle: String,
    val topicsToRepeatFromCurrentTrack: List<TopicToRepeat>,
    val topicsToRepeatFromOtherTracks: List<TopicToRepeat>,
    val showMoreButtonState: ShowMoreButtonState,
    val topicsToRepeatWillLoadedCount: Int
)