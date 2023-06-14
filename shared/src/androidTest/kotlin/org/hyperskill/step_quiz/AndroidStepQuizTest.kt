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
                    StepRoute.Learn::class -> StepRoute.Learn.Step(step.id)
                    StepRoute.LearnDaily::class -> StepRoute.LearnDaily(step.id)
                    StepRoute.Repeat::class -> StepRoute.Repeat.Practice(step.id)
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
    fun `Theory should be available for Learn and Repeat StepRoutes`() {
        val step = Step.stub(id = 1, topicTheory = 2)
        val attempt = Attempt.stub()
        val submissionState = StepQuizFeature.SubmissionState.Empty()

        StepRoute::class.sealedSubclasses.forEach { stepRouteClass ->
            stepRouteClass.sealedSubclasses.ifEmpty {
                listOf(stepRouteClass)
            }.forEach { concreteStepRouteClass ->
                val expectedState = StepQuizFeature.State(
                    stepQuizState = StepQuizFeature.StepQuizState.AttemptLoaded(
                        step = step,
                        attempt = attempt,
                        submissionState = submissionState,
                        isProblemsLimitReached = false,
                        isTheoryAvailable = when (concreteStepRouteClass) {
                            StepRoute.Learn.Step::class -> true
                            StepRoute.Learn.TheoryOpenedFromPractice::class -> false
                            StepRoute.LearnDaily::class -> false
                            StepRoute.Repeat.Practice::class -> true
                            StepRoute.Repeat.Theory::class -> false
                            StepRoute.StageImplement::class -> false
                            else -> throw IllegalStateException(
                                "Unknown step route class: $concreteStepRouteClass. Please add it to the test."
                            )
                        }
                    ),
                    problemsLimitState = ProblemsLimitFeature.State.Idle
                )

                val reducer = StepQuizReducer(
                    when (concreteStepRouteClass) {
                        StepRoute.Learn.Step::class -> StepRoute.Learn.Step(step.id)
                        StepRoute.Learn.TheoryOpenedFromPractice::class ->
                            StepRoute.Learn.TheoryOpenedFromPractice(step.id)
                        StepRoute.LearnDaily::class -> StepRoute.LearnDaily(step.id)
                        StepRoute.Repeat.Practice::class -> StepRoute.Repeat.Practice(step.id)
                        StepRoute.Repeat.Theory::class -> StepRoute.Repeat.Theory(step.id)
                        StepRoute.StageImplement::class -> StepRoute.StageImplement(step.id, 1, 1)
                        else -> throw IllegalStateException(
                            "Unknown step route class: $concreteStepRouteClass. Please add it to the test."
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
}