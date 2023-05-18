package org.hyperskill.step_quiz

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
import org.junit.Test

class AndroidStepQuizTest {
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
}