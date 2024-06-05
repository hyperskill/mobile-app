package org.hyperskill.app.step_feedback.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.step_feedback.presentation.StepFeedbackViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformStepFeedbackComponentImpl(
    private val stepFeedbackComponent: StepFeedbackComponent
) : PlatformStepFeedbackComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory =
        ReduxViewModelFactory(
            mapOf(
                StepFeedbackViewModel::class.java to {
                    StepFeedbackViewModel(
                        stepFeedbackComponent.stepFeedbackFeature.wrapWithViewContainer()
                    )
                }
            )
        )
}