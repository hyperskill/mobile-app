package org.hyperskill.app.topic_completed_modal.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.topic_completed_modal.domain.model.TopicCompletedModalFeatureParams
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalActionDispatcher
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.Action
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.Message
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.ViewState
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalReducer
import org.hyperskill.app.topic_completed_modal.view.TopicCompletedModalViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object TopicCompletedModalFeatureBuilder {
    private const val LOG_TAG = "TopicCompletedModalFeature"

    fun build(
        analyticInteractor: AnalyticInteractor,
        resourceProvider: ResourceProvider,
        logger: Logger,
        buildVariant: BuildVariant,
        params: TopicCompletedModalFeatureParams
    ): Feature<ViewState, Message, Action> {
        val topicCompletedModalReducer =
            TopicCompletedModalReducer(analyticRoute = params.stepRoute.analyticRoute)
                .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val topicCompletedModalActionDispatcher = TopicCompletedModalActionDispatcher(
            ActionDispatcherOptions(),
            analyticInteractor
        )

        val viewStateMapper = TopicCompletedModalViewStateMapper(resourceProvider)

        return ReduxFeature(
            initialState = TopicCompletedModalFeature.initialState(params),
            reducer = topicCompletedModalReducer
        )
            .wrapWithActionDispatcher(topicCompletedModalActionDispatcher)
            .transformState(viewStateMapper::map)
    }
}