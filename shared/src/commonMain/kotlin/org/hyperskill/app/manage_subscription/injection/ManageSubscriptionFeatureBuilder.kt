package org.hyperskill.app.manage_subscription.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionActionDispatcher
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Action
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Message
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.State
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object ManageSubscriptionFeatureBuilder {
    private const val LOG_TAG = "ManageSubscriptionFeature"

    fun build(
        analyticInteractor: AnalyticInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<State, Message, Action> {
        val manageSubscriptionReducer =
            ManageSubscriptionReducer()
                .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val manageSubscriptionActionDispatcher = ManageSubscriptionActionDispatcher(
            ActionDispatcherOptions(),
            analyticInteractor
        )

        return ReduxFeature(
            initialState = State.Idle,
            reducer = manageSubscriptionReducer
        ).wrapWithActionDispatcher(manageSubscriptionActionDispatcher)
    }
}