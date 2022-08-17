package org.hyperskill.app.onboarding.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.onboarding.presentation.OnboardingViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformOnboardingComponentImpl(private val onboardingComponent: OnboardingComponent) : PlatformOnboardingComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(mapOf(OnboardingViewModel::class.java to { OnboardingViewModel(onboardingComponent.onboardingFeature.wrapWithViewContainer()) }))
}