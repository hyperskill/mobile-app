package org.hyperskill.app.theory_feedback.presentation

import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.Action
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.InternalAction
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.Message
import org.hyperskill.app.theory_feedback.presentation.TheoryFeedbackFeature.State
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class TheoryFeedbackActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.event)
            else -> {
                // no op
            }
        }
    }
}