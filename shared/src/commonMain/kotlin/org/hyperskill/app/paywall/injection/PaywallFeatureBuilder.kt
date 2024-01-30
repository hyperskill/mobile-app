package org.hyperskill.app.paywall.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.paywall.presentation.PaywallActionDispatcher
import org.hyperskill.app.paywall.presentation.PaywallFeature.Action
import org.hyperskill.app.paywall.presentation.PaywallFeature.Message
import org.hyperskill.app.paywall.presentation.PaywallFeature.State
import org.hyperskill.app.paywall.presentation.PaywallReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object PaywallFeatureBuilder {
    private const val LOG_TAG = "PaywallFeature"

    fun build(
        paywallTransitionSource: PaywallTransitionSource,
        analyticInteractor: AnalyticInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<State, Message, Action> {
        val paywallReducer =
            PaywallReducer(paywallTransitionSource)
                .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val paywallActionDispatcher = PaywallActionDispatcher(
            ActionDispatcherOptions(),
            analyticInteractor
        )

        return ReduxFeature(
            initialState = State,
            reducer = paywallReducer
        ).wrapWithActionDispatcher(paywallActionDispatcher)
    }
}