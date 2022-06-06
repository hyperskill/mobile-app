package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.core.injection.ReduxViewModelFactory
import org.hyperskill.app.step_quiz.presentation.StepQuizViewModel
import ru.nobird.app.presentation.redux.container.wrapWithViewContainer

class PlatformStepQuizComponentImpl(private val stepQuizComponent: StepQuizComponent) : PlatformStepQuizComponent {
    override val reduxViewModelFactory: ReduxViewModelFactory
        get() = ReduxViewModelFactory(mapOf(StepQuizViewModel::class.java to { StepQuizViewModel(stepQuizComponent.stepQuizFeature.wrapWithViewContainer()) }))
}