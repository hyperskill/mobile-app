package org.hyperskill.app.topics_repetitions.injection

import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Action
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Message
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.State
import org.hyperskill.app.topics_repetitions.view.mapper.TopicsRepetitionsViewDataMapper
import ru.nobird.app.presentation.redux.feature.Feature

interface TopicsRepetitionsComponent {
    val topicsRepetitionsFeature: Feature<State, Message, Action>

    val topicsRepetitionsViewDataMapper: TopicsRepetitionsViewDataMapper
}