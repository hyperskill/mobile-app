package org.hyperskill.app.topics_to_discover_next.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextActionDispatcher
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextReducer

class TopicsToDiscoverNextComponentImpl(
    private val appGraph: AppGraph
) : TopicsToDiscoverNextComponent {
    override val topicsToDiscoverNextReducer: TopicsToDiscoverNextReducer
        get() = TopicsToDiscoverNextReducer()

    override val topicsToDiscoverNextActionDispatcher: TopicsToDiscoverNextActionDispatcher
        get() = TopicsToDiscoverNextActionDispatcher(
            ActionDispatcherOptions(),
            appGraph.buildTopicsToDiscoverNextDataComponent().topicsToDiscoverNextInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.analyticComponent.analyticInteractor
        )
}