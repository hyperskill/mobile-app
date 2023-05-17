package org.hyperskill.step_quiz

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.problems_limit.domain.model.ProblemsLimitScreen
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.analytic.StepQuizClickedTheoryToolbarHyperskillAnalyticEvent
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizReducer
import org.hyperskill.step.domain.model.stub
import org.hyperskill.step_quiz.domain.model.stub

class StepQuizTest {
    @Test
    fun `When problems limit reached not blocks problem of the day and repeat solving`() {
        val step = Step.stub(id = 1)
        val attempt = Attempt.stub(step = step.id)
        val submissionState = StepQuizFeature.SubmissionState.Empty()
        val stepRoutes = listOf(StepRoute.LearnDaily(step.id), StepRoute.Repeat(step.id))

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
        val attempt = Attempt.stub(step = step.id)
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

        val reducer = StepQuizReducer(StepRoute.Learn(step.id), ProblemsLimitReducer(ProblemsLimitScreen.STEP_QUIZ))
        val (state, actions) = reducer.reduce(
            StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Loading,
                problemsLimitState = ProblemsLimitFeature.State.Idle
            ),
            StepQuizFeature.Message.FetchAttemptSuccess(
                step,
                attempt,
                submissionState,
                isProblemsLimitReached = true
            )
        )

        assertEquals(expectedState, state)
        assertContains(actions, StepQuizFeature.Action.ViewAction.ShowProblemsLimitReachedModal)
    }

    @Test
    fun `Problems limit state initialized only for Learn StepRoute`() {
        val step = Step.stub(id = 1)

        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.Idle,
            problemsLimitState = ProblemsLimitFeature.State.Idle
        )

        StepRoute::class.sealedSubclasses.forEach { stepRouteClass ->
            val expectedProblemsLimitState = when (stepRouteClass) {
                StepRoute.Learn::class -> ProblemsLimitFeature.State.Loading
                else -> ProblemsLimitFeature.State.Idle
            }

            val expectedState = StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.Loading,
                problemsLimitState = expectedProblemsLimitState
            )

            val reducer = StepQuizReducer(
                stepRoute = when (stepRouteClass) {
                    StepRoute.Learn::class -> StepRoute.Learn(step.id)
                    StepRoute.LearnDaily::class -> StepRoute.LearnDaily(step.id)
                    StepRoute.Repeat::class -> StepRoute.Repeat(step.id)
                    StepRoute.StageImplement::class -> StepRoute.StageImplement(step.id, 1, 1)
                    else -> throw IllegalStateException(
                        "Unknown step route class: $stepRouteClass. Please add it to the test."
                    )
                },
                problemsLimitReducer = ProblemsLimitReducer(ProblemsLimitScreen.STEP_QUIZ)
            )

            val (state, _) = reducer.reduce(
                initialState,
                StepQuizFeature.Message.InitWithStep(step)
            )

            assertEquals(expectedState, state)
        }
    }

    @Test
    fun `Theory availability`() {
        val step = Step.stub(id = 1, topicTheory = 2)
        val attempt = Attempt.stub(step = step.id)
        val submissionState = StepQuizFeature.SubmissionState.Empty()

        StepRoute::class.sealedSubclasses.forEach { stepRouteClass ->
            val expectedState = StepQuizFeature.State(
                stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                    step = step,
                    attempt = attempt,
                    submissionState = submissionState,
                    isProblemsLimitReached = false,
                    isTheoryAvailable = when (stepRouteClass) {
                        StepRoute.Learn::class -> true
                        StepRoute.LearnDaily::class -> false
                        StepRoute.Repeat::class -> true
                        StepRoute.StageImplement::class -> false
                        else -> throw IllegalStateException(
                            "Unknown step route class: $stepRouteClass. Please add it to the test."
                        )
                    },
                ),
                problemsLimitState = ProblemsLimitFeature.State.Idle
            )

            val reducer = StepQuizReducer(
                when (stepRouteClass) {
                    StepRoute.Learn::class -> StepRoute.Learn(step.id)
                    StepRoute.LearnDaily::class -> StepRoute.LearnDaily(step.id)
                    StepRoute.Repeat::class -> StepRoute.Repeat(step.id)
                    StepRoute.StageImplement::class -> StepRoute.StageImplement(step.id, 1, 1)
                    else -> throw IllegalStateException(
                        "Unknown step route class: $stepRouteClass. Please add it to the test."
                    )
                },
                ProblemsLimitReducer(ProblemsLimitScreen.STEP_QUIZ)
            )

            val (state, _) = reducer.reduce(
                StepQuizFeature.State(
                    stepQuizState = StepQuizFeature.StepQuizState.Loading,
                    problemsLimitState = ProblemsLimitFeature.State.Idle
                ),
                StepQuizFeature.Message.FetchAttemptSuccess(
                    step,
                    attempt,
                    submissionState,
                    isProblemsLimitReached = false
                )
            )

            assertEquals(expectedState, state)
        }
    }

    @Test
    fun `TheoryToolbarClicked message navigates to step screen when theory available`() {
        val topicTheoryId = 2L
        val step = Step.stub(id = 1, topicTheory = topicTheoryId)
        val attempt = Attempt.stub(step = step.id)
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
            StepRoute.Learn(step.id),
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
                isProblemsLimitReached = false
            )
        )

        val (finalState, finalActions) = reducer.reduce(
            intermediateState,
            StepQuizFeature.Message.TheoryToolbarClicked
        )

        assertEquals(expectedState, finalState)
        assertTrue {
            finalActions.any {
                it is StepQuizFeature.Action.ViewAction.NavigateTo.StepScreen &&
                    it.stepRoute is StepRoute.Learn &&
                    it.stepRoute.stepId == topicTheoryId
            }
        }
        assertTrue {
            val analyticActions = finalActions.filterIsInstance<StepQuizFeature.Action.LogAnalyticEvent>()
            analyticActions.any {
                (it.analyticEvent as StepQuizClickedTheoryToolbarHyperskillAnalyticEvent).topicTheoryId == topicTheoryId
            }
        }
    }

    @Test
    fun `TheoryToolbarClicked message not navigates to step screen when theory unavailable`() {
        val step = Step.stub(id = 1, topicTheory = 2)
        val attempt = Attempt.stub(step = step.id)
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
                isProblemsLimitReached = false
            )
        )

        val (finalState, finalActions) = reducer.reduce(
            intermediateState,
            StepQuizFeature.Message.TheoryToolbarClicked
        )

        assertEquals(expectedState, finalState)
        assertTrue {
            finalActions.none {
                it is StepQuizFeature.Action.ViewAction.NavigateTo.StepScreen
            }
        }
    }
}