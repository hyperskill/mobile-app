package org.hyperskill.step_quiz

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.problems_limit.domain.model.ProblemsLimitScreen
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedTheoryToolbarItemHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizReducer
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
            problemsLimitState = ProblemsLimitFeature.State.Idle
        )

        stepRoutes.forEach { stepRoute ->
            val reducer = StepQuizReducer(stepRoute, ProblemsLimitReducer(ProblemsLimitScreen.STEP_QUIZ))
            val (state, actions) = reducer.reduce(
                StepQuizFeature.State(
                    stepQuizState = StepQuizFeature.StepQuizState.Loading,
                    problemsLimitState = ProblemsLimitFeature.State.Idle
                ),
                StepQuizFeature.Message.FetchAttemptSuccess(
                    step,
                    attempt,
                    submissionState,
                    isProblemsLimitReached = true,
                    problemsLimitReachedModalText = "",
                    isParsonsOnboardingShown = false
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
            problemsLimitState = ProblemsLimitFeature.State.Idle
        )

        val reducer = StepQuizReducer(
            stepRoute = StepRoute.Learn.Step(step.id),
            problemsLimitReducer = ProblemsLimitReducer(ProblemsLimitScreen.STEP_QUIZ)
        )
        val (state, actions) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Loading,
                problemsLimitState = ProblemsLimitFeature.State.Idle
            ),
            StepQuizFeature.Message.FetchAttemptSuccess(
                step,
                attempt,
                submissionState,
                isProblemsLimitReached = true,
                problemsLimitReachedModalText = "",
                isParsonsOnboardingShown = false
            )
        )

        assertEquals(expectedState, state)
        assertTrue {
            actions.any { it is StepQuizFeature.Action.ViewAction.ShowProblemsLimitReachedModal }
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
            problemsLimitState = ProblemsLimitFeature.State.Idle
        )

        val reducer = StepQuizReducer(
            StepRoute.Learn.Step(step.id),
            ProblemsLimitReducer(ProblemsLimitScreen.STEP_QUIZ)
        )

        val (intermediateState, _) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Loading,
                problemsLimitState = ProblemsLimitFeature.State.Idle
            ),
            StepQuizFeature.Message.FetchAttemptSuccess(
                step,
                attempt,
                submissionState,
                isProblemsLimitReached = false,
                problemsLimitReachedModalText = null,
                isParsonsOnboardingShown = false
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
            problemsLimitState = ProblemsLimitFeature.State.Idle
        )

        val reducer = StepQuizReducer(
            StepRoute.LearnDaily(step.id),
            ProblemsLimitReducer(ProblemsLimitScreen.STEP_QUIZ)
        )

        val (intermediateState, _) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Loading,
                problemsLimitState = ProblemsLimitFeature.State.Idle
            ),
            StepQuizFeature.Message.FetchAttemptSuccess(
                step,
                attempt,
                submissionState,
                isProblemsLimitReached = false,
                problemsLimitReachedModalText = null,
                isParsonsOnboardingShown = false
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