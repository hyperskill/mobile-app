package org.hyperskill.app.paywall.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.domain.platform.PlatformType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.paywall.presentation.PaywallActionDispatcher
import org.hyperskill.app.paywall.presentation.PaywallFeature
import org.hyperskill.app.paywall.presentation.PaywallFeature.Action
import org.hyperskill.app.paywall.presentation.PaywallFeature.Message
import org.hyperskill.app.paywall.presentation.PaywallFeature.ViewState
import org.hyperskill.app.paywall.presentation.PaywallReducer
import org.hyperskill.app.paywall.view.PaywallViewStateMapper
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.subscriptions.domain.repository.SubscriptionsRepository
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object PaywallFeatureBuilder {
    private const val LOG_TAG = "PaywallFeature"

    fun build(
        paywallTransitionSource: PaywallTransitionSource,
        analyticInteractor: AnalyticInteractor,
        purchaseInteractor: PurchaseInteractor,
        resourceProvider: ResourceProvider,
        subscriptionsRepository: SubscriptionsRepository,
        currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
        platformType: PlatformType,
        sentryInteractor: SentryInteractor,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<ViewState, Message, Action> {
        val paywallReducer = PaywallReducer(
            paywallTransitionSource = paywallTransitionSource,
            resourceProvider = resourceProvider
        )
            .wrapWithLogger(buildVariant, logger, LOG_TAG)

        val paywallActionDispatcher = PaywallActionDispatcher(
            config = ActionDispatcherOptions(),
            analyticInteractor = analyticInteractor,
            purchaseInteractor = purchaseInteractor,
            subscriptionsRepository = subscriptionsRepository,
            sentryInteractor = sentryInteractor,
            currentSubscriptionStateRepository = currentSubscriptionStateRepository,
            logger = logger.withTag(LOG_TAG)
        )

        val viewStateMapper = PaywallViewStateMapper(resourceProvider, platformType)

        return ReduxFeature(
            initialState = PaywallFeature.State.Idle,
            reducer = paywallReducer
        )
            .wrapWithActionDispatcher(paywallActionDispatcher)
            .transformState { state ->
                viewStateMapper.map(state, paywallTransitionSource)
            }
    }
}