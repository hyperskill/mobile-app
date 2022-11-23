package org.hyperskill.app.topics_repetitions.injection

import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature
import org.hyperskill.app.topics_repetitions.view.mapper.TopicsRepetitionsViewDataMapper
import ru.nobird.app.presentation.redux.feature.Feature

interface TopicsRepetitionsComponent {
    val topicsRepetitionsFeature: Feature<TopicsRepetitionsFeature.State, TopicsRepetitionsFeature.Message, TopicsRepetitionsFeature.Action>

    val topicsRepetitionsViewDataMapper: TopicsRepetitionsViewDataMapper
}