package org.hyperskill.app.notification_onboarding.injection

import org.hyperskill.app.notification_onboarding.presentation.NotificationOnboardingFeature.Action
import org.hyperskill.app.notification_onboarding.presentation.NotificationOnboardingFeature.Message
import org.hyperskill.app.notification_onboarding.presentation.NotificationOnboardingFeature.State
import ru.nobird.app.presentation.redux.feature.Feature

class NotificationOnboardingComponentImpl : NotificationOnboardingComponent {
    override val notificationOnboardingFeature: Feature<State, Message, Action>
        get() = NotificationOnboardingFeatureBuilder.build()
}