package org.hyperskill.app.paywall.injection

import org.hyperskill.app.paywall.presentation.PaywallFeature.Action
import org.hyperskill.app.paywall.presentation.PaywallFeature.Message
import org.hyperskill.app.paywall.presentation.PaywallFeature.State
import ru.nobird.app.presentation.redux.feature.Feature

interface PaywallComponent {
    val paywallFeature: Feature<State, Message, Action>
}