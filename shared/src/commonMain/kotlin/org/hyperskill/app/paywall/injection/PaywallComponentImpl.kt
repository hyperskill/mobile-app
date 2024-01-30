package org.hyperskill.app.paywall.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.paywall.presentation.PaywallFeature.Action
import org.hyperskill.app.paywall.presentation.PaywallFeature.Message
import org.hyperskill.app.paywall.presentation.PaywallFeature.State
import ru.nobird.app.presentation.redux.feature.Feature

class PaywallComponentImpl(
    private val paywallTransitionSource: PaywallTransitionSource,
    private val appGraph: AppGraph
) : PaywallComponent {
    override val paywallFeature: Feature<State, Message, Action>
        get() = PaywallFeatureBuilder.build(
            paywallTransitionSource = paywallTransitionSource,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}