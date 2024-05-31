package org.hyperskill.app.step_feedback.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.step_feedback.domain.analytic.StepFeedbackModalSendButtonClickedHyperskillAnalyticEvent
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.Action
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.InternalAction
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.InternalMessage
import org.hyperskill.app.step_feedback.presentation.StepFeedbackFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class StepFeedbackActionDispatcher(
    config: ActionDispatcherOptions,
    private val analyticInteractor: AnalyticInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.event)
            is InternalAction.SendFeedback -> {
                analyticInteractor.logEvent(
                    StepFeedbackModalSendButtonClickedHyperskillAnalyticEvent(
                        route = action.route,
                        stepId = action.stepId,
                        feedback = action.feedback
                    )
                )
                onNewMessage(InternalMessage.FeedbackSent)
            }
            else -> {
                // no op
            }
        }
    }
}