package org.hyperskill.app.problems_limit_info.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalFeatureParams
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalActionDispatcher
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.Action
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.Message
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.ViewState
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalReducer
import org.hyperskill.app.problems_limit_info.view.ProblemsLimitInfoModalViewStateMapper
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object ProblemsLimitInfoModalFeatureBuilder {
    private const val LOG_TAG = "ProblemsLimitInfoModalFeature"

    fun build(
        analyticInteractor: AnalyticInteractor,
        resourceProvider: ResourceProvider,
        logger: Logger,
        buildVariant: BuildVariant,
        params: ProblemsLimitInfoModalFeatureParams
    ): Feature<ViewState, Message, Action> {
        val problemsLimitInfoModalReducer =
            ProblemsLimitInfoModalReducer(params.stepRoute)
                .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val problemsLimitInfoModalActionDispatcher = ProblemsLimitInfoModalActionDispatcher(
            ActionDispatcherOptions(),
            analyticInteractor
        )

        val viewStateMapper = ProblemsLimitInfoModalViewStateMapper(resourceProvider)

        return ReduxFeature(
            initialState = ProblemsLimitInfoModalFeature.initialState(params.subscription, params.profile),
            reducer = problemsLimitInfoModalReducer
        )
            .wrapWithActionDispatcher(problemsLimitInfoModalActionDispatcher)
            .transformState(viewStateMapper::map)
    }
}