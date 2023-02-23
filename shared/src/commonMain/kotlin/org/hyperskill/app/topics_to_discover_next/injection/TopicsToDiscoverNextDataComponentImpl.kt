package org.hyperskill.app.topics_to_discover_next.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.topics_to_discover_next.domain.interactor.TopicsToDiscoverNextInteractor

class TopicsToDiscoverNextDataComponentImpl(private val appGraph: AppGraph) : TopicsToDiscoverNextDataComponent {
    override val topicsToDiscoverNextInteractor: TopicsToDiscoverNextInteractor
        get() = TopicsToDiscoverNextInteractor(
            appGraph.buildProfileDataComponent().profileRepository,
            appGraph.buildLearningActivitiesDataComponent().learningActivitiesRepository,
            appGraph.buildTopicsDataComponent().topicsRepository,
            appGraph.buildProgressesDataComponent().progressesRepository,
            appGraph.buildTrackDataComponent().trackInteractor
        )
}