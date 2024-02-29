package org.hyperskill.app.step_quiz.presentation

import kotlinx.datetime.Clock
import org.hyperskill.app.freemium.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.app.onboarding.domain.model.ProblemsOnboardingFlags
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.analytic.ProblemOnboardingModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.ProblemOnboardingModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.ProblemsLimitReachedModalClickedGoToHomeScreenHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.ProblemsLimitReachedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.ProblemsLimitReachedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedCodeDetailsHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedOpenFullScreenCodeEditorHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedRetryHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedRunHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedSendHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedStepTextDetailsHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedTheoryToolbarItemHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizCodeEditorClickedInputAccessoryButtonHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizFullScreenCodeEditorClickedCodeDetailsHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizFullScreenCodeEditorClickedStepTextDetailsHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizUnsupportedClickedGoToStudyPlanHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizUnsupportedClickedSolveOnTheWebHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.model.submissions.Reply
import org.hyperskill.app.step_quiz.domain.model.submissions.Submission
import org.hyperskill.app.step_quiz.domain.model.submissions.SubmissionStatus
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Action
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.InternalAction
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.InternalMessage
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Message
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.State
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.StepQuizState
import org.hyperskill.app.step_quiz_fill_blanks.model.FillBlanksMode
import org.hyperskill.app.step_quiz_fill_blanks.presentation.FillBlanksResolver
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsReducer
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal typealias StepQuizReducerResult = Pair<State, Set<Action>>

