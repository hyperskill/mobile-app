package org.hyperskill.app.notification_onboarding.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor
import org.hyperskill.app.notification_onboarding.presentation.NotificationsOnboardingActionDispatcher
import org.hyperskill.app.notification_onboarding.presentation.NotificationsOnboardingFeature.Action
import org.hyperskill.app.notification_onboarding.presentation.NotificationsOnboardingFeature.Message
import org.hyperskill.app.notification_onboarding.presentation.NotificationsOnboardingFeature.State
import org.hyperskill.app.notification_onboarding.presentation.NotificationsOnboardingReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object NotificationsOnboardingFeatureBuilder {
    fun build(
        analyticInteractor: AnalyticInteractor,
        notificationInteractor: NotificationInteractor
    ): Feature<State, Message, Action> {
        val reducer = NotificationsOnboardingReducer()
        val actionDispatcher = NotificationsOnboardingActionDispatcher(
            config = ActionDispatcherOptions(),
            analyticInteractor = analyticInteractor,
            notificationInteractor = notificationInteractor
        )
        return ReduxFeature(State, reducer).wrapWithActionDispatcher(actionDispatcher)
    }
}