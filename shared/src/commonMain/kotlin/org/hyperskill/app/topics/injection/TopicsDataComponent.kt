package org.hyperskill.app.topics.injection

import org.hyperskill.app.topics.domain.interactor.TopicsInteractor

interface TopicsDataComponent {
    val topicsInteractor: TopicsInteractor
}