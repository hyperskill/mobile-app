package org.hyperskill.app.topics_repetitions.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.topics_repetitions.presentation.TopicsRepetitionsFeature
import org.hyperskill.app.topics_repetitions.view.mapper.TopicsRepetitionsViewDataMapper
import ru.nobird.app.presentation.redux.feature.Feature

class TopicsRepetitionsComponentImpl(private val appGraph: AppGraph) : TopicsRepetitionsComponent {
    override val topicsRepetitionsFeature: Feature<TopicsRepetitionsFeature.State, TopicsRepetitionsFeature.Message, TopicsRepetitionsFeature.Action>
        get() = TopicsRepetitionsFeatureBuilder.build(
            appGraph.topicsRepetitionsDataComponent.topicsRepetitionsInteractor,
            appGraph.buildTopicsDataComponent().topicsInteractor,
            appGraph.buildProgressesDataComponent().progressesInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor
        )

    override val topicsRepetitionsViewDataMapper: TopicsRepetitionsViewDataMapper
        get() = TopicsRepetitionsViewDataMapper(appGraph.commonComponent.resourceProvider)
}