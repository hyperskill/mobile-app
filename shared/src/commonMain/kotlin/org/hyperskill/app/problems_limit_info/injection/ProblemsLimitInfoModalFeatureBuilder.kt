package org.hyperskill.app.problems_limit_info.injection

import co.touchlab.kermit.Logger
import kotlinx.datetime.Clock
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.wrapWithAnalyticLogger
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalFeatureParams
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.Action
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.InternalAction
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.Message
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalFeature.ViewState
import org.hyperskill.app.problems_limit_info.presentation.ProblemsLimitInfoModalReducer
import org.hyperskill.app.problems_limit_info.view.ProblemsLimitInfoModalViewStateMapper
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object ProblemsLimitInfoModalFeatureBuilder {
    private const val LOG_TAG = "ProblemsLimitInfoModalFeature"

    fun build(
        analyticInteractor: AnalyticInteractor,
        resourceProvider: ResourceProvider,
        dateFormatter: SharedDateFormatter,
        logger: Logger,
        buildVariant: BuildVariant,
        params: ProblemsLimitInfoModalFeatureParams
    ): Feature<ViewState, Message, Action> {
        val problemsLimitInfoModalReducer =
            ProblemsLimitInfoModalReducer(params.context.analyticRoute)
                .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val viewStateMapper = ProblemsLimitInfoModalViewStateMapper(resourceProvider, dateFormatter)

        if (params.subscription.stepsLimitResetTime != null &&
            params.subscription.stepsLimitResetTime < Clock.System.now()
        ) {
            logger.e(
                """
                stepsLimitResetTime is in the past:
                subscription: {$params.subscription},
                chargeLimitsStrategy: ${params.chargeLimitsStrategy},
                launchSource: ${params.context.launchSource}
                """.trimIndent()
            )
        }

        return ReduxFeature(
            initialState = ProblemsLimitInfoModalFeature.initialState(
                params.subscription,
                params.chargeLimitsStrategy,
                params.context.launchSource
            ),
            reducer = problemsLimitInfoModalReducer
        )
            .transformState(viewStateMapper::map)
            .wrapWithAnalyticLogger(analyticInteractor) {
                (it as? InternalAction.LogAnalyticEvent)?.event
            }
    }
}