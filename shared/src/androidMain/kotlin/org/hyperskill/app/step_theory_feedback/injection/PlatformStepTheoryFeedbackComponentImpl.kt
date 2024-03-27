package org.hyperskill.app.step_theory_feedback.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.step_theory_feedback.presentation.StepTheoryFeedbackViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformStepTheoryFeedbackComponentImpl(
    private val stepTheoryFeedbackComponent: StepTheoryFeedbackComponent
) : PlatformStepTheoryFeedbackComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory =
        ReduxViewModelFactory(
            mapOf(
                StepTheoryFeedbackViewModel::class.java to {
                    StepTheoryFeedbackViewModel(
                        stepTheoryFeedbackComponent.stepTheoryFeedbackFeature.wrapWithViewContainer()
                    )
                }
            )
        )
}