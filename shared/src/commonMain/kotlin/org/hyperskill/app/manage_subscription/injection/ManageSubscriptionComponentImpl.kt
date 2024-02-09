package org.hyperskill.app.manage_subscription.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Action
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Message
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class ManageSubscriptionComponentImpl(
    private val appGraph: AppGraph
) : ManageSubscriptionComponent {
    override val manageSubscriptionFeature: Feature<ViewState, Message, Action>
        get() = ManageSubscriptionFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            currentSubscriptionStateRepository = appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository,
            purchaseInteractor = appGraph.buildPurchaseComponent().purchaseInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            resourceProvider = appGraph.commonComponent.resourceProvider,
            dateFormatter = appGraph.commonComponent.dateFormatter,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}