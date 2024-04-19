package org.hyperskill.step_quiz

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.onboarding.domain.model.ProblemsOnboardingFlags
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedTheoryToolbarItemHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.presentation.StepQuizChildFeatureReducer
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizReducer
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature
import org.hyperskill.app.submissions.domain.model.Submission
import org.hyperskill.app.submissions.domain.model.SubmissionStatus
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.SubscriptionType
import org.hyperskill.onboarding.domain.model.stub
import org.hyperskill.profile.stub
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

        val expectedState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = attempt,
                submissionState = submissionState,
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState()
        )

        stepRoutes.forEach { stepRoute ->
            val reducer = StepQuizReducer(
                stepRoute = stepRoute,
                stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
            )
            val (state, actions) = reducer.reduce(
                StepQuizFeature.State(
                    stepQuizState = StepQuizFeature.StepQuizState.Loading,
                    stepQuizHintsState = StepQuizHintsFeature.State.Idle,
                    stepQuizToolbarState = StepQuizToolbarFeature.initialState()
                ),
                StepQuizFeature.InternalMessage.FetchAttemptSuccess(
                    step,
                    attempt,
                    submissionState,
                    subscription = limitReachedSubscription,
                    profile = Profile.stub(),
                    problemsOnboardingFlags = ProblemsOnboardingFlags.stub(),
                    isMobileGptCodeGenerationWithErrorsEnabled = false
                )
            )

            assertEquals(expectedState, state)
            assertEquals(emptySet(), actions)
        }
    }

    @Test
    fun `When problems limit reached blocks learn solving`() {
        val step = Step.stub(id = 1)
        val stepRoute = StepRoute.Learn.Step(step.id)
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
            stepQuizToolbarState = StepQuizToolbarFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )
        val (state, actions) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Loading,
                stepQuizHintsState = StepQuizHintsFeature.State.Idle,
                stepQuizToolbarState = StepQuizToolbarFeature.initialState()
            ),
            StepQuizFeature.InternalMessage.FetchAttemptSuccess(
                step,
                attempt,
                submissionState,
                subscription = limitReachedSubscription,
                profile = Profile.stub(),
                problemsOnboardingFlags = ProblemsOnboardingFlags.stub(),
                isMobileGptCodeGenerationWithErrorsEnabled = false
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
        val stepRoute = StepRoute.Learn.Step(step.id)
        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Empty(),
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState()
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
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState()
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
            stepQuizToolbarState = StepQuizToolbarFeature.initialState()
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
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState()
        )

        assertEquals(expectedState, actualState)
        assertTrue(actualActions.isEmpty())
    }

    @Test
    fun `When updated problems limits reached for step with limited attempts blocks solving`() {
        val step = Step.stub(id = 1)

        val subscription = limitReachedSubscription
        val profile = Profile.stub()
        val stepRoute = StepRoute.Learn.Step(step.id)

        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Empty(),
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        val (actualState, actualActions) = reducer.reduce(
            initialState,
            StepQuizFeature.InternalMessage.UpdateProblemsLimitResult(
                subscription = subscription,
                profile = profile
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
            stepQuizToolbarState = StepQuizToolbarFeature.initialState()
        )

        assertEquals(expectedState, actualState)
        assertContains(
            actualActions,
            StepQuizFeature.Action.ViewAction.ShowProblemsLimitReachedModal(subscription, profile, stepRoute)
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
            stepQuizToolbarState = StepQuizToolbarFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        val (actualState, actualActions) = reducer.reduce(
            initialState,
            StepQuizFeature.InternalMessage.UpdateProblemsLimitResult(
                subscription = limitReachedSubscription,
                profile = Profile.stub()
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
            stepQuizToolbarState = StepQuizToolbarFeature.initialState()
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
        val stepRoute = StepRoute.Learn.Step(step.id)
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
            stepQuizToolbarState = StepQuizToolbarFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = stepRoute,
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(stepRoute)
        )

        val (intermediateState, _) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Loading,
                stepQuizHintsState = StepQuizHintsFeature.State.Idle,
                stepQuizToolbarState = StepQuizToolbarFeature.initialState()
            ),
            StepQuizFeature.InternalMessage.FetchAttemptSuccess(
                step,
                attempt,
                submissionState,
                subscription = Subscription.stub(),
                profile = Profile.stub(),
                problemsOnboardingFlags = ProblemsOnboardingFlags.stub(),
                isMobileGptCodeGenerationWithErrorsEnabled = false
            )
        )

        val (finalState, finalActions) = reducer.reduce(
            intermediateState,
            StepQuizFeature.Message.TheoryToolbarItemClicked
        )

        assertEquals(expectedState, finalState)
        assertTrue {
            finalActions.any {
                it is StepQuizFeature.Action.ViewAction.NavigateTo.StepScreen &&
                    it.stepRoute == StepRoute.Learn.TheoryOpenedFromPractice(topicTheoryId)
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

        val expectedState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = attempt,
                submissionState = submissionState,
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle,
            stepQuizToolbarState = StepQuizToolbarFeature.initialState()
        )

        val reducer = StepQuizReducer(
            stepRoute = StepRoute.LearnDaily(step.id),
            stepQuizChildFeatureReducer = StepQuizChildFeatureReducer.stub(StepRoute.Learn.Step(step.id))
        )

        val (intermediateState, _) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Loading,
                stepQuizHintsState = StepQuizHintsFeature.State.Idle,
                stepQuizToolbarState = StepQuizToolbarFeature.initialState()
            ),
            StepQuizFeature.InternalMessage.FetchAttemptSuccess(
                step,
                attempt,
                submissionState,
                subscription = Subscription.stub(),
                profile = Profile.stub(),
                problemsOnboardingFlags = ProblemsOnboardingFlags.stub(),
                isMobileGptCodeGenerationWithErrorsEnabled = false
            )
        )

        val (finalState, finalActions) = reducer.reduce(
            intermediateState,
            StepQuizFeature.Message.TheoryToolbarItemClicked
        )

        assertEquals(expectedState, finalState)
        assertTrue {
            finalActions.none {
                it is StepQuizFeature.Action.ViewAction.NavigateTo.StepScreen
            }
        }
    }
}