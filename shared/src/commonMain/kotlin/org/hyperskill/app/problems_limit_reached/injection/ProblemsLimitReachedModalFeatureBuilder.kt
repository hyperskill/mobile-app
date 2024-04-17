package org.hyperskill.app.problems_limit_reached.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.problems_limit_reached.domain.model.ProblemsLimitReachedModalFeatureParams
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalActionDispatcher
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.Action
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.Message
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalFeature.ViewState
import org.hyperskill.app.problems_limit_reached.presentation.ProblemsLimitReachedModalReducer
import org.hyperskill.app.problems_limit_reached.view.ProblemsLimitReachedModalViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object ProblemsLimitReachedModalFeatureBuilder {
    private const val LOG_TAG = "ProblemsLimitReachedModalFeature"

    fun build(
        analyticInteractor: AnalyticInteractor,
        resourceProvider: ResourceProvider,
        logger: Logger,
        buildVariant: BuildVariant,
        params: ProblemsLimitReachedModalFeatureParams
    ): Feature<ViewState, Message, Action> {
        val problemsLimitReachedModalReducer =
            ProblemsLimitReachedModalReducer(params.stepRoute)
                .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val problemsLimitReachedModalActionDispatcher = ProblemsLimitReachedModalActionDispatcher(
            ActionDispatcherOptions(),
            analyticInteractor
        )

        val viewStateMapper = ProblemsLimitReachedModalViewStateMapper(resourceProvider)

        return ReduxFeature(
            initialState = ProblemsLimitReachedModalFeature.initialState(params.subscription, params.profile),
            reducer = problemsLimitReachedModalReducer
        )
            .wrapWithActionDispatcher(problemsLimitReachedModalActionDispatcher)
            .transformState(viewStateMapper::map)
    }
}