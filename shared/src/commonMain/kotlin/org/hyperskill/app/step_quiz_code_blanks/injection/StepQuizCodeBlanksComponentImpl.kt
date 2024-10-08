package org.hyperskill.app.step_quiz_code_blanks.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksActionDispatcher
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksOnboardingReducer
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksReducer

internal class StepQuizCodeBlanksComponentImpl(
    private val appGraph: AppGraph,
    private val stepRoute: StepRoute
) : StepQuizCodeBlanksComponent {
    override val stepQuizCodeBlanksReducer: StepQuizCodeBlanksReducer
        get() = StepQuizCodeBlanksReducer(stepRoute, StepQuizCodeBlanksOnboardingReducer())

    override val stepQuizCodeBlanksActionDispatcher: StepQuizCodeBlanksActionDispatcher
        get() = StepQuizCodeBlanksActionDispatcher(
            config = ActionDispatcherOptions(),
            analyticInteractor = appGraph.analyticComponent.analyticInteractor
        )
}