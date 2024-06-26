package org.hyperskill.app.paywall.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.paywall.presentation.PaywallFeature.Action
import org.hyperskill.app.paywall.presentation.PaywallFeature.Message
import org.hyperskill.app.paywall.presentation.PaywallFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class PaywallComponentImpl(
    private val paywallTransitionSource: PaywallTransitionSource,
    private val appGraph: AppGraph
) : PaywallComponent {
    override val paywallFeature: Feature<ViewState, Message, Action>
        get() = PaywallFeatureBuilder.build(
            paywallTransitionSource = paywallTransitionSource,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            purchaseInteractor = appGraph.buildPurchaseComponent().purchaseInteractor,
            resourceProvider = appGraph.commonComponent.resourceProvider,
            subscriptionsRepository = appGraph.subscriptionDataComponent.subscriptionsRepository,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            currentSubscriptionStateRepository = appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository,
            platformType = appGraph.commonComponent.platform.platformType,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}