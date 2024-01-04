package org.hyperskill.app.interview_preparation_onboarding.injection

import org.hyperskill.app.core.flowredux.presentation.wrapWithFlowView
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingViewModel
import org.hyperskill.app.step.domain.model.StepRoute

class PlatformInterviewPreparationOnboardingComponentImpl(
    private val interviewPreparationOnboardingComponent: InterviewPreparationOnboardingComponent,
    private val stepRoute: StepRoute
) : PlatformInterviewPreparationOnboardingComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            mapOf(
                InterviewPreparationOnboardingViewModel::class.java to {
                    InterviewPreparationOnboardingViewModel(
                        interviewPreparationOnboardingComponent
                            .interviewPreparationOnboardingFeature(stepRoute)
                            .wrapWithFlowView()
                    )
                }
            )
        )
}