package org.hyperskill.app.first_problem_onboarding.injection

import org.hyperskill.app.core.flowredux.presentation.wrapWithFlowView
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingViewModel

class PlatformFirstProblemOnboardingComponentImpl(
    private val isNewUserMode: Boolean,
    private val firstProblemOnboardingComponent: FirstProblemOnboardingComponent
) : PlatformFirstProblemOnboardingComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                FirstProblemOnboardingViewModel::class.java to {
                    FirstProblemOnboardingViewModel(
                        firstProblemOnboardingComponent
                            .firstProblemOnboardingFeature(isNewUserMode)
                            .wrapWithFlowView()
                    )
                }
            )
        )
}