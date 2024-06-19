package org.hyperskill.app.welcome_onboarding.track_details.injection

import org.hyperskill.app.core.flowredux.presentation.wrapWithFlowView
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsViewModel

class PlatformWelcomeOnboardingTrackDetailsComponentImpl(
    private val welcomeOnboardingTrackDetailsComponent: WelcomeOnboardingTrackDetailsComponent
) : PlatformWelcomeOnboardingTrackDetailsComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                WelcomeOnboardingTrackDetailsViewModel::class.java to {
                    WelcomeOnboardingTrackDetailsViewModel(
                        welcomeOnboardingTrackDetailsComponent.welcomeOnboardingTrackDetails.wrapWithFlowView()
                    )
                }
            )
        )
}