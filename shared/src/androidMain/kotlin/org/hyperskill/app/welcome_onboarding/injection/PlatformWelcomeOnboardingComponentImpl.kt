package org.hyperskill.app.welcome_onboarding.injection

import org.hyperskill.app.core.flowredux.presentation.wrapWithFlowView
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingViewModel
import org.hyperskill.app.welcome_onboarding.root.injection.WelcomeOnboardingComponent

class PlatformWelcomeOnboardingComponentImpl(
    private val welcomeOnboardingComponent: WelcomeOnboardingComponent
) : PlatformWelcomeOnboardingComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                WelcomeOnboardingViewModel::class.java to {
                    WelcomeOnboardingViewModel(
                        welcomeOnboardingComponent.welcomeOnboardingFeature.wrapWithFlowView()
                    )
                }
            )
        )
}