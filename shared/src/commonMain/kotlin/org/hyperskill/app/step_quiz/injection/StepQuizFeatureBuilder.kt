package org.hyperskill.app.step_quiz.injection

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.presentation.StepQuizActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Action
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Message
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.State
import org.hyperskill.app.step_quiz.presentation.StepQuizReducer
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.ReduxFeature

object StepQuizFeatureBuilder {
    fun build(stepQuizInteractor: StepQuizInteractor): Feature<State, Message, Action> {
        val stepQuizReducer = StepQuizReducer()
        val stepQuizActionDispatcher = StepQuizActionDispatcher(ActionDispatcherOptions(), stepQuizInteractor)

        return ReduxFeature(State.Idle, stepQuizReducer)
            .wrapWithActionDispatcher(stepQuizActionDispatcher)
    }
}