package org.hyperskill.step_quiz

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz.domain.model.attempts.Attempt
import org.hyperskill.app.step_quiz.presentation.StepQuizFeature
import org.hyperskill.app.step_quiz.presentation.StepQuizResolver
import org.hyperskill.step.domain.model.stub
import org.hyperskill.step_quiz.domain.model.stub

class StepQuizResolverTest {
    // This is a marker test function, to not forget to add new cases to tests
    @Suppress("UNUSED_EXPRESSION")
    private fun marker(
        stepRoute: StepRoute,
        stepQuizState: StepQuizFeature.StepQuizState
    ) {
        when (stepRoute) {
            is StepRoute.Learn -> true
            is StepRoute.LearnDaily -> true
            is StepRoute.Repeat -> true
            is StepRoute.StageImplement -> true
        }
        when (stepQuizState) {
            is StepQuizFeature.StepQuizState.AttemptLoaded -> true
            is StepQuizFeature.StepQuizState.AttemptLoading -> true
            StepQuizFeature.StepQuizState.Idle -> true
            StepQuizFeature.StepQuizState.Loading -> true
            StepQuizFeature.StepQuizState.NetworkError -> true
            StepQuizFeature.StepQuizState.Unsupported -> true
        }
    }

    @Test
    fun `Theory toolbar item available based on StepQuizState`() {
        val attemptLoadedState = StepQuizFeature.StepQuizState.AttemptLoaded(
            step = Step.stub(id = 1),
            attempt = Attempt.stub(),
            submissionState = StepQuizFeature.SubmissionState.Empty(),
            isProblemsLimitReached = false,
            isTheoryAvailable = true
        )

        listOf(
            attemptLoadedState,
            StepQuizFeature.StepQuizState.AttemptLoading(oldState = attemptLoadedState),
        ).forEach {
            assertEquals(true, StepQuizResolver.isTheoryToolbarItemAvailable(it))
        }
    }

    @Test
    fun `Theory toolbar item unavailable based on StepQuizState`() {
        val attemptLoadedState = StepQuizFeature.StepQuizState.AttemptLoaded(
            step = Step.stub(id = 1),
            attempt = Attempt.stub(),
            submissionState = StepQuizFeature.SubmissionState.Empty(),
            isProblemsLimitReached = false,
            isTheoryAvailable = false
        )

        listOf(
            attemptLoadedState,
            StepQuizFeature.StepQuizState.AttemptLoading(oldState = attemptLoadedState),
            StepQuizFeature.StepQuizState.Idle,
            StepQuizFeature.StepQuizState.Loading,
            StepQuizFeature.StepQuizState.NetworkError,
            StepQuizFeature.StepQuizState.Unsupported
        ).forEach {
            assertEquals(false, StepQuizResolver.isTheoryToolbarItemAvailable(it))
        }
    }

    @Test
    fun `Theory toolbar item available based on route and step`() {
        val step = Step.stub(id = 1, topicTheory = 2)

        listOf(
            StepRoute.Learn(step.id),
            StepRoute.Repeat(step.id)
        ).forEach {
            assertEquals(true, StepQuizResolver.isTheoryToolbarItemAvailable(it, step))
        }
    }

    @Test
    fun `Theory toolbar item unavailable based on route and step`() {
        val step = Step.stub(id = 1, topicTheory = null)

        listOf(
            StepRoute.Learn(step.id),
            StepRoute.Repeat(step.id),
            StepRoute.LearnDaily(step.id),
            StepRoute.StageImplement(stepId = step.id, projectId = 2, stageId = 3)
        ).forEach { stepRoute ->
            assertEquals(
                false,
                StepQuizResolver.isTheoryToolbarItemAvailable(stepRoute, step)
            )
        }
    }
}