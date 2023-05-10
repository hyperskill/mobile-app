package org.hyperskill.step_quiz

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import org.hyperskill.app.problems_limit.domain.model.ProblemsLimitScreen
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitFeature
import org.hyperskill.app.problems_limit.presentation.ProblemsLimitReducer
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
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
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(step, attempt, submissionState, isProblemsLimitReached = false),
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
            stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(step, attempt, submissionState, isProblemsLimitReached = true),
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
        val initialState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.Idle,
            problemsLimitState = ProblemsLimitFeature.State.Idle
        )
        val problemsLimitInitializedState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.Loading,
            problemsLimitState = ProblemsLimitFeature.State.Loading
        )
        val problemsLimitIdleState = StepQuizFeature.State(
            stepQuizState = StepQuizFeature.StepQuizState.Loading,
            problemsLimitState = ProblemsLimitFeature.State.Idle
        )
        // TODO: implement test
//        StepRoute::class.sealedSubclasses.forEach { stateClass ->

//        }


//        val reducer = StepQuizReducer(StepRoute.Learn(step.id), ProblemsLimitReducer(ProblemsLimitScreen.STEP_QUIZ))
//        val (state, actions) = reducer.reduce(
//            StepQuizFeature.State(
//                stepQuizState = StepQuizFeature.StepQuizState.Loading,
//                problemsLimitState = ProblemsLimitFeature.State.Idle
//            ),
//            StepQuizFeature.Message.FetchAttemptSuccess(
//                step,
//                attempt,
//                submissionState,
//                isProblemsLimitReached = true
//            )
//        )
//
//        assertEquals(problemsLimitInitializedState, state)
//        assertContains(actions, StepQuizFeature.Action.ViewAction.ShowProblemsLimitReachedModal)
    }
}