package org.hyperskill.app.step_quiz.presentation

import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.step_quiz.domain.interactor.StepQuizInteractor
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Action
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Message
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StepQuizActionDispatcher(
    config: ActionDispatcherOptions,
    private val stepQuizInteractor: StepQuizInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchAttempt -> {
                // TODO ALTAPPS-102 Will get user id inside interactor
                val message = stepQuizInteractor
                    .getAttempt(action.step.id, userId = 234547455)
                    .fold(
                        onSuccess = { attempt ->
                            val message = getSubmissionState(attempt.id, action.step.id, userId = 234547455).fold(
                                onSuccess = { Message.FetchAttemptSuccess(attempt, it) },
                                onFailure = {
                                    println("ALTAPPS – Error – $it")
                                    Message.FetchAttemptError
                                }
                            )
                            message
                        },
                        onFailure = { Message.FetchAttemptError }
                    )
                onNewMessage(message)
            }
            is Action.CreateAttempt -> {
                val reply = (action.submissionState as? StepQuizFeature.SubmissionState.Loaded)
                    ?.submission
                    ?.reply

                val message = stepQuizInteractor
                    .createAttempt(action.step.id)
                    .fold(
                        onSuccess = {
                            Message.CreateAttemptSuccess(it, StepQuizFeature.SubmissionState.Empty(reply = reply))
                        },
                        onFailure = {
                            Message.CreateAttemptError
                        }
                    )
                onNewMessage(message)
            }
            is Action.CreateSubmission -> {
                val message = stepQuizInteractor
                    .createSubmission(action.attemptId, action.reply)
                    .fold(
                        onSuccess = { newSubmission ->
                            Message.CreateSubmissionSuccess(newSubmission)
                        },
                        onFailure = {
                            Message.CreateSubmissionError
                        }
                    )
                onNewMessage(message)
            }
        }
    }

    private suspend fun getSubmissionState(attemptId: Long, stepId: Long, userId: Long): Result<StepQuizFeature.SubmissionState> =
        stepQuizInteractor
            .getSubmission(attemptId, stepId, userId)
            .map { submission ->
                println("ALTAPPS – Here we go")
                if (submission == null) {
                    StepQuizFeature.SubmissionState.Empty()
                } else {
                    StepQuizFeature.SubmissionState.Loaded(submission)
                }
            }
}