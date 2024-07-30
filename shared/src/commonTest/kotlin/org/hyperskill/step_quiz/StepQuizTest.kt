package org.hyperskill.step_quiz

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.hyperskill.app.comments.domain.model.CommentStatisticsEntry
import org.hyperskill.app.comments.domain.model.CommentThread
import org.hyperskill.app.onboarding.domain.model.ProblemsOnboardingFlags
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalContext
import org.hyperskill.app.problems_limit_info.domain.model.ProblemsLimitInfoModalLaunchSource
import org.hyperskill.app.step.domain.model.Block
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedTheoryToolbarItemHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.domain.validation.ReplyValidationResult
import org.hyperskill.app.step_quiz.presentation.StepQuizChildFeatureReducer
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizReducer
import org.hyperskill.app.step_quiz_code_blanks.presentation.StepQuizCodeBlanksFeature
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature
import org.hyperskill.app.submissions.domain.model.Reply
import org.hyperskill.app.submissions.domain.model.Submission
import org.hyperskill.app.submissions.domain.model.SubmissionStatus
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType
import org.hyperskill.onboarding.domain.model.stub
import org.hyperskill.step.domain.model.stub
import org.hyperskill.step_quiz.domain.model.stub
import org.hyperskill.step_quiz.presentation.stub
import org.hyperskill.subscriptions.stub

class StepQuizTest {
    companion object {
        private val limitReachedSubscription: Subscription =
            Subscription.stub(
                type = SubscriptionType.FREEMIUM,
                stepsLimitLeft = 0,
                stepsLimitTotal = 5
            )
    }

    @Test
    fun `When problems limit reached not blocks problem of the day and repeat solving`() {
        val step = Step.stub(id = 1)
        val attempt = Attempt.stub()
        val submissionState = StepQuizFeature.SubmissionState.Empty()
        val stepRoutes = listOf(StepRoute.LearnDaily(step.id), StepRoute.Repeat.Practice(step.id))

        stepRoutes.forEach { stepRoute ->
            val expectedState = StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                    step = step,
                    attempt = attempt,
                    submissionState = submissionState,
                    isProblemsLimitReached = false,
                    isTheoryAvailable = false
                ),
                stepQuizHintsState = StepQuizHintsFeature.State.Idle,
                stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
                stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
            )

            val reducer = StepQuizReducer(
                stepRoute = stepRoute,
                stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
            )
            val (state, actions) = reducer.reduce(
                StepQuizFeature.State(
                    stepQuizState = StepQuizFeature.StepQuizState.Loading,
                    stepQuizHintsState = StepQuizHintsFeature.State.Idle,
                    stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
                    stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
                ),
                StepQuizFeature.InternalMessage.FetchAttemptSuccess(
                    step,
                    attempt,
                    submissionState,
                    subscription = limitReachedSubscription,
                    chargeLimitsStrategy = FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION,
                    problemsOnboardingFlags = ProblemsOnboardingFlags.stub(),
                    isProblemsLimitReached = true
                )
            )

