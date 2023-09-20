package org.hyperskill.app.notifications_onboarding.presentation

import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Action.ViewAction
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.Message
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature.State
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class NotificationOnboardingViewModel(
    reduxViewContainer: ReduxViewContainer<State, Message, ViewAction>
) : ReduxViewModel<State, Message, ViewAction>(reduxViewContainer)