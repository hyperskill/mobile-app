package org.hyperskill.app.step_quiz.presentation

import kotlinx.datetime.Clock
import org.hyperskill.app.onboarding.domain.model.ProblemsOnboardingFlags
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.analytic.ProblemOnboardingModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.ProblemOnboardingModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.ProblemsLimitReachedModalClickedGoToHomeScreenHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.ProblemsLimitReachedModalClickedUnlockUnlimitedProblemsHSAnalyticEvent
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
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizGptGeneratedCodeWithErrorsHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizUnsupportedClickedGoToStudyPlanHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizUnsupportedClickedSolveOnTheWebHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
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
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.Submission
import org.hyperskill.app.submissions.domain.model.SubmissionStatus
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
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
            is InternalMessage.FetchAttemptSuccess ->
                handleFetchAttemptSuccess(state, message)
            is InternalMessage.FetchAttemptError ->
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
                            isFixGptCodeGenerationMistakesBadgeVisible = false,
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
                        InternalAction.LogAnalyticEvent(analyticEvent)
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
            is InternalMessage.ProblemsLimitChanged ->
                handleProblemsLimitChanged(state, message)
            is Message.ProblemsLimitReachedModalGoToHomeScreenClicked ->
                state to setOf(
                    Action.ViewAction.NavigateTo.Home,
                    InternalAction.LogAnalyticEvent(
                        ProblemsLimitReachedModalClickedGoToHomeScreenHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    )
                )
            is Message.ProblemsLimitReachedModalUnlockUnlimitedProblemsClicked ->
                state to setOf(
                    Action.ViewAction.NavigateTo.Paywall(PaywallTransitionSource.PROBLEMS_LIMIT_MODAL),
                    InternalAction.LogAnalyticEvent(
                        ProblemsLimitReachedModalClickedUnlockUnlimitedProblemsHSAnalyticEvent(stepRoute.analyticRoute)
                    )
                )
            is Message.ClickedCodeDetailsEventMessage ->
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    val event = StepQuizClickedCodeDetailsHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    state to setOf(InternalAction.LogAnalyticEvent(event))
                } else {
                    null
                }
            is Message.FullScreenCodeEditorClickedCodeDetailsEventMessage -> {
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    val event = StepQuizFullScreenCodeEditorClickedCodeDetailsHyperskillAnalyticEvent(
                        stepRoute.analyticRoute
                    )
                    state to setOf(InternalAction.LogAnalyticEvent(event))
                } else {
                    null
                }
            }
            is Message.ClickedStepTextDetailsEventMessage -> {
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    val event = StepQuizClickedStepTextDetailsHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    state to setOf(InternalAction.LogAnalyticEvent(event))
                } else {
                    null
                }
            }
            is Message.FullScreenCodeEditorClickedStepTextDetailsEventMessage -> {
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    val event = StepQuizFullScreenCodeEditorClickedStepTextDetailsHyperskillAnalyticEvent(
                        stepRoute.analyticRoute
                    )
                    state to setOf(InternalAction.LogAnalyticEvent(event))
                } else {
                    null
                }
            }
            is Message.ClickedOpenFullScreenCodeEditorEventMessage -> {
                if (state.stepQuizState is StepQuizState.AttemptLoaded) {
                    val event = StepQuizClickedOpenFullScreenCodeEditorHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    state to setOf(InternalAction.LogAnalyticEvent(event))
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
                    state to setOf(InternalAction.LogAnalyticEvent(event))
                } else {
                    null
                }
            }
            is Message.TheoryToolbarItemClicked ->
                handleTheoryToolbarItemClicked(state)
            Message.UnsupportedQuizGoToStudyPlanClicked ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        StepQuizUnsupportedClickedGoToStudyPlanHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    ),
                    Action.ViewAction.NavigateTo.StudyPlan
                )
            Message.UnsupportedQuizSolveOnTheWebClicked ->
                state to setOf(
                    Action.ViewAction.CreateMagicLinkState.Loading,
                    InternalAction.CreateMagicLinkForUnsupportedQuiz(stepRoute),
                    InternalAction.LogAnalyticEvent(
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
                    state to setOf(InternalAction.LogAnalyticEvent(event))
                } else {
                    null
                }
            is Message.ProblemsLimitReachedModalShownEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        ProblemsLimitReachedModalShownHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    )
                )
            is Message.ProblemsLimitReachedModalHiddenEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        ProblemsLimitReachedModalHiddenHyperskillAnalyticEvent(stepRoute.analyticRoute)
                    )
                )
            is Message.ProblemOnboardingModalShownMessage -> {
                state to setOf(
                    Action.SaveProblemOnboardingModalShownCacheFlag(modalType = message.modalType),
                    InternalAction.LogAnalyticEvent(
                        ProblemOnboardingModalShownHyperskillAnalyticEvent(
                            route = stepRoute.analyticRoute,
                            modalType = message.modalType
                        )
                    )
                )
            }
            is Message.ProblemOnboardingModalHiddenMessage -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
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

    private fun handleFetchAttemptSuccess(
        state: State,
        message: InternalMessage.FetchAttemptSuccess
    ): StepQuizReducerResult =
        if (state.stepQuizState is StepQuizState.Loading) {
            if (StepQuizResolver.isIdeRequired(message.step, message.submissionState)) {
                state.copy(stepQuizState = StepQuizState.Unsupported) to emptySet()
            } else {
                val isProblemsLimitReached =
                    StepQuizResolver.isStepHasLimitedAttempts(stepRoute) && message.isProblemsLimitReached

                val stepQuizState = StepQuizState.AttemptLoaded(
                    step = message.step,
                    attempt = message.attempt,
                    submissionState = message.submissionState,
                    isProblemsLimitReached = isProblemsLimitReached,
                    isFixGptCodeGenerationMistakesBadgeVisible = false,
                    isTheoryAvailable = StepQuizResolver.isTheoryAvailable(stepRoute, message.step)
                ).let {
                    applyGptGeneratedCodeWithErrorsIfNeeded(it, message.gptCodeGenerationWithErrorsData)
                }

                state.copy(stepQuizState = stepQuizState) to buildSet {
                    if (isProblemsLimitReached && message.problemsLimitReachedModalData != null) {
                        add(Action.ViewAction.ShowProblemsLimitReachedModal(message.problemsLimitReachedModalData))
                    } else {
                        addAll(
                            getProblemOnboardingModalActions(
                                step = message.step,
                                attempt = message.attempt,
                                problemsOnboardingFlags = message.problemsOnboardingFlags
                            )
                        )
                    }

                    if (stepQuizState.isFixGptCodeGenerationMistakesBadgeVisible) {
                        add(
                            InternalAction.LogAnalyticEvent(
                                StepQuizGptGeneratedCodeWithErrorsHyperskillAnalyticEvent(
                                    stepRoute = stepRoute,
                                    code = message.gptCodeGenerationWithErrorsData.code
                                )
                            )
                        )
                    }
                }
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
        if (state.stepQuizState is StepQuizState.AttemptLoaded) {
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

    private fun handleProblemsLimitChanged(
        state: State,
        message: InternalMessage.ProblemsLimitChanged
    ): StepQuizReducerResult? =
        if (state.stepQuizState is StepQuizState.AttemptLoaded) {
            val isProblemsLimitReached =
                StepQuizResolver.isStepHasLimitedAttempts(stepRoute) && message.isProblemsLimitReached
            val shouldHideProblemsLimitModal =
                state.stepQuizState.isProblemsLimitReached && !isProblemsLimitReached
            state.copy(
                stepQuizState = state.stepQuizState.copy(
                    isProblemsLimitReached = isProblemsLimitReached
                )
            ) to if (shouldHideProblemsLimitModal) {
                setOf(Action.ViewAction.HideProblemsLimitReachedModal)
            } else {
                emptySet()
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
                    InternalAction.LogAnalyticEvent(
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
                        is StepRoute.StageImplement ->
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

    @Suppress("ReturnCount")
    private fun applyGptGeneratedCodeWithErrorsIfNeeded(
        state: StepQuizState.AttemptLoaded,
        gptCodeGenerationWithErrorsData: StepQuizFeature.GptCodeGenerationWithErrorsData
    ): StepQuizState.AttemptLoaded {
        if (!gptCodeGenerationWithErrorsData.isEnabled) {
            return state
        }
        if (state.submissionState !is StepQuizFeature.SubmissionState.Empty) {
            return state
        }

        val code = gptCodeGenerationWithErrorsData.code?.takeIf { it.isNotBlank() } ?: return state

        val reply = when (state.step.block.name) {
            BlockName.CODE -> Reply.code(code = code, language = null)
            BlockName.PYCHARM -> Reply.pycharm(step = state.step, pycharmCode = code)
            BlockName.SQL -> Reply.sql(sqlCode = code)
            else -> null
        } ?: return state

        return state.copy(
            submissionState = StepQuizFeature.SubmissionState.Empty(reply = reply),
            isFixGptCodeGenerationMistakesBadgeVisible = true
        )
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