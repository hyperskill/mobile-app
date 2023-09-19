package org.hyperskill.app.notification_onboarding.injection

import org.hyperskill.app.notification_onboarding.presentation.NotificationsOnboardingFeature.Action
import org.hyperskill.app.notification_onboarding.presentation.NotificationsOnboardingFeature.Message
import org.hyperskill.app.notification_onboarding.presentation.NotificationsOnboardingFeature.State
import ru.nobird.app.presentation.redux.feature.Feature

interface NotificationsOnboardingComponent {
    val notificationOnboardingFeature: Feature<State, Message, Action>
}