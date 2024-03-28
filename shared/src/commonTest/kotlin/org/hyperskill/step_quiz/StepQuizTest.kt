package org.hyperskill.step_quiz

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.onboarding.domain.model.ProblemsOnboardingFlags
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedTheoryToolbarItemHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizReducer
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsFeature
import org.hyperskill.app.step_quiz_hints.presentation.StepQuizHintsReducer
import org.hyperskill.app.submissions.domain.model.Submission
import org.hyperskill.app.submissions.domain.model.SubmissionStatus
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.step.domain.model.stub
import org.hyperskill.step_quiz.domain.model.stub

class StepQuizTest {
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
            stepQuizHintsState = StepQuizHintsFeature.State.Idle
        )

        stepRoutes.forEach { stepRoute ->
            val reducer = StepQuizReducer(
                stepRoute = stepRoute,
                stepQuizHintsReducer = StepQuizHintsReducer(stepRoute)
            )
            val (state, actions) = reducer.reduce(
                StepQuizFeature.State(
                    stepQuizState = StepQuizFeature.StepQuizState.Loading,
                    stepQuizHintsState = StepQuizHintsFeature.State.Idle
                ),
                StepQuizFeature.Message.FetchAttemptSuccess(
                    step,
                    attempt,
                    submissionState,
                    isProblemsLimitReached = true,
                    problemsLimitReachedModalData = StepQuizFeature.ProblemsLimitReachedModalData(
                        title = "",
                        description = "",
                        unlockLimitsButtonText = null
                    ),
                    problemsOnboardingFlags = ProblemsOnboardingFlags(
                        isParsonsOnboardingShown = false,
                        isFillBlanksInputModeOnboardingShown = false,
                        isFillBlanksSelectModeOnboardingShown = false
                    )
                )
            )

            assertEquals(expectedState, state)
            assertEquals(emptySet(), actions)
        }
    }

    @Test
    fun `When problems limit reached blocks learn solving`() {
        val step = Step.stub(id = 1)
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
            stepQuizHintsState = StepQuizHintsFeature.State.Idle
        )

        val reducer = StepQuizReducer(
            stepRoute = StepRoute.Learn.Step(step.id),
            stepQuizHintsReducer = StepQuizHintsReducer(StepRoute.Learn.Step(step.id))
        )
        val (state, actions) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Loading,
                stepQuizHintsState = StepQuizHintsFeature.State.Idle
            ),
            StepQuizFeature.Message.FetchAttemptSuccess(
                step,
                attempt,
                submissionState,
                isProblemsLimitReached = true,
                problemsLimitReachedModalData = StepQuizFeature.ProblemsLimitReachedModalData(
                    title = "",
                    description = "",
                    unlockLimitsButtonText = null
                ),
                problemsOnboardingFlags = ProblemsOnboardingFlags(
                    isParsonsOnboardingShown = false,
                    isFillBlanksInputModeOnboardingShown = false,
                    isFillBlanksSelectModeOnboardingShown = false
                )
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
        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Empty(),
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle
        )

        val reducer = StepQuizReducer(
            stepRoute = StepRoute.Learn.Step(step.id),
            stepQuizHintsReducer = StepQuizHintsReducer(StepRoute.Learn.Step(step.id))
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
            stepQuizHintsState = StepQuizHintsFeature.State.Idle
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
        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Empty(),
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle
        )

        val reducer = StepQuizReducer(
            stepRoute = StepRoute.LearnDaily(step.id),
            stepQuizHintsReducer = StepQuizHintsReducer(StepRoute.LearnDaily(step.id))
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
            stepQuizHintsState = StepQuizHintsFeature.State.Idle
        )

        assertEquals(expectedState, actualState)
        assertTrue(actualActions.isEmpty())
    }

    @Test
    fun `When updated problems limits reached for step with limited attempts blocks solving`() {
        val step = Step.stub(id = 1)
        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Empty(),
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle
        )

        val reducer = StepQuizReducer(
            stepRoute = StepRoute.Learn.Step(step.id),
            stepQuizHintsReducer = StepQuizHintsReducer(StepRoute.Learn.Step(step.id))
        )

        val (actualState, actualActions) = reducer.reduce(
            initialState,
            StepQuizFeature.InternalMessage.UpdateProblemsLimitResult(
                isProblemsLimitReached = true,
                problemsLimitReachedModalData = StepQuizFeature.ProblemsLimitReachedModalData(
                    title = "",
                    description = "",
                    unlockLimitsButtonText = null
                )
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
            stepQuizHintsState = StepQuizHintsFeature.State.Idle
        )

        assertEquals(expectedState, actualState)
        assertContains(
            actualActions,
            StepQuizFeature.Action.ViewAction.ShowProblemsLimitReachedModal(
                StepQuizFeature.ProblemsLimitReachedModalData(
                    title = "",
                    description = "",
                    unlockLimitsButtonText = null
                )
            )
        )
    }

    @Test
    fun `When updated problems limits reached for step without limited attempts not blocks solving`() {
        val step = Step.stub(id = 1)
        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                step = step,
                attempt = Attempt.stub(),
                submissionState = StepQuizFeature.SubmissionState.Empty(),
                isProblemsLimitReached = false,
                isTheoryAvailable = false
            ),
            stepQuizHintsState = StepQuizHintsFeature.State.Idle
        )

        val reducer = StepQuizReducer(
            stepRoute = StepRoute.LearnDaily(step.id),
            stepQuizHintsReducer = StepQuizHintsReducer(StepRoute.LearnDaily(step.id))
        )

        val (actualState, actualActions) = reducer.reduce(
            initialState,
            StepQuizFeature.InternalMessage.UpdateProblemsLimitResult(
                isProblemsLimitReached = true,
                problemsLimitReachedModalData = StepQuizFeature.ProblemsLimitReachedModalData(
                    title = "",
                    description = "",
                    unlockLimitsButtonText = null
                )
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
            stepQuizHintsState = StepQuizHintsFeature.State.Idle
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
            stepQuizHintsState = StepQuizHintsFeature.State.Idle
        )

        val reducer = StepQuizReducer(
            stepRoute = StepRoute.Learn.Step(step.id),
            stepQuizHintsReducer = StepQuizHintsReducer(StepRoute.Learn.Step(step.id))
        )

        val (intermediateState, _) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Loading,
                stepQuizHintsState = StepQuizHintsFeature.State.Idle
            ),
            StepQuizFeature.Message.FetchAttemptSuccess(
                step,
                attempt,
                submissionState,
                isProblemsLimitReached = false,
                problemsLimitReachedModalData = null,
                problemsOnboardingFlags = ProblemsOnboardingFlags(
                    isParsonsOnboardingShown = false,
                    isFillBlanksInputModeOnboardingShown = false,
                    isFillBlanksSelectModeOnboardingShown = false
                )
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
            val analyticActions = finalActions.filterIsInstance<StepQuizFeature.Action.LogAnalyticEvent>()
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
            stepQuizHintsState = StepQuizHintsFeature.State.Idle
        )

        val reducer = StepQuizReducer(
            stepRoute = StepRoute.LearnDaily(step.id),
            stepQuizHintsReducer = StepQuizHintsReducer(StepRoute.Learn.Step(step.id))
        )

        val (intermediateState, _) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Loading,
                stepQuizHintsState = StepQuizHintsFeature.State.Idle
            ),
            StepQuizFeature.Message.FetchAttemptSuccess(
                step,
                attempt,
                submissionState,
                isProblemsLimitReached = false,
                problemsLimitReachedModalData = null,
                problemsOnboardingFlags = ProblemsOnboardingFlags(
                    isParsonsOnboardingShown = false,
                    isFillBlanksInputModeOnboardingShown = false,
                    isFillBlanksSelectModeOnboardingShown = false
                )
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