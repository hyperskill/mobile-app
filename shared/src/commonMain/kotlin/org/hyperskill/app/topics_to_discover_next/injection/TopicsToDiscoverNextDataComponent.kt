package org.hyperskill.app.topics_to_discover_next.injection

import org.hyperskill.app.topics_to_discover_next.domain.interactor.TopicsToDiscoverNextInteractor

interface TopicsToDiscoverNextDataComponent {
    val topicsToDiscoverNextInteractor: TopicsToDiscoverNextInteractor
}