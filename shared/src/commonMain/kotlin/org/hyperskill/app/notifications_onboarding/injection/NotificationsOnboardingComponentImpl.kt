package org.hyperskill.app.notifications_onboarding.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Action
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Message
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.ViewState
import ru.nobird.app.presentation.redux.feature.Feature

internal class NotificationsOnboardingComponentImpl(
    private val appGraph: AppGraph
) : NotificationsOnboardingComponent {
    override val notificationsOnboardingFeature: Feature<ViewState, Message, Action>
        get() = NotificationsOnboardingFeatureBuilder.build(
            notificationInteractor = appGraph.buildNotificationComponent().notificationInteractor,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}