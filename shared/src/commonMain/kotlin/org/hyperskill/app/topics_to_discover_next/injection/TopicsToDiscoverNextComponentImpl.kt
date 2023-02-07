package org.hyperskill.app.topics_to_discover_next.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.topics_to_discover_next.domain.model.TopicsToDiscoverNextScreen
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextActionDispatcher
import org.hyperskill.app.topics_to_discover_next.presentation.TopicsToDiscoverNextReducer

class TopicsToDiscoverNextComponentImpl(
    private val appGraph: AppGraph,
    private val screen: TopicsToDiscoverNextScreen
) : TopicsToDiscoverNextComponent {
    override val topicsToDiscoverNextReducer: TopicsToDiscoverNextReducer
        get() = TopicsToDiscoverNextReducer(screen)

    override val topicsToDiscoverNextActionDispatcher: TopicsToDiscoverNextActionDispatcher
        get() = TopicsToDiscoverNextActionDispatcher(
            ActionDispatcherOptions(),
            appGraph.buildTopicsToDiscoverNextDataComponent().topicsToDiscoverNextInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.stepCompletionFlowDataComponent.topicCompletedFlow
        )
}