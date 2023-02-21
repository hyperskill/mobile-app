package org.hyperskill.app.step_quiz.presentation

import kotlinx.datetime.Clock
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedCodeDetailsHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedRetryHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedRunHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedSendHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizHiddenDailyNotificationsNoticeHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizShownDailyNotificationsNoticeHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.daily_step_completed_modal.StepQuizDailyStepCompletedModalClickedGoBackHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.daily_step_completed_modal.StepQuizDailyStepCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.daily_step_completed_modal.StepQuizDailyStepCompletedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.model.permissions.StepQuizUserPermissionRequest
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Action
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Message
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class StepQuizReducer(private val stepRoute: StepRoute) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.InitWithStep ->
                if (state is State.Idle ||
                    state is State.NetworkError && message.forceUpdate
                ) {
                    State.Loading to setOf(Action.FetchAttempt(message.step))
                } else {
                    null
                }
            is Message.FetchAttemptSuccess ->
                if (state is State.Loading) {
                    State.AttemptLoaded(
                        message.step,
                        message.attempt,
                        message.submissionState,
                        message.currentProfile
                    ) to emptySet()
                } else {
                    null
                }
            is Message.FetchAttemptError ->
                if (state is State.Loading) {
                    State.NetworkError to emptySet()
                } else {
                    null
                }
            is Message.CreateAttemptClicked ->
                if (state is State.AttemptLoaded) {
                    if (state.step.block.name == BlockName.CODE || state.step.block.name == BlockName.SQL) {
                        state to setOf(
                            Action.ViewAction.RequestUserPermission(StepQuizUserPermissionRequest.RESET_CODE)
                        )
                    } else {
                        State.AttemptLoading(oldState = state) to setOf(
                            Action.CreateAttempt(
                                message.step,
                                state.attempt,
                                state.submissionState,
                                message.shouldResetReply
                            )
                        )
                    }
                } else {
                    null
                }
            is Message.CreateAttemptSuccess ->
                if (state is State.AttemptLoading) {
                    State.AttemptLoaded(
                        message.step,
                        message.attempt,
                        message.submissionState,
                        message.currentProfile
                    ) to emptySet()
                } else {
                    null
                }
            is Message.CreateAttemptError ->
                if (state is State.AttemptLoading) {
                    State.NetworkError to emptySet()
                } else {
                    null
                }
            is Message.CreateSubmissionClicked ->
                if (state is State.AttemptLoaded) {
                    val analyticEvent =
                        if (message.step.block.name == BlockName.CODE || message.step.block.name == BlockName.SQL) {
                            StepQuizClickedRunHyperskillAnalyticEvent(stepRoute.analyticRoute)
                        } else {
                            StepQuizClickedSendHyperskillAnalyticEvent(stepRoute.analyticRoute)
                        }
                    state to setOf(
                        Action.CreateSubmissionValidateReply(message.step, message.reply),
                        Action.LogAnalyticEvent(analyticEvent)
                    )
                } else {
                    null
                }
            is Message.CreateSubmissionReplyValidationResult ->
                if (state is State.AttemptLoaded) {
                    when (message.replyValidation) {
                        is ReplyValidationResult.Error -> {
                            state.copy(
                                submissionState = StepQuizFeature.SubmissionState.Loaded(
                                    createLocalSubmission(state, message.reply),
                                    message.replyValidation
                                )
                            ) to emptySet()
                        }
                        ReplyValidationResult.Success -> {
                            val submission = createLocalSubmission(state, message.reply)
                                .copy(status = SubmissionStatus.EVALUATION)

                            state.copy(
                                submissionState = StepQuizFeature.SubmissionState.Loaded(
                                    submission,
                                    message.replyValidation
                                )
                            ) to setOf(
                                Action.CreateSubmission(message.step, stepRoute.stepContext, state.attempt.id, submission)
                            )
                        }
                    }
                } else {
                    null
                }
            is Message.CreateSubmissionSuccess ->
                if (state is State.AttemptLoaded) {
                    state.copy(
                        attempt = message.newAttempt ?: state.attempt,
                        submissionState = StepQuizFeature.SubmissionState.Loaded(message.submission)
                    ) to emptySet()
                } else {
                    null
                }
            is Message.CreateSubmissionNetworkError ->
                if (state is State.AttemptLoaded && state.submissionState is StepQuizFeature.SubmissionState.Loaded) {
                    val submission = state.submissionState.submission.copy(status = SubmissionStatus.LOCAL)
                    state.copy(
                        submissionState = StepQuizFeature.SubmissionState.Loaded(submission)
                    ) to setOf(Action.ViewAction.ShowNetworkError)
                } else {
                    null
                }
            is Message.SyncReply ->
                if (state is State.AttemptLoaded && StepQuizResolver.isQuizEnabled(state)) {
                    val submission = createLocalSubmission(state, message.reply)
                    state.copy(submissionState = StepQuizFeature.SubmissionState.Loaded(submission)) to emptySet()
                } else {
                    null
                }
            is Message.RequestUserPermission ->
                if (state is State.AttemptLoaded) {
                    val logAnalyticEventAction = when (message.userPermissionRequest) {
                        StepQuizUserPermissionRequest.SEND_DAILY_STUDY_REMINDERS ->
                            Action.LogAnalyticEvent(
                                StepQuizShownDailyNotificationsNoticeHyperskillAnalyticEvent(stepRoute.analyticRoute)
                            )
                        else -> null
                    }
                    state to setOfNotNull(
                        Action.ViewAction.RequestUserPermission(message.userPermissionRequest),
                        logAnalyticEventAction
                    )
                } else {
                    null
                }
            is Message.RequestUserPermissionResult ->
                if (state is State.AttemptLoaded) {
                    when (message.userPermissionRequest) {
                        StepQuizUserPermissionRequest.RESET_CODE -> if (message.isGranted) {
                            State.AttemptLoading(oldState = state) to setOf(
                                Action.CreateAttempt(
                                    state.step,
                                    state.attempt,
                                    state.submissionState,
                                    shouldResetReply = true
                                )
                            )
                        } else {
                            null
                        }
                        StepQuizUserPermissionRequest.SEND_DAILY_STUDY_REMINDERS -> {
                            val analyticEvent = StepQuizHiddenDailyNotificationsNoticeHyperskillAnalyticEvent(
                                route = stepRoute.analyticRoute,
                                isAgreed = message.isGranted
                            )
                            state to setOf(
                                Action.RequestUserPermissionResult(message.userPermissionRequest, message.isGranted),
                                Action.LogAnalyticEvent(analyticEvent)
                            )
                        }
                    }
                } else {
                    null
                }
            is Message.ShowProblemOfDaySolvedModal ->
                state to setOf(Action.ViewAction.ShowProblemOfDaySolvedModal(message.earnedGemsText))
            is Message.ProblemOfDaySolvedModalGoBackClicked ->
                if (state is State.AttemptLoaded) {
                    val event = StepQuizDailyStepCompletedModalClickedGoBackHyperskillAnalyticEvent(
                        stepRoute.analyticRoute
                    )
                    state to setOf(
                        Action.LogAnalyticEvent(event),
                        Action.ViewAction.NavigateTo.Back
                    )
                } else {
                    null
                }
            is Message.ClickedCodeDetailsEventMessage ->
                if (state is State.AttemptLoaded) {
                    val event = StepQuizClickedCodeDetailsHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    state to setOf(Action.LogAnalyticEvent(event))
                } else {
                    null
                }
            is Message.ClickedRetryEventMessage ->
                if (state is State.AttemptLoaded) {
                    val event = StepQuizClickedRetryHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    state to setOf(Action.LogAnalyticEvent(event))
                } else {
                    null
                }
            is Message.DailyStepCompletedModalShownEventMessage ->
                if (state is State.AttemptLoaded) {
                    val event = StepQuizDailyStepCompletedModalShownHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    state to setOf(Action.LogAnalyticEvent(event))
                } else {
                    null
                }
            is Message.DailyStepCompletedModalHiddenEventMessage ->
                if (state is State.AttemptLoaded) {
                    val event = StepQuizDailyStepCompletedModalHiddenHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    state to setOf(Action.LogAnalyticEvent(event))
                } else {
                    null
                }
        } ?: (state to emptySet())

    private fun createLocalSubmission(oldState: State.AttemptLoaded, reply: Reply): Submission {
        val submission = (oldState.submissionState as? StepQuizFeature.SubmissionState.Loaded)?.submission
        return Submission(
            id = submission?.id ?: 0,
            attempt = oldState.attempt.id,
            reply = reply,
            status = SubmissionStatus.LOCAL,
            originalStatus = submission?.originalStatus ?: submission?.status,
            time = Clock.System.now().toString()
        )
    }
}