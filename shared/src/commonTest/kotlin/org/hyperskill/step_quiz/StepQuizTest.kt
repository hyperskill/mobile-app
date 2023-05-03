package org.hyperskill.step_quiz

import kotlin.test.Test
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
    fun `When problems limit reached not blocks problem of the day solving`() {
        val step = Step.stub(id = 1)
        val attempt = Attempt.stub(step = step.id)
        val submissionState = StepQuizFeature.SubmissionState.Empty()

        val expectedState =
            StepQuizFeature.State.AttemptLoaded(step, attempt, submissionState, isProblemsLimitReached = false)

        val reducer = StepQuizReducer(StepRoute.LearnDaily(step.id))
        val message =
            StepQuizFeature.Message.FetchAttemptSuccess(step, attempt, submissionState, isProblemsLimitReached = true)
        val (state, actions) = reducer.reduce(StepQuizFeature.State.Loading, message)

        assertEquals(expectedState, state)
        assertEquals(emptySet(), actions)
    }
}