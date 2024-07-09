package org.hyperskill.app.step_quiz.presentation

import kotlinx.datetime.Clock
import org.hyperskill.app.onboarding.domain.model.ProblemsOnboardingFlags
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalContext
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalLaunchSource
import org.hyperskill.app.step.domain.model.BlockName
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.analytic.ProblemOnboardingModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.ProblemOnboardingModalShownHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedCodeDetailsHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedOpenFullScreenCodeEditorHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedRetryHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedRunHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedSendHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedStepTextDetailsHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedTheoryToolbarItemHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizCodeEditorClickedInputAccessoryButtonHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizFixGptGeneratedCodeMistakesBadgeClickedQuestionMarkHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizFullScreenCodeEditorClickedCodeDetailsHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizFullScreenCodeEditorClickedStepTextDetailsHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizGptGeneratedCodeWithErrorsHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizUnsupportedClickedGoToStudyPlanHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizUnsupportedClickedSolveOnTheWebHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Action
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.InternalAction
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.InternalMessage
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.Message
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.State
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature.StepQuizState
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.Submission
import org.hyperskill.app.submissions.domain.model.SubmissionStatus
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.isProblemsLimitReached
import ru.nobird.app.presentation.redux.reducer.StateReducer

internal typealias StepQuizReducerResult = Pair<State, Set<Action>>

