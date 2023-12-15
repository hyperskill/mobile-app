package org.hyperskill.app.notifications_onboarding.injection

import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Action
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Message
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

interface NotificationsOnboardingComponent {
    val notificationsOnboardingFeature: Feature<ViewState, Message, Action>
}