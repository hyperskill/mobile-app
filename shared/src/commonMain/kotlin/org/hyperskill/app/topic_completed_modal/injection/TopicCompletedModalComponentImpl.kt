package org.hyperskill.app.topic_completed_modal.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.topic_completed_modal.domain.model.TopicCompletedModalFeatureParams
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.Action
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.Message
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class TopicCompletedModalComponentImpl(
    private val appGraph: AppGraph,
    private val params: TopicCompletedModalFeatureParams
) : TopicCompletedModalComponent {
    override val topicCompletedModalFeature: Feature<ViewState, Message, Action>
        get() = TopicCompletedModalFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            resourceProvider = appGraph.commonComponent.resourceProvider,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant,
            params = params
        )
}