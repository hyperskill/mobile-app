package org.hyperskill.app.step_quiz.presentation

import kotlinx.datetime.Clock
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedCodeDetailsHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedContinueHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedRetryHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedRunHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedSendHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizHiddenDailyNotificationsNoticeHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizShownDailyNotificationsNoticeHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.model.permissions.StepQuizUserPermissionRequest
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Action
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Message
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

class StepQuizReducer : StateReducer<State, Message, Action> {
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
                        state to setOf(Action.ViewAction.RequestUserPermission(StepQuizUserPermissionRequest.RESET_CODE))
                    } else {
                        State.AttemptLoading to setOf(
                            Action.CreateAttempt(
                                message.step,
                                state.attempt,
                                state.submissionState
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
                    val analyticRoute = resolveAnalyticRoute(state)
                    val analyticEvent = if (message.step.block.name == BlockName.CODE)
                        StepQuizClickedRunHyperskillAnalyticEvent(analyticRoute)
                    else StepQuizClickedSendHyperskillAnalyticEvent(analyticRoute)

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
                            val submission = Submission(
                                attempt = state.attempt.id,
                                reply = message.reply,
                                status = SubmissionStatus.LOCAL
                            )

                            state.copy(
                                submissionState = StepQuizFeature.SubmissionState.Loaded(
                                    submission,
                                    message.replyValidation
                                )
                            ) to emptySet()
                        }
                        ReplyValidationResult.Success -> {
                            val submission = Submission(
                                attempt = state.attempt.id,
                                reply = message.reply,
                                status = SubmissionStatus.EVALUATION
                            )

                            state.copy(
                                submissionState = StepQuizFeature.SubmissionState.Loaded(
                                    submission,
                                    message.replyValidation
                                )
                            ) to setOf(Action.CreateSubmission(message.step, state.attempt.id, message.reply))
                        }
                    }
                } else {
                    null
                }
            is Message.CreateSubmissionSuccess ->
                if (state is State.AttemptLoaded) {
                    state.copy(
                        submissionState = StepQuizFeature.SubmissionState.Loaded(message.submission)
                    ) to emptySet()
                } else {
                    null
                }
            is Message.CreateSubmissionNetworkError ->
                if (state is State.AttemptLoaded && state.submissionState is StepQuizFeature.SubmissionState.Loaded) {
                    val submission = state.submissionState.submission.copy(status = SubmissionStatus.LOCAL)

                    state.copy(submissionState = StepQuizFeature.SubmissionState.Loaded(submission)) to setOf(Action.ViewAction.ShowNetworkError)
                } else {
                    null
                }
            is Message.ContinueClicked ->
                if (state is State.AttemptLoaded) {
                    val analyticEvent = StepQuizClickedContinueHyperskillAnalyticEvent(resolveAnalyticRoute(state))
                    state to setOf(Action.LogAnalyticEvent(analyticEvent), Action.ViewAction.NavigateTo.HomeScreen)
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
            is Message.NeedToAskUserToEnableDailyReminders ->
                if (state is State.AttemptLoaded) {
                    val analyticEvent =
                        StepQuizShownDailyNotificationsNoticeHyperskillAnalyticEvent(route = resolveAnalyticRoute(state))
                    state to setOf(
                        Action.ViewAction.AskUserToEnableDailyReminders,
                        Action.LogAnalyticEvent(analyticEvent)
                    )
                } else {
                    null
                }
            is Message.UserAgreedToEnableDailyReminders ->
                if (state is State.AttemptLoaded) {
                    val analyticEvent = StepQuizHiddenDailyNotificationsNoticeHyperskillAnalyticEvent(
                        route = resolveAnalyticRoute(state),
                        isAgreed = true
                    )
                    state to setOf(
                        Action.NotifyUserAgreedToEnableDailyReminders,
                        Action.LogAnalyticEvent(analyticEvent)
                    )
                } else {
                    null
                }
            is Message.UserDeclinedToEnableDailyReminders ->
                if (state is State.AttemptLoaded) {
                    val analyticEvent = StepQuizHiddenDailyNotificationsNoticeHyperskillAnalyticEvent(
                        route = resolveAnalyticRoute(state),
                        isAgreed = false
                    )
                    state to setOf(
                        Action.NotifyUserDeclinedToEnableDailyReminders,
                        Action.LogAnalyticEvent(analyticEvent)
                    )
                } else {
                    null
                }
            is Message.RequestUserPermissionResult ->
                if (state is State.AttemptLoaded) {
                    when (message.userPermissionRequest) {
                        StepQuizUserPermissionRequest.RESET_CODE -> if (message.isGranted) {
                            State.AttemptLoading to setOf(
                                Action.CreateAttempt(
                                    state.step,
                                    state.attempt,
                                    state.submissionState
                                )
                            )
                        } else {
                            null
                        }
                    }
                } else {
                    null
                }
            is Message.ViewedEventMessage ->
                state to setOf(Action.LogViewedEvent(message.stepId))
            is Message.ClickedCodeDetailsEventMessage ->
                if (state is State.AttemptLoaded) {
                    val event = StepQuizClickedCodeDetailsHyperskillAnalyticEvent(route = resolveAnalyticRoute(state))
                    state to setOf(Action.LogAnalyticEvent(event))
                } else {
                    null
                }
            is Message.ClickedRetryEventMessage ->
                if (state is State.AttemptLoaded) {
                    val event = StepQuizClickedRetryHyperskillAnalyticEvent(route = resolveAnalyticRoute(state))
                    state to setOf(Action.LogAnalyticEvent(event))
                } else {
                    null
                }
        } ?: (state to emptySet())

    private fun createLocalSubmission(oldState: State.AttemptLoaded, reply: Reply): Submission {
        val submissionId = (oldState.submissionState as? StepQuizFeature.SubmissionState.Loaded)
            ?.submission
            ?.id
            ?: 0

        return Submission(
            id = submissionId,
            attempt = oldState.attempt.id,
            reply = reply,
            status = SubmissionStatus.LOCAL,
            time = Clock.System.now().toString()
        )
    }

    private fun resolveAnalyticRoute(state: State.AttemptLoaded): HyperskillAnalyticRoute =
        if (state.attempt.step == state.currentProfile.dailyStep)
            HyperskillAnalyticRoute.Learn.Daily(state.attempt.step)
        else HyperskillAnalyticRoute.Learn.Step(state.attempt.step)
}