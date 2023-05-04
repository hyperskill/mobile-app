package org.hyperskill.app.step_quiz.presentation

import kotlinx.datetime.Clock
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer
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
import org.hyperskill.app.step_quiz.domain.analytic.problems_limit_reached_modal.ProblemsLimitReachedModalClickedGoToHomeScreenHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.problems_limit_reached_modal.ProblemsLimitReachedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.problems_limit_reached_modal.ProblemsLimitReachedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.model.permissions.StepQuizUserPermissionRequest
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Action
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Message
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.State
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.StepQuizState
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal typealias StepQuizReducerResult = Pair<State, Set<Action>>

class StepQuizReducer(
    private val stepRoute: StepRoute,
    private val problemsLimitReducer: ProblemsLimitReducer
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): StepQuizReducerResult =
        when (message) {
            is Message.InitWithStep -> initialize(state, message)
            is Message.FetchAttemptSuccess ->
                handleFetchAttemptSuccess(state, message)
            is Message.FetchAttemptError ->
                if (state.stepQuizState is StepQuizState.Loading) {
                    state.copy(stepQuizState = StepQuizState.NetworkError) to emptySet()
                } else {
                    null
                }
            is Message.CreateAttemptClicked ->
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    if (BlockName.codeRelatedBlocksNames.contains(state.stepQuizState.step.block.name)) {
                        state to setOf(
                            Action.ViewAction.RequestUserPermission(StepQuizUserPermissionRequest.RESET_CODE)
                        )
                    } else {
                        state.copy(
                            stepQuizState = StepQuizState.AttemptLoading(oldState = state.stepQuizState)
                        ) to setOf(
                            Action.CreateAttempt(
                                message.step,
                                state.stepQuizState.attempt,
                                state.stepQuizState.submissionState,
                                state.stepQuizState.isProblemsLimitReached,
                                message.shouldResetReply
                            )
                        )
                    }
                } else {
                    null
                }
            is Message.CreateAttemptSuccess ->
                if (state.stepQuizState is StepQuizState.AttemptLoading) {
                    state.copy(
                        stepQuizState = StepQuizState.AttemptLoaded(
                            message.step,
                            message.attempt,
                            message.submissionState,
                            message.isProblemsLimitReached
                        )
                    ) to emptySet()
                } else {
                    null
                }
            is Message.CreateAttemptError ->
                if (state.stepQuizState is StepQuizState.AttemptLoading) {
                    state.copy(
                        stepQuizState = StepQuizState.NetworkError
                    ) to emptySet()
                } else {
                    null
                }
            is Message.CreateSubmissionClicked ->
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    val analyticEvent =
                        if (BlockName.codeRelatedBlocksNames.contains(state.stepQuizState.step.block.name)) {
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
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    when (message.replyValidation) {
                        is ReplyValidationResult.Error -> {
                            state.copy(
                                stepQuizState = state.stepQuizState.copy(
                                    submissionState = StepQuizFeature.SubmissionState.Loaded(
                                        createLocalSubmission(state.stepQuizState, message.reply),
                                        message.replyValidation
                                    )
                                )
                            ) to emptySet()
                        }
                        ReplyValidationResult.Success -> {
                            val submission = createLocalSubmission(state.stepQuizState, message.reply)
                                .copy(status = SubmissionStatus.EVALUATION)

                            state.copy(
                                stepQuizState = state.stepQuizState.copy(
                                    submissionState = StepQuizFeature.SubmissionState.Loaded(
                                        submission,
                                        message.replyValidation
                                    )
                                )
                            ) to setOf(
                                Action.CreateSubmission(
                                    message.step,
                                    stepRoute.stepContext,
                                    state.stepQuizState.attempt.id,
                                    submission
                                )
                            )
                        }
                    }
                } else {
                    null
                }
            is Message.CreateSubmissionSuccess ->
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    state.copy(
                        stepQuizState = state.stepQuizState.copy(
                            attempt = message.newAttempt ?: state.stepQuizState.attempt,
                            submissionState = StepQuizFeature.SubmissionState.Loaded(message.submission)
                        )
                    ) to emptySet()
                } else {
                    null
                }
            is Message.CreateSubmissionNetworkError ->
                if (
                    state.stepQuizState is StepQuizState.AttemptLoaded &&
                    state.stepQuizState.submissionState is StepQuizFeature.SubmissionState.Loaded
                ) {
                    val submission = state.stepQuizState.submissionState.submission
                        .copy(status = SubmissionStatus.LOCAL)
                    state.copy(
                        stepQuizState = state.stepQuizState.copy(
                            submissionState = StepQuizFeature.SubmissionState.Loaded(submission)
                        )
                    ) to setOf(Action.ViewAction.ShowNetworkError)
                } else {
                    null
                }
            is Message.SyncReply ->
                if (state.stepQuizState is StepQuizState.AttemptLoaded && StepQuizResolver.isQuizEnabled(state.stepQuizState)) {
                    val submission = createLocalSubmission(state.stepQuizState, message.reply)
                    state.copy(
                        stepQuizState = state.stepQuizState.copy(
                            submissionState = StepQuizFeature.SubmissionState.Loaded(submission)
                        )
                    ) to emptySet()
                } else {
                    null
                }
            is Message.RequestUserPermission ->
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
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
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    when (message.userPermissionRequest) {
                        StepQuizUserPermissionRequest.RESET_CODE -> if (message.isGranted) {
                            state.copy(
                                stepQuizState = StepQuizState.AttemptLoading(oldState = state.stepQuizState)
                            ) to setOf(
                                Action.CreateAttempt(
                                    state.stepQuizState.step,
                                    state.stepQuizState.attempt,
                                    state.stepQuizState.submissionState,
                                    state.stepQuizState.isProblemsLimitReached,
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
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
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
            is Message.ProblemsLimitReachedModalGoToHomeScreenClicked ->
                state to setOf(
                    Action.ViewAction.NavigateTo.Home,
                    Action.LogAnalyticEvent(
                        ProblemsLimitReachedModalClickedGoToHomeScreenHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    )
                )
            is Message.ClickedCodeDetailsEventMessage ->
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    val event = StepQuizClickedCodeDetailsHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    state to setOf(Action.LogAnalyticEvent(event))
                } else {
                    null
                }
            is Message.ClickedRetryEventMessage ->
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    val event = StepQuizClickedRetryHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    state to setOf(Action.LogAnalyticEvent(event))
                } else {
                    null
                }
            is Message.DailyStepCompletedModalShownEventMessage ->
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    val event = StepQuizDailyStepCompletedModalShownHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    state to setOf(Action.LogAnalyticEvent(event))
                } else {
                    null
                }
            is Message.DailyStepCompletedModalHiddenEventMessage ->
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    val event = StepQuizDailyStepCompletedModalHiddenHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    state to setOf(Action.LogAnalyticEvent(event))
                } else {
                    null
                }
            is Message.ProblemsLimitReachedModalShownEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProblemsLimitReachedModalShownHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    )
                )
            is Message.ProblemsLimitReachedModalHiddenEventMessage ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProblemsLimitReachedModalHiddenHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    )
                )
            // Wrapper Messages
            is Message.ProblemsLimitMessage -> {
                val (problemsLimitState, problemsLimitActions) =
                    reduceProblemsLimitMessage(state.problemsLimitState, message.message)
                state.copy(problemsLimitState = problemsLimitState) to problemsLimitActions
            }
        } ?: (state to emptySet())

    private fun handleFetchAttemptSuccess(state: State, message: Message.FetchAttemptSuccess): StepQuizReducerResult =
        if (state.stepQuizState is StepQuizState.Loading) {
            if (StepQuizResolver.isIdeRequired(message.step, message.submissionState)) {
                state.copy(stepQuizState = StepQuizState.Unsupported) to emptySet()
            } else {
                val isProblemsLimitReached = when (stepRoute) {
                    is StepRoute.Repeat,
                    is StepRoute.LearnDaily -> false
                    else -> message.isProblemsLimitReached
                }

                val actions = if (isProblemsLimitReached) {
                    setOf(Action.ViewAction.ShowProblemsLimitReachedModal)
                } else {
                    emptySet()
                }

                state.copy(
                    stepQuizState = StepQuizState.AttemptLoaded(
                        message.step,
                        message.attempt,
                        message.submissionState,
                        isProblemsLimitReached
                    )
                ) to actions
            }
        } else {
            state to emptySet()
        }

    private fun reduceProblemsLimitMessage(
        state: ProblemsLimitFeature.State,
        message: ProblemsLimitFeature.Message
    ): Pair<ProblemsLimitFeature.State, Set<Action>> {
        val (problemsLimitState, problemsLimitActions) =
            problemsLimitReducer.reduce(state, message)

        val actions = problemsLimitActions
            .map {
                if (it is ProblemsLimitFeature.Action.ViewAction) {
                    Action.ViewAction.ProblemsLimitViewAction(it)
                } else {
                    Action.ProblemsLimitAction(it)
                }
            }
            .toSet()

        return problemsLimitState to actions
    }

    private fun initialize(state: State, message: Message.InitWithStep): Pair<State, Set<Action>> {
        val needReloadStepQuiz =
            state.stepQuizState is StepQuizState.Idle ||
                (message.forceUpdate && state.stepQuizState is StepQuizState.NetworkError)
        val (stepQuizState, stepQuizActions) =
            if (needReloadStepQuiz) {
                StepQuizState.Loading to setOf(Action.FetchAttempt(message.step))
            } else {
                state.stepQuizState to emptySet()
            }

        val (problemsLimitState, problemsLimitActions) =
            if (stepRoute is StepRoute.Learn) {
                reduceProblemsLimitMessage(
                    state.problemsLimitState,
                    ProblemsLimitFeature.Message.Initialize(message.forceUpdate)
                )
            } else {
                state.problemsLimitState to emptySet()
            }

        return state.copy(
            stepQuizState = stepQuizState,
            problemsLimitState = problemsLimitState
        ) to stepQuizActions + problemsLimitActions
    }

    private fun createLocalSubmission(oldState: StepQuizState.AttemptLoaded, reply: Reply): Submission {
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