package org.hyperskill.app.notification_onboarding.presentation

import org.hyperskill.app.notification_onboarding.presentation.NotificationOnboardingFeature.Action
import org.hyperskill.app.notification_onboarding.presentation.NotificationOnboardingFeature.Message
import org.hyperskill.app.notification_onboarding.presentation.NotificationOnboardingFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class NotificationOnboardingReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> {
        TODO("Not yet implemented")
    }
}