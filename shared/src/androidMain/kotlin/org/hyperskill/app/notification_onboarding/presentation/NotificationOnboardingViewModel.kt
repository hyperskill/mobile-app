package org.hyperskill.app.notification_onboarding.presentation

import org.hyperskill.app.notification_onboarding.presentation.NotificationOnboardingFeature.Action.ViewAction
import org.hyperskill.app.notification_onboarding.presentation.NotificationOnboardingFeature.Message
import org.hyperskill.app.notification_onboarding.presentation.NotificationOnboardingFeature.State
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class NotificationOnboardingViewModel(
    reduxViewContainer: ReduxViewContainer<State, Message, ViewAction>
) : ReduxViewModel<State, Message, ViewAction>(reduxViewContainer)