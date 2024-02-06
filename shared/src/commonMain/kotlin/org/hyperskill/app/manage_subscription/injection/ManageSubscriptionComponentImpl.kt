package org.hyperskill.app.manage_subscription.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Action
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Message
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.State
import ru.nobird.app.presentation.redux.feature.Feature

class ManageSubscriptionComponentImpl(
    private val appGraph: AppGraph
) : ManageSubscriptionComponent {
    override val manageSubscriptionFeature: Feature<State, Message, Action>
        get() = ManageSubscriptionFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}