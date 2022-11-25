package org.hyperskill.app.topics_repetitions.injection

import org.hyperskill.app.topics_repetitions.domain.interactor.TopicsRepetitionsInteractor

interface TopicsRepetitionsDataComponent {
    val topicsRepetitionsInteractor: TopicsRepetitionsInteractor
}