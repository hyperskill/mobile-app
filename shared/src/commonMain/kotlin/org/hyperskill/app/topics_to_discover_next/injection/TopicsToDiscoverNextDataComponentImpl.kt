package org.hyperskill.app.topics_to_discover_next.injection

import org.hyperskill.app.topics_to_discover_next.domain.interactor.TopicsToDiscoverNextInteractor

class TopicsToDiscoverNextDataComponentImpl : TopicsToDiscoverNextDataComponent {
    override val topicsToDiscoverNextInteractor: TopicsToDiscoverNextInteractor
        get() = TopicsToDiscoverNextInteractor()
}