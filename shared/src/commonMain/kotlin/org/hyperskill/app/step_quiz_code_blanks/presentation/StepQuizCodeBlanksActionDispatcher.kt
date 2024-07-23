package org.hyperskill.app.step_quiz_code_blanks.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.Action
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.InternalAction
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepQuizCodeBlanksActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
        }
    }
}