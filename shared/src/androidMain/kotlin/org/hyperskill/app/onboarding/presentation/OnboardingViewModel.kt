package org.hyperskill.app.onboarding.presentation

import ru.nobird.android.view.redux.viewmodel.ReduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxViewContainer

class OnboardingViewModel(
    reduxViewContainer: ReduxViewContainer<
        OnboardingFeature.State, OnboardingFeature.Message, OnboardingFeature.Action.ViewAction>
) : ReduxViewModel<
    OnboardingFeature.State, OnboardingFeature.Message, OnboardingFeature.Action.ViewAction>(reduxViewContainer)