package org.hyperskill.app.topics_repetitions.injection

import org.hyperskill.app.topics_repetitions.domain.interactor.TopicsRepetitionsInteractor
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature
import org.hyperskill.app.topics_repetitions.view.mapper.TopicsRepetitionsViewDataMapper
import ru.nobird.app.presentation.redux.feature.Feature

interface TopicsRepetitionsDataComponent {
    val topicsRepetitionsInteractor: TopicsRepetitionsInteractor
}