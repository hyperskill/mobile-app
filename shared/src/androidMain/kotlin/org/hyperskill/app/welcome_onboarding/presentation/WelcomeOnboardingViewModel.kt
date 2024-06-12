package org.hyperskill.app.welcome_onboarding.presentation

import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Action
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.Message
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature.State
import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class WelcomeOnboardingViewModel(
    reduxViewContainer: ReduxViewContainer<State, Message, Action>
) : ReduxViewModel<State, Message, Action>(reduxViewContainer)