            assertEquals(expectedState, state)
            assertEquals(emptySet(), actions)
        }
    }

    @Test
    fun `When problems limit reached blocks learn solving`() {
        val step = Step.stub(id = 1)
        val stepRoute = StepRoute.Learn.Step(step.id, null)
        val attempt = Attempt.stub()
        val submissionState = StepQuizFeature.SubmissionState.Empty()

        val expectedState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = attempt,
                submissionState = submissionState,
                isProblemsLimitReached = true,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )
        val (state, actions) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Loading,
                stepQuizHintsState = StepQuizHintsFeature.State.Idle,
                stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
                stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
            ),
            StepQuizFeature.InternalMessage.FetchAttemptSuccess(
                step,
                attempt,
                submissionState,
                subscription = limitReachedSubscription,
                chargeLimitsStrategy = FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION,
                problemsOnboardingFlags = ProblemsOnboardingFlags.stub(),
                isProblemsLimitReached = true
            )
        )

        assertEquals(expectedState, state)
        assertTrue {
            actions.any { it is StepQuizFeature.Action.ViewAction.ShowProblemsLimitReachedModal }
        }
    }

    @Test
    fun `Receiving wrong submission for step with limited attempts updates problems limits`() {
        val step = Step.stub(id = 1)
        val stepRoute = StepRoute.Learn.Step(step.id, null)
        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Empty(),
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        val (actualState, actualActions) = reducer.reduce(
            initialState,
            StepQuizFeature.Message.CreateSubmissionSuccess(Submission.stub(status = SubmissionStatus.WRONG))
        )

        val expectedState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Loaded(
                    Submission.stub(status = SubmissionStatus.WRONG)
                ),
                isProblemsLimitReached = false,
                isTheoryAvailable = false,
                wrongSubmissionsCount = 1
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
        )

        assertEquals(expectedState, actualState)
        assertContains(
            actualActions,
            StepQuizFeature.InternalAction.UpdateProblemsLimit(FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION)
        )
    }

    @Test
    fun `Receiving wrong submission for step without limited attempts not updates problems limits`() {
        val step = Step.stub(id = 1)
        val stepRoute = StepRoute.LearnDaily(step.id)
        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Empty(),
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        val (actualState, actualActions) = reducer.reduce(
            initialState,
            StepQuizFeature.Message.CreateSubmissionSuccess(Submission.stub(status = SubmissionStatus.WRONG))
        )

        val expectedState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Loaded(
                    Submission.stub(status = SubmissionStatus.WRONG)
                ),
                isProblemsLimitReached = false,
                isTheoryAvailable = false,
                wrongSubmissionsCount = 1
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState(),
        )

        assertEquals(expectedState, actualState)
        assertTrue {
            actualActions.none {
                it is StepQuizFeature.InternalAction.UpdateProblemsLimit
            }
        }
    }

    @Test
    fun `When updated problems limits reached for step with limited attempts blocks solving`() {
        val step = Step.stub(id = 1)

        val subscription = limitReachedSubscription
        val stepRoute = StepRoute.Learn.Step(step.id, null)
        val chargeLimitsStrategy = FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION

        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Empty(),
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        val (actualState, actualActions) = reducer.reduce(
            initialState,
            StepQuizFeature.InternalMessage.UpdateProblemsLimitResult(
                subscription = subscription,
                chargeLimitsStrategy = chargeLimitsStrategy,
                isProblemsLimitReached = true
            )
        )

        val expectedState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Empty(),
                isProblemsLimitReached = true,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
        )

        assertEquals(expectedState, actualState)
        assertContains(
            actualActions,
            StepQuizFeature.Action.ViewAction.ShowProblemsLimitReachedModal(
                subscription = subscription,
                chargeLimitsStrategy = chargeLimitsStrategy,
                context = ProblemsLimitInfoModalContext.Step(
                    launchSource = ProblemsLimitInfoModalLaunchSource.AUTOMATIC_NO_LIMITS_LEFT,
                    stepRoute = stepRoute
                )
            )
        )
    }

    @Test
    fun `When updated problems limits reached for step without limited attempts not blocks solving`() {
        val step = Step.stub(id = 1)
        val stepRoute = StepRoute.LearnDaily(step.id)
        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Empty(),
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        val (actualState, actualActions) = reducer.reduce(
            initialState,
            StepQuizFeature.InternalMessage.UpdateProblemsLimitResult(
                subscription = limitReachedSubscription,
                chargeLimitsStrategy = FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION,
                isProblemsLimitReached = true
            )
        )

        val expectedState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Empty(),
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
        )

        assertEquals(expectedState, actualState)
        assertTrue {
            actualActions.none {
                it is StepQuizFeature.Action.ViewAction.ShowProblemsLimitReachedModal
            }
        }
    }

    @Test
    fun `TheoryToolbarClicked message navigates to step screen when theory available`() {
        val topicTheoryId = 2L
        val step = Step.stub(id = 1, topicTheory = topicTheoryId)
        val stepRoute = StepRoute.Learn.Step(step.id, null)
        val attempt = Attempt.stub()
        val submissionState = StepQuizFeature.SubmissionState.Empty()

        val expectedState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = attempt,
                submissionState = submissionState,
                isProblemsLimitReached = false,
                isTheoryAvailable = true
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        val (intermediateState, _) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Loading,
                stepQuizHintsState = StepQuizHintsFeature.State.Idle,
                stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
                stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
            ),
            StepQuizFeature.InternalMessage.FetchAttemptSuccess(
                step,
                attempt,
                submissionState,
                subscription = Subscription.stub(),
                chargeLimitsStrategy = FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION,
                problemsOnboardingFlags = ProblemsOnboardingFlags.stub(),
                isProblemsLimitReached = false
            )
        )

        val (finalState, finalActions) = reducer.reduce(
            intermediateState,
            StepQuizFeature.Message.TheoryToolbarItemClicked
        )

        assertEquals(expectedState, finalState)
        assertTrue {
            finalActions.any {
                it is StepQuizFeature.Action.ViewAction.NavigateTo.TheoryStepScreen &&
                    it.stepRoute == StepRoute.Learn.TheoryOpenedFromPractice(topicTheoryId, null)
            }
        }
        assertTrue {
            val analyticActions = finalActions.filterIsInstance<StepQuizFeature.InternalAction.LogAnalyticEvent>()
            analyticActions.any {
                (it.analyticEvent as StepQuizClickedTheoryToolbarItemHyperskillAnalyticEvent)
                    .topicTheoryId == topicTheoryId
            }
        }
    }

    @Test
    fun `TheoryToolbarClicked message not navigates to step screen when theory unavailable`() {
        val step = Step.stub(id = 1, topicTheory = 2)
        val attempt = Attempt.stub()
        val submissionState = StepQuizFeature.SubmissionState.Empty()
        val stepRoute = StepRoute.LearnDaily(step.id)

        val expectedState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = attempt,
                submissionState = submissionState,
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(StepRoute.Learn.Step(step.id, null))
        )

        val (intermediateState, _) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Loading,
                stepQuizHintsState = StepQuizHintsFeature.State.Idle,
                stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
                stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
            ),
            StepQuizFeature.InternalMessage.FetchAttemptSuccess(
                step,
                attempt,
                submissionState,
                subscription = Subscription.stub(),
                chargeLimitsStrategy = FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION,
                problemsOnboardingFlags = ProblemsOnboardingFlags.stub(),
                isProblemsLimitReached = false
            )
        )

        val (finalState, finalActions) = reducer.reduce(
            intermediateState,
            StepQuizFeature.Message.TheoryToolbarItemClicked
        )

        assertEquals(expectedState, finalState)
        assertTrue {
            finalActions.none {
                it is StepQuizFeature.Action.ViewAction.NavigateTo.TheoryStepScreen
            }
        }
    }

    @Test
    fun `HapticFeedbackCorrectSubmission triggers when submission is correct`() {
        val stepRoute = StepRoute.Learn.Step(1, null)
        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = Step.stub(id = stepRoute.stepId),
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Empty(),
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        val (_, actions) = reducer.reduce(
            initialState,
            StepQuizFeature.Message.CreateSubmissionSuccess(
                submission = Submission(status = SubmissionStatus.CORRECT),
                newAttempt = null
            )
        )

        assertContains(actions, StepQuizFeature.Action.ViewAction.HapticFeedback.CorrectSubmission)
    }

    @Test
    fun `HapticFeedbackWrongSubmission triggers when submission is wrong`() {
        val stepRoute = StepRoute.Learn.Step(1, null)
        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = Step.stub(id = stepRoute.stepId),
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Empty(),
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        val (_, actions) = reducer.reduce(
            initialState,
            StepQuizFeature.Message.CreateSubmissionSuccess(
                submission = Submission(status = SubmissionStatus.WRONG),
                newAttempt = null
            )
        )

        assertContains(actions, StepQuizFeature.Action.ViewAction.HapticFeedback.WrongSubmission)
    }

    @Test
    fun `HapticFeedbackReplyValidationError triggers when ReplyValidationResult is Error`() {
        val step = Step.stub(id = 1)
        val stepRoute = StepRoute.Learn.Step(step.id, null)
        val attempt = Attempt.stub()
        val submissionState = StepQuizFeature.SubmissionState.Empty()

        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = attempt,
                submissionState = submissionState,
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        val (_, actions) = reducer.reduce(
            initialState,
            StepQuizFeature.Message.CreateSubmissionReplyValidationResult(
                step = step,
                reply = Reply(),
                replyValidation = ReplyValidationResult.Error("Error message")
            )
        )

        assertContains(actions, StepQuizFeature.Action.ViewAction.HapticFeedback.ReplyValidationError)
    }

    @Test
    fun `ScrollToCallToActionButton triggers when receiving ReplyValidationResult`() {
        val step = Step.stub(id = 1)
        val stepRoute = StepRoute.Learn.Step(step.id, null)
        val attempt = Attempt.stub()
        val submissionState = StepQuizFeature.SubmissionState.Empty()

        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = attempt,
                submissionState = submissionState,
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        listOf(
            ReplyValidationResult.Success,
            ReplyValidationResult.Error("Error message")
        ).forEach { replyValidation ->
            val (_, actions) = reducer.reduce(
                initialState,
                StepQuizFeature.Message.CreateSubmissionReplyValidationResult(
                    step = step,
                    reply = Reply(),
                    replyValidation = replyValidation
                )
            )

            assertContains(actions, StepQuizFeature.Action.ViewAction.ScrollToCallToActionButton)
        }
    }

    @Test
    fun `ScrollToCallToActionButton triggers when receiving correct or wrong submission`() {
        val stepRoute = StepRoute.Learn.Step(1, null)
        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = Step.stub(id = stepRoute.stepId),
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Empty(),
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        listOf(
            SubmissionStatus.CORRECT,
            SubmissionStatus.WRONG
        ).forEach { submissionStatus ->
            val (_, actions) = reducer.reduce(
                initialState,
                StepQuizFeature.Message.CreateSubmissionSuccess(
                    submission = Submission(status = submissionStatus),
                    newAttempt = null
                )
            )

            assertContains(actions, StepQuizFeature.Action.ViewAction.ScrollToCallToActionButton)
        }
    }

    @Test
    fun `StepQuizCodeBlanksFeature should be initialized when isCodeBlanksFeatureAvailable returns true`() {
        val step = Step.stub(
            id = 1,
            block = Block.stub(
                options = Block.Options(
                    codeBlanksStrings = listOf("a", "b")
                )
            )
        )
        val attempt = Attempt.stub()
        val submissionState = StepQuizFeature.SubmissionState.Empty()
        val stepRoute = StepRoute.Learn.Step(step.id, null)

        val expectedState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = attempt,
                submissionState = submissionState,
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.State.Content(
                step = step,
                codeBlocks = emptyList()
            )
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )
        val (state, _) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Loading,
                stepQuizHintsState = StepQuizHintsFeature.State.Idle,
                stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
                stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
            ),
            StepQuizFeature.InternalMessage.FetchAttemptSuccess(
                step,
                attempt,
                submissionState,
                subscription = Subscription.stub(),
                isProblemsLimitReached = false,
                chargeLimitsStrategy = FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION,
                problemsOnboardingFlags = ProblemsOnboardingFlags.stub()
            )
        )

        assertEquals(expectedState.stepQuizState, state.stepQuizState)
        assertEquals(expectedState.stepQuizHintsState, state.stepQuizHintsState)
        assertEquals(expectedState.stepQuizToolbarState, state.stepQuizToolbarState)
        assertTrue(state.stepQuizCodeBlanksState is StepQuizCodeBlanksFeature.State.Content)
    }

    @Test
    fun `StepQuizCodeBlanksFeature should not be initialized when isCodeBlanksFeatureAvailable returns false`() {
        val step = Step.stub(id = 1)
        val attempt = Attempt.stub()
        val submissionState = StepQuizFeature.SubmissionState.Empty()
        val stepRoute = StepRoute.Learn.Step(step.id, null)

        val expectedState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = attempt,
                submissionState = submissionState,
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
            stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.State.Idle
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )
        val (state, actions) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Loading,
                stepQuizHintsState = StepQuizHintsFeature.State.Idle,
                stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
                stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
            ),
            StepQuizFeature.InternalMessage.FetchAttemptSuccess(
                step,
                attempt,
                submissionState,
                subscription = Subscription.stub(),
                isProblemsLimitReached = false,
                chargeLimitsStrategy = FreemiumChargeLimitsStrategy.AFTER_WRONG_SUBMISSION,
                problemsOnboardingFlags = ProblemsOnboardingFlags.stub()
            )
        )

        assertEquals(expectedState.stepQuizState, state.stepQuizState)
        assertEquals(expectedState.stepQuizHintsState, state.stepQuizHintsState)
        assertEquals(expectedState.stepQuizToolbarState, state.stepQuizToolbarState)
        assertTrue(state.stepQuizCodeBlanksState is StepQuizCodeBlanksFeature.State.Idle)

        assertFalse {
            actions.any {
                it is StepQuizFeature.Action.StepQuizCodeBlanksAction ||
                    it is StepQuizFeature.Action.ViewAction.StepQuizCodeBlanksViewAction
            }
        }
    }

    @Test
    fun `Wrong and rejected submissions should increment wrongSubmissionsCount on CreateSubmissionSuccess`() {
        val step = Step.stub(id = 1)
        val attempt = Attempt.stub()
        val submissionState = StepQuizFeature.SubmissionState.Loaded(
            Submission.stub(status = SubmissionStatus.EVALUATION)
        )
        val stepRoute = StepRoute.Learn.Step(step.id, null)

        val expectedWrongSubmissionsCount = 1

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        listOf(
            SubmissionStatus.WRONG,
            SubmissionStatus.REJECTED
        ).forEach { submissionStatus ->
            val (state, _) = reducer.reduce(
                StepQuizFeature.State(
                    stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                        step = step,
                        attempt = attempt,
                        submissionState = submissionState,
                        isProblemsLimitReached = false,
                        isTheoryAvailable = false,
                        wrongSubmissionsCount = 0
                    ),
                    stepQuizHintsState = StepQuizHintsFeature.State.Idle,
                    stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
                    stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
                ),
                StepQuizFeature.Message.CreateSubmissionSuccess(
                    submission = Submission.stub(status = submissionStatus)
                )
            )

            assertEquals(
                expectedWrongSubmissionsCount,
                (state.stepQuizState as? StepQuizFeature.StepQuizState.AttemptLoaded)?.wrongSubmissionsCount
            )
        }
    }

    @Test
    fun `WrongSubmissionsCount submissions should be kept on CreateAttemptSuccess`() {
        val step = Step.stub(id = 1)
        val attempt = Attempt.stub()
        val submissionState = StepQuizFeature.SubmissionState.Loaded(
            Submission.stub(status = SubmissionStatus.EVALUATION)
        )
        val stepRoute = StepRoute.Learn.Step(step.id, null)

        val expectedWrongSubmissionsCount = 1

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        val (state, _) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.AttemptLoading(
                    StepQuizFeature.StepQuizState.AttemptLoaded(
                        step = step,
                        attempt = attempt,
                        submissionState = submissionState,
                        isProblemsLimitReached = false,
                        isTheoryAvailable = false,
                        wrongSubmissionsCount = expectedWrongSubmissionsCount
                    )
                ),
                stepQuizHintsState = StepQuizHintsFeature.State.Idle,
                stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
                stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
            ),
            StepQuizFeature.Message.CreateAttemptSuccess(
                step = step,
                attempt = attempt,
                submissionState = submissionState,
                isProblemsLimitReached = false
            )
        )

        assertEquals(
            expectedWrongSubmissionsCount,
            (state.stepQuizState as? StepQuizFeature.StepQuizState.AttemptLoaded)?.wrongSubmissionsCount
        )
    }

    @Test
    fun `Non-error submissions should not increment wrongSubmissionsCount`() {
        val step = Step.stub(id = 1)
        val attempt = Attempt.stub()
        val submissionState = StepQuizFeature.SubmissionState.Loaded(
            Submission.stub(status = SubmissionStatus.EVALUATION)
        )
        val stepRoute = StepRoute.Learn.Step(step.id, null)

        val expectedWrongSubmissionsCount = 0

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        listOf(
            SubmissionStatus.LOCAL,
            SubmissionStatus.EVALUATION,
            SubmissionStatus.CORRECT
        ).forEach { submissionStatus ->
            val (state, _) = reducer.reduce(
                StepQuizFeature.State(
                    stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                        step = step,
                        attempt = attempt,
                        submissionState = submissionState,
                        isProblemsLimitReached = false,
                        isTheoryAvailable = false,
                        wrongSubmissionsCount = 0
                    ),
                    stepQuizHintsState = StepQuizHintsFeature.State.Idle,
                    stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
                    stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
                ),
                StepQuizFeature.Message.CreateSubmissionSuccess(
                    submission = Submission.stub(status = submissionStatus)
                )
            )

            assertEquals(
                expectedWrongSubmissionsCount,
                (state.stepQuizState as? StepQuizFeature.StepQuizState.AttemptLoaded)?.wrongSubmissionsCount
            )
        }
    }

    @Test
    fun `Clicking on the SeeHint button in the feedback should trigger hint loading`() {
        val step = Step.stub(
            id = 1,
            commentsStatistics = listOf(
                CommentStatisticsEntry(CommentThread.HINT, totalCount = 1)
            )
        )
        val attempt = Attempt.stub()
        val submissionState = StepQuizFeature.SubmissionState.Loaded(
            Submission.stub(status = SubmissionStatus.EVALUATION)
        )
        val stepRoute = StepRoute.Learn.Step(step.id, null)

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        val expectedNextHintId = 2L

        val (_, actions) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                    step = step,
                    attempt = attempt,
                    submissionState = submissionState,
                    isProblemsLimitReached = false,
                    isTheoryAvailable = false,
                    wrongSubmissionsCount = 0
                ),
                stepQuizHintsState = StepQuizHintsFeature.State.Content(
                    hintsIds = listOf(0, 1, expectedNextHintId),
                    currentHint = null,
                    hintHasReaction = false,
                    areHintsLimited = false,
                    stepId = step.id
                ),
                stepQuizToolbarState = StepQuizToolbarFeature.initialState(stepRoute),
                stepQuizCodeBlanksState = StepQuizCodeBlanksFeature.initialState()
            ),
            StepQuizFeature.Message.SeeHintClicked
        )

        assertContains(
            actions,
            StepQuizFeature.Action.StepQuizHintsAction(
                StepQuizHintsFeature.Action.FetchNextHint(
                    nextHintId = expectedNextHintId,
                    remainingHintsIds = listOf(0, 1),
                    areHintsLimited = false,
                    stepId = step.id
                )
            )
        )
    }
}