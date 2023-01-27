package org.hyperskill.app.topics_repetitions.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Action
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.Message
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature.State
import org.hyperskill.app.topics_repetitions.view.mapper.TopicsRepetitionsViewDataMapper
import ru.nobird.app.presentation.redux.feature.Feature

class TopicsRepetitionsComponentImpl(private val appGraph: AppGraph) : TopicsRepetitionsComponent {
    override val topicsRepetitionsFeature: Feature<State, Message, Action>
        get() = TopicsRepetitionsFeatureBuilder.build(
            appGraph.buildTopicsRepetitionsDataComponent().topicsRepetitionsInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.topicsRepetitionsFlowDataComponent.topicRepeatedFlow,
            appGraph.submissionDataComponent.submissionRepository
        )

    override val topicsRepetitionsViewDataMapper: TopicsRepetitionsViewDataMapper
        get() = TopicsRepetitionsViewDataMapper(appGraph.commonComponent.resourceProvider)
}