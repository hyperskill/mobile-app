package org.hyperskill.app.paywall.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.paywall.presentation.PaywallFeature.Action
import org.hyperskill.app.paywall.presentation.PaywallFeature.Message
import org.hyperskill.app.paywall.presentation.PaywallFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

class PaywallComponentImpl(
    private val paywallTransitionSource: PaywallTransitionSource,
    private val appGraph: AppGraph
) : PaywallComponent {
    override val paywallFeature: Feature<ViewState, Message, Action>
        get() = PaywallFeatureBuilder.build(
            paywallTransitionSource = paywallTransitionSource,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            purchaseInteractor = appGraph.buildPurchaseComponent().purchaseInteractor,
            resourceProvider = appGraph.commonComponent.resourceProvider,
            subscriptionsRepository = appGraph.buildSubscriptionsDataComponent().subscriptionsRepository,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            currentSubscriptionStateRepository = appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}