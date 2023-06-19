package org.hyperskill.app.topics_repetitions.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.topics_repetitions.view.mapper.TopicsRepetitionsViewDataMapper

interface PlatformTopicsRepetitionComponent {
    val reduxViewModelFactory: ReduxViewModelFactory
    val topicsRepetitionsViewDataMapper: TopicsRepetitionsViewDataMapper
}