internal class StepQuizReducer(
    private val stepRoute: StepRoute,
    private val stepQuizHintsReducer: StepQuizHintsReducer
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): StepQuizReducerResult =
        when (message) {
            is Message.InitWithStep ->
                initialize(state, message)
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
                            Action.ViewAction.RequestResetCode
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
                            step = message.step,
                            attempt = message.attempt,
                            submissionState = message.submissionState,
                            isProblemsLimitReached = message.isProblemsLimitReached,
                            isTheoryAvailable = StepQuizResolver.isTheoryAvailable(stepRoute, message.step)
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
                    ) to buildSet {
                        if (message.submission.status == SubmissionStatus.WRONG &&
                            StepQuizResolver.isStepHasLimitedAttempts(stepRoute)
                        ) {
                            add(InternalAction.UpdateProblemsLimit(FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION))
                        }
                    }
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
                if (state.stepQuizState is StepQuizState.AttemptLoaded &&
                    StepQuizResolver.isQuizEnabled(state.stepQuizState)
                ) {
                    val submission = createLocalSubmission(state.stepQuizState, message.reply)
                    state.copy(
                        stepQuizState = state.stepQuizState.copy(
                            submissionState = StepQuizFeature.SubmissionState.Loaded(submission)
                        )
                    ) to emptySet()
                } else {
                    null
                }
            is Message.RequestResetCodeResult -> {
                if (state.stepQuizState is StepQuizState.AttemptLoaded && message.isGranted) {
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
            }
            is InternalMessage.UpdateProblemsLimitResult ->
                handleUpdateProblemsLimitResult(state, message)
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
            is Message.FullScreenCodeEditorClickedCodeDetailsEventMessage -> {
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    val event = StepQuizFullScreenCodeEditorClickedCodeDetailsHyperskillAnalyticEvent(
                        stepRoute.analyticRoute
                    )
                    state to setOf(Action.LogAnalyticEvent(event))
                } else {
                    null
                }
            }
            is Message.ClickedStepTextDetailsEventMessage -> {
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    val event = StepQuizClickedStepTextDetailsHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    state to setOf(Action.LogAnalyticEvent(event))
                } else {
                    null
                }
            }
            is Message.FullScreenCodeEditorClickedStepTextDetailsEventMessage -> {
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    val event = StepQuizFullScreenCodeEditorClickedStepTextDetailsHyperskillAnalyticEvent(
                        stepRoute.analyticRoute
                    )
                    state to setOf(Action.LogAnalyticEvent(event))
                } else {
                    null
                }
            }
            is Message.ClickedOpenFullScreenCodeEditorEventMessage -> {
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    val event = StepQuizClickedOpenFullScreenCodeEditorHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    state to setOf(Action.LogAnalyticEvent(event))
                } else {
                    null
                }
            }
            is Message.CodeEditorClickedInputAccessoryButtonEventMessage -> {
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    val event = StepQuizCodeEditorClickedInputAccessoryButtonHyperskillAnalyticEvent(
                        route = stepRoute.analyticRoute,
                        symbol = message.symbol
                    )
                    state to setOf(Action.LogAnalyticEvent(event))
                } else {
                    null
                }
            }
            is Message.TheoryToolbarItemClicked ->
                handleTheoryToolbarItemClicked(state)
            Message.UnsupportedQuizGoToStudyPlanClicked ->
                state to setOf(
                    Action.LogAnalyticEvent(
                        StepQuizUnsupportedClickedGoToStudyPlanHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    ),
                    Action.ViewAction.NavigateTo.StudyPlan
                )
            Message.UnsupportedQuizSolveOnTheWebClicked ->
                state to setOf(
                    Action.ViewAction.CreateMagicLinkState.Loading,
                    InternalAction.CreateMagicLinkForUnsupportedQuiz(stepRoute),
                    Action.LogAnalyticEvent(
                        StepQuizUnsupportedClickedSolveOnTheWebHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    )
                )
            InternalMessage.CreateMagicLinkForUnsupportedQuizError ->
                state to setOf(Action.ViewAction.CreateMagicLinkState.Error)
            is InternalMessage.CreateMagicLinkForUnsupportedQuizSuccess ->
                state to setOf(
                    Action.ViewAction.CreateMagicLinkState.Success,
                    Action.ViewAction.OpenUrl(message.url)
                )
            is Message.ClickedRetryEventMessage ->
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    val event = StepQuizClickedRetryHyperskillAnalyticEvent(stepRoute.analyticRoute)
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
            is Message.ProblemOnboardingModalShownMessage -> {
                state to setOf(
                    Action.SaveProblemOnboardingModalShownCacheFlag(modalType = message.modalType),
                    Action.LogAnalyticEvent(
                        ProblemOnboardingModalShownHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute,
                            modalType = message.modalType
                        )
                    )
                )
            }
            is Message.ProblemOnboardingModalHiddenMessage -> {
                state to setOf(
                    Action.LogAnalyticEvent(
                        ProblemOnboardingModalHiddenHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute,
                            modalType = message.modalType
                        )
                    )
                )
            }
            // Wrapper Messages
            is Message.StepQuizHintsMessage -> {
                val (stepQuizHintsState, stepQuizHintsActions) =
                    reduceStepQuizHintsMessage(state.stepQuizHintsState, message.message)
                state.copy(stepQuizHintsState = stepQuizHintsState) to stepQuizHintsActions
            }
        } ?: (state to emptySet())

    private fun handleFetchAttemptSuccess(state: State, message: Message.FetchAttemptSuccess): StepQuizReducerResult =
        if (state.stepQuizState is StepQuizState.Loading) {
            if (StepQuizResolver.isIdeRequired(message.step, message.submissionState)) {
                state.copy(stepQuizState = StepQuizState.Unsupported) to emptySet()
            } else {
                val isProblemsLimitReached = 
                    StepQuizResolver.isStepHasLimitedAttempts(stepRoute) && message.isProblemsLimitReached

                val actions = if (isProblemsLimitReached && message.problemsLimitReachedModalData != null) {
                    setOf(
                        Action.ViewAction.ShowProblemsLimitReachedModal(message.problemsLimitReachedModalData)
                    )
                } else {
                    getProblemOnboardingModalActions(
                        step = message.step,
                        attempt = message.attempt,
                        problemsOnboardingFlags = message.problemsOnboardingFlags
                    )
                }

                state.copy(
                    stepQuizState = StepQuizState.AttemptLoaded(
                        step = message.step,
                        attempt = message.attempt,
                        submissionState = message.submissionState,
                        isProblemsLimitReached = isProblemsLimitReached,
                        isTheoryAvailable = StepQuizResolver.isTheoryAvailable(stepRoute, message.step)
                    )
                ) to actions
            }
        } else {
            state to emptySet()
        }

    private fun initialize(state: State, message: Message.InitWithStep): StepQuizReducerResult {
        val needReloadStepQuiz =
            state.stepQuizState is StepQuizState.Idle ||
                (message.forceUpdate && state.stepQuizState is StepQuizState.NetworkError)
        val (stepQuizState, stepQuizActions) =
            if (needReloadStepQuiz) {
                StepQuizState.Loading to setOf(Action.FetchAttempt(message.step))
            } else {
                state.stepQuizState to emptySet()
            }

        val (stepQuizHintsState, stepQuizHintsActions) =
            if (StepQuizHintsFeature.isHintsFeatureAvailable(step = message.step)) {
                reduceStepQuizHintsMessage(
                    state.stepQuizHintsState,
                    StepQuizHintsFeature.Message.InitWithStepId(message.step.id)
                )
            } else {
                StepQuizHintsFeature.State.Idle to emptySet()
            }

        return state.copy(
            stepQuizState = stepQuizState,
            stepQuizHintsState = stepQuizHintsState
        ) to stepQuizActions + stepQuizHintsActions
    }

    private fun handleUpdateProblemsLimitResult(
        state: State,
        message: InternalMessage.UpdateProblemsLimitResult
    ): StepQuizReducerResult? =
                val isProblemsLimitReached = 
                    StepQuizResolver.isStepHasLimitedAttempts(stepRoute) && message.isProblemsLimitReached

            state.copy(
                stepQuizState = state.stepQuizState.copy(
                    isProblemsLimitReached = isProblemsLimitReached
                )
            ) to buildSet {
                if (isProblemsLimitReached && message.problemsLimitReachedModalData != null) {
                    add(Action.ViewAction.ShowProblemsLimitReachedModal(message.problemsLimitReachedModalData))
                }
            }
        } else {
            null
        }

    private fun handleTheoryToolbarItemClicked(state: State): StepQuizReducerResult =
        if (state.stepQuizState is StepQuizState.AttemptLoaded &&
            state.stepQuizState.isTheoryAvailable
        ) {
            state to buildSet {
                val topicTheoryId = state.stepQuizState.step.topicTheory

                add(
                    Action.LogAnalyticEvent(
                        StepQuizClickedTheoryToolbarItemHyperskillAnalyticEvent(
                            stepRoute.analyticRoute,
                            topicTheoryId
                        )
                    )
                )

                if (topicTheoryId != null) {
                    when (stepRoute) {
                        is StepRoute.Repeat.Practice ->
                            StepRoute.Repeat.Theory(stepId = topicTheoryId)
                        is StepRoute.Learn.Step ->
                            StepRoute.Learn.TheoryOpenedFromPractice(stepId = topicTheoryId)
                        is StepRoute.Learn.TheoryOpenedFromPractice,
                        is StepRoute.Learn.TheoryOpenedFromSearch,
                        is StepRoute.LearnDaily,
                        is StepRoute.Repeat.Theory,
                        is StepRoute.StageImplement,
                        is StepRoute.InterviewPreparation ->
                            null
                    }?.let { targetStepRoute ->
                        add(Action.ViewAction.NavigateTo.StepScreen(targetStepRoute))
                    } ?: add(Action.ViewAction.ShowNetworkError)
                }
            }
        } else {
            state to emptySet()
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

    private fun getProblemOnboardingModalActions(
        step: Step,
        attempt: Attempt,
        problemsOnboardingFlags: ProblemsOnboardingFlags
    ): Set<Action> =
        when (step.block.name) {
            BlockName.PARSONS -> {
                if (!problemsOnboardingFlags.isParsonsOnboardingShown) {
                    setOf(
                        Action.ViewAction.ShowProblemOnboardingModal(
                            modalType = StepQuizFeature.ProblemOnboardingModal.Parsons
                        )
                    )
                } else {
                    emptySet()
                }
            }
            BlockName.FILL_BLANKS -> {
                val fillBlanksMode = kotlin.runCatching {
                    val dataset = attempt.dataset ?: return@runCatching null
                    FillBlanksResolver.resolve(dataset)
                }.getOrNull()?.takeIf {
                    when (it) {
                        FillBlanksMode.INPUT -> !problemsOnboardingFlags.isFillBlanksInputModeOnboardingShown
                        FillBlanksMode.SELECT -> !problemsOnboardingFlags.isFillBlanksSelectModeOnboardingShown
                    }
                }

                if (fillBlanksMode != null) {
                    setOf(
                        Action.ViewAction.ShowProblemOnboardingModal(
                            modalType = StepQuizFeature.ProblemOnboardingModal.FillBlanks(fillBlanksMode)
                        )
                    )
                } else {
                    emptySet()
                }
            }
            else -> emptySet()
        }

    private fun reduceStepQuizHintsMessage(
        state: StepQuizHintsFeature.State,
        message: StepQuizHintsFeature.Message
    ): Pair<StepQuizHintsFeature.State, Set<Action>> {
        val (stepQuizHintsState, stepQuizHintsActions) = stepQuizHintsReducer.reduce(state, message)

        val actions = stepQuizHintsActions
            .map {
                if (it is StepQuizHintsFeature.Action.ViewAction) {
                    Action.ViewAction.StepQuizHintsViewAction(it)
                } else {
                    Action.StepQuizHintsAction(it)
                }
            }
            .toSet()

        return stepQuizHintsState to actions
    }
}