package org.hyperskill.app.android.topics_repetitions.view.model

import org.hyperskill.app.topics_repetitions.view.model.TopicToRepeat

sealed interface TopicsRepetitionListItem {
    data class Topic(val topic: TopicToRepeat) : TopicsRepetitionListItem
    object LoadingStub : TopicsRepetitionListItem
}