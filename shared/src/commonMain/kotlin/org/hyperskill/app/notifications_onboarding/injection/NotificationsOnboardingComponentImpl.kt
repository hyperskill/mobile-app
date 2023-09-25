package org.hyperskill.app.notifications_onboarding.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Action
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Message
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.State
import ru.nobird.app.presentation.redux.feature.Feature

internal class NotificationsOnboardingComponentImpl(
    private val appGraph: AppGraph
) : NotificationsOnboardingComponent {
    override val notificationsOnboardingFeature: Feature<State, Message, Action>
        get() = NotificationsOnboardingFeatureBuilder.build(
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            notificationInteractor = appGraph.buildNotificationComponent().notificationInteractor
        )
}