internal class StepQuizReducer(
    private val stepRoute: StepRoute,
    private val stepQuizChildFeatureReducer: StepQuizChildFeatureReducer
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
            is InternalMessage.GenerateGptCodeWithErrorsResult ->
                handleGenerateGptCodeWithErrorsResult(state, message)
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
                        Action.CreateSubmissionValidateReply(
                            step = message.step,
                            dataset = state.stepQuizState.attempt.dataset,
                            reply = message.reply
                        ),
                        InternalAction.LogAnalyticEvent(analyticEvent)
                    )
                } else {
                    null
                }
            is Message.CreateSubmissionReplyValidationResult ->
                handleCreateSubmissionReplyValidationResult(state, message)
            is Message.CreateSubmissionSuccess ->
                handleCreateSubmissionSuccess(state, message)
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
            Message.FixGptGeneratedCodeMistakesBadgeClickedQuestionMark ->
                handleFixGptGeneratedCodeMistakesBadgeClickedQuestionMark(state)

            // Wrapper Messages
            is StepQuizFeature.ChildFeatureMessage -> stepQuizChildFeatureReducer.reduce(state, message)
        } ?: (state to emptySet())

    private fun handleFetchAttemptSuccess(
        state: State,
        message: InternalMessage.FetchAttemptSuccess
    ): StepQuizReducerResult =
        if (state.stepQuizState is StepQuizState.Loading) {
            if (StepQuizResolver.isIdeRequired(message.step, message.submissionState)) {
                state.copy(stepQuizState = StepQuizState.Unsupported) to emptySet()
            } else {
                val isProblemsLimitReached = isProblemsLimitReached(stepRoute, message.subscription)
                val isMobileGptCodeGenerationWithErrorsAvailable =
                    StepQuizResolver.isMobileGptCodeGenerationWithErrorsAvailable(
                        step = message.step,
                        submissionState = message.submissionState,
                        isMobileGptCodeGenerationWithErrorsEnabled = message.isMobileGptCodeGenerationWithErrorsEnabled,
                        isMobileGptCodeGenerationWithErrorsOnboardingShown =
                        message.problemsOnboardingFlags.isGptCodeGenerationWithErrorsOnboardingShown
                    )

                val stepQuizState = StepQuizState.AttemptLoaded(
                    step = message.step,
                    attempt = message.attempt,
                    submissionState = message.submissionState,
                    isProblemsLimitReached = isProblemsLimitReached,
                    isTheoryAvailable = StepQuizResolver.isTheoryAvailable(stepRoute, message.step)
                )

                if (isMobileGptCodeGenerationWithErrorsAvailable && !isProblemsLimitReached) {
                    state to setOf(InternalAction.GenerateGptCodeWithErrors(stepQuizState))
                } else {
                    state.copy(stepQuizState = stepQuizState) to
                        if (isProblemsLimitReached && shouldShowProblemsLimitModal(message.subscription)) {
                            showProblemsLimitReachedModal(message.subscription, message.chargeLimitsStrategy)
                        } else {
                            getProblemOnboardingModalActions(
                                step = message.step,
                                problemsOnboardingFlags = message.problemsOnboardingFlags
                            )
                        }
                }
            }
        } else {
            state to emptySet()
        }

    private fun handleGenerateGptCodeWithErrorsResult(
        state: State,
        message: InternalMessage.GenerateGptCodeWithErrorsResult
    ): StepQuizReducerResult {
        val defaultResult: StepQuizReducerResult =
            state.copy(stepQuizState = message.attemptLoadedState) to emptySet()

        val code = message.code?.takeIf { it.isNotBlank() } ?: return defaultResult
        val reply = when (message.attemptLoadedState.step.block.name) {
            BlockName.CODE -> Reply.code(code = code, language = null)
            BlockName.PYCHARM -> Reply.pycharm(step = message.attemptLoadedState.step, pycharmCode = code)
            BlockName.SQL -> Reply.sql(sqlCode = code)
            else -> null
        } ?: return defaultResult

        val stepQuizState = message.attemptLoadedState.copy(
            submissionState = StepQuizFeature.SubmissionState.Empty(reply = reply),
            isFixGptCodeGenerationMistakesBadgeVisible = true
        )

        return state.copy(stepQuizState = stepQuizState) to setOf(
            InternalAction.LogAnalyticEvent(
                StepQuizGptGeneratedCodeWithErrorsHyperskillAnalyticEvent(
                    stepRoute = stepRoute,
                    code = code
                )
            ),
            Action.ViewAction.ShowProblemOnboardingModal(
                modalType = StepQuizFeature.ProblemOnboardingModal.GptCodeGenerationWithErrors
            )
        )
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
                stepQuizChildFeatureReducer.reduceStepQuizHintsMessage(
                    state.stepQuizHintsState,
                    StepQuizHintsFeature.Message.InitWithStepId(message.step.id)
                )
            } else {
                StepQuizHintsFeature.State.Idle to emptySet()
            }

        val (stepQuizToolbarState, stepQuizToolbarActions) =
            stepQuizChildFeatureReducer.reduceStepQuizToolbarMessage(
                state.stepQuizToolbarState,
                StepQuizToolbarFeature.InternalMessage.Initialize
            )

        return state.copy(
            stepQuizState = stepQuizState,
            stepQuizHintsState = stepQuizHintsState,
            stepQuizToolbarState = stepQuizToolbarState
        ) to stepQuizActions + stepQuizHintsActions + stepQuizToolbarActions
    }

    private fun handleCreateSubmissionReplyValidationResult(
        state: State,
        message: Message.CreateSubmissionReplyValidationResult
    ): StepQuizReducerResult? =
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
                    ) to setOf(
                        Action.ViewAction.HapticFeedback.ReplyValidationError,
                        Action.ViewAction.ScrollToCallToActionButton
                    )
                }
                ReplyValidationResult.Success -> {
                    val reply = prepareReplyForSubmission(state.stepQuizState.step.block.name, message.reply)
                    val submission = createLocalSubmission(state.stepQuizState, reply)
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
                        ),
                        Action.ViewAction.ScrollToCallToActionButton
                    )
                }
            }
        } else {
            null
        }

    private fun handleCreateSubmissionSuccess(
        state: State,
        message: Message.CreateSubmissionSuccess
    ): StepQuizReducerResult? =
        if (state.stepQuizState is StepQuizState.AttemptLoaded) {
            state.copy(
                stepQuizState = state.stepQuizState.copy(
                    attempt = message.newAttempt ?: state.stepQuizState.attempt,
                    submissionState = StepQuizFeature.SubmissionState.Loaded(message.submission)
                )
            ) to buildSet {
                val submissionStatus = message.submission.status

                if (submissionStatus == SubmissionStatus.WRONG &&
                    StepQuizResolver.isStepHasLimitedAttempts(stepRoute)
                ) {
                    add(InternalAction.UpdateProblemsLimit(FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION))
                }

                if (submissionStatus == SubmissionStatus.CORRECT) {
                    add(Action.ViewAction.HapticFeedback.CorrectSubmission)
                    add(Action.ViewAction.ScrollToCallToActionButton)
                } else if (submissionStatus == SubmissionStatus.WRONG) {
                    add(Action.ViewAction.HapticFeedback.WrongSubmission)
                    add(Action.ViewAction.ScrollToCallToActionButton)
                }
            }
        } else {
            null
        }

    private fun handleUpdateProblemsLimitResult(
        state: State,
        message: InternalMessage.UpdateProblemsLimitResult
    ): StepQuizReducerResult? =
        if (state.stepQuizState is StepQuizState.AttemptLoaded) {
            val isProblemsLimitReached = isProblemsLimitReached(stepRoute, message.subscription)
            state.copy(
                stepQuizState = state.stepQuizState.copy(isProblemsLimitReached = isProblemsLimitReached)
            ) to if (isProblemsLimitReached && shouldShowProblemsLimitModal(message.subscription)) {
                showProblemsLimitReachedModal(message.subscription, message.chargeLimitsStrategy)
            } else {
                emptySet()
            }
        } else {
            null
        }

    private fun handleProblemsLimitChanged(
        state: State,
        message: InternalMessage.ProblemsLimitChanged
    ): StepQuizReducerResult? =
        if (state.stepQuizState is StepQuizState.AttemptLoaded) {
            val isProblemsLimitReached = isProblemsLimitReached(stepRoute, message.subscription)
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

    private fun isProblemsLimitReached(stepRoute: StepRoute, subscription: Subscription): Boolean =
        StepQuizResolver.isStepHasLimitedAttempts(stepRoute) && subscription.isProblemsLimitReached

    private fun shouldShowProblemsLimitModal(subscription: Subscription): Boolean =
        subscription.isProblemsLimitReached && subscription.stepsLimitTotal != null

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
                            StepRoute.Learn.TheoryOpenedFromPractice(
                                stepId = topicTheoryId,
                                topicId = state.stepQuizState.step.topic
                            )
                        is StepRoute.Learn.TheoryOpenedFromPractice,
                        is StepRoute.Learn.TheoryOpenedFromSearch,
                        is StepRoute.LearnDaily,
                        is StepRoute.Repeat.Theory,
                        is StepRoute.StageImplement ->
                            null
                    }?.let { targetStepRoute ->
                        add(Action.ViewAction.NavigateTo.TheoryStepScreen(targetStepRoute))
                    } ?: add(Action.ViewAction.ShowNetworkError)
                }
            }
        } else {
            state to emptySet()
        }

    private fun handleFixGptGeneratedCodeMistakesBadgeClickedQuestionMark(state: State): StepQuizReducerResult =
        state to setOf(
            InternalAction.LogAnalyticEvent(
                StepQuizFixGptGeneratedCodeMistakesBadgeClickedQuestionMarkHyperskillAnalyticEvent(stepRoute)
            ),
            Action.ViewAction.ShowProblemOnboardingModal(
                modalType = StepQuizFeature.ProblemOnboardingModal.GptCodeGenerationWithErrors
            )
        )

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

    private fun prepareReplyForSubmission(stepBlockName: String, reply: Reply): Reply =
        when (stepBlockName) {
            BlockName.STRING -> reply.copy(text = reply.text?.trim())
            BlockName.NUMBER -> reply.copy(number = reply.number?.trim())
            BlockName.MATH -> reply.copy(formula = reply.formula?.trim())
            BlockName.PROMPT -> reply.copy(prompt = reply.prompt?.trim())
            else -> reply
        }

    private fun getProblemOnboardingModalActions(
        step: Step,
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
            else -> emptySet()
        }

    private fun showProblemsLimitReachedModal(
        subscription: Subscription,
        chargeLimitsStrategy: FreemiumChargeLimitsStrategy
    ): Set<Action> =
        setOf(
            Action.ViewAction.ShowProblemsLimitReachedModal(
                subscription = subscription,
                chargeLimitsStrategy = chargeLimitsStrategy,
                context = ProblemsLimitInfoModalContext.Step(
                    launchSource = ProblemsLimitInfoModalLaunchSource.AUTOMATIC_NO_LIMITS_LEFT,
                    stepRoute = stepRoute
                )
            )
        )
}