package org.hyperskill.app.manage_subscription.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.wrapWithAnalyticLogger
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionActionDispatcher
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Action
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Message
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.State
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.ViewState
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionReducer
import org.hyperskill.app.manage_subscription.view.mapper.ManageSubscriptionViewStateMapper
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object ManageSubscriptionFeatureBuilder {
    private const val LOG_TAG = "ManageSubscriptionFeature"

    fun build(
        analyticInteractor: AnalyticInteractor,
        currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
        purchaseInteractor: PurchaseInteractor,
        sentryInteractor: SentryInteractor,
        resourceProvider: ResourceProvider,
        dateFormatter: SharedDateFormatter,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<ViewState, Message, Action> {
        val manageSubscriptionReducer =
            ManageSubscriptionReducer()
                .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val manageSubscriptionActionDispatcher = ManageSubscriptionActionDispatcher(
            config = ActionDispatcherOptions(),
            currentSubscriptionStateRepository = currentSubscriptionStateRepository,
            purchaseInteractor = purchaseInteractor,
            sentryInteractor = sentryInteractor,
            logger = logger.withTag(LOG_TAG)
        )

        val viewStateMapper = ManageSubscriptionViewStateMapper(resourceProvider, dateFormatter)

        return ReduxFeature(
            initialState = State.Idle,
            reducer = manageSubscriptionReducer
        )
            .wrapWithActionDispatcher(manageSubscriptionActionDispatcher)
            .transformState(viewStateMapper::map)
            .wrapWithAnalyticLogger(analyticInteractor) {
                (it as? ManageSubscriptionFeature.InternalAction.LogAnalyticEvent)?.analyticEvent
            }
    }
}