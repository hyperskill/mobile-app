package org.hyperskill.app.notification_onboarding.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.notification_onboarding.presentation.NotificationOnboardingActionDispatcher
import org.hyperskill.app.notification_onboarding.presentation.NotificationOnboardingFeature.Action
import org.hyperskill.app.notification_onboarding.presentation.NotificationOnboardingFeature.Message
import org.hyperskill.app.notification_onboarding.presentation.NotificationOnboardingFeature.State
import org.hyperskill.app.notification_onboarding.presentation.NotificationOnboardingReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object NotificationOnboardingFeatureBuilder {
    fun build(): Feature<State, Message, Action> {
        val reducer = NotificationOnboardingReducer()
        val actionDispatcher = NotificationOnboardingActionDispatcher(ActionDispatcherOptions())
        return ReduxFeature(State, reducer).wrapWithActionDispatcher(actionDispatcher)
    }
}