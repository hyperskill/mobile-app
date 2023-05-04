package org.hyperskill.step_quiz

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
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

        val expectedState =
            StepQuizFeature.State.AttemptLoaded(step, attempt, submissionState, isProblemsLimitReached = false)

        stepRoutes.forEach { stepRoute ->
            val reducer = StepQuizReducer(stepRoute)
            val (state, actions) = reducer.reduce(
                StepQuizFeature.State.Loading,
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

        val expectedState =
            StepQuizFeature.State.AttemptLoaded(step, attempt, submissionState, isProblemsLimitReached = true)

        val reducer = StepQuizReducer(StepRoute.Learn(step.id))
        val (state, actions) = reducer.reduce(
            StepQuizFeature.State.Loading,
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
}