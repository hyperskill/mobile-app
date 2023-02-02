package org.hyperskill.app.step_quiz_hints.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformStepQuizHintsComponentImpl(
    appGraph: AppGraph,
    private val step: Step
) : PlatformStepQuizHintsComponent {

    private val stepQuizHintComponent = StepQuizHintsComponentImpl(appGraph)

    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(
            viewModelMap = mapOf(
                StepQuizHintsViewModel::class.java to
                    {
                        StepQuizHintsViewModel(
                            reduxViewContainer = stepQuizHintComponent.stepQuizHintsFeature.wrapWithViewContainer(),
                            step = step
                        )
                    }
            )
        )
}