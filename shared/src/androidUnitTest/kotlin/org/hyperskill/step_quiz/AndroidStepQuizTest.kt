package org.hyperskill.step_quiz

import kotlin.test.assertEquals
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
                    )
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
                    }
                )

                val (state, _) = reducer.reduce(
                    StepQuizFeature.State(
                        stepQuizState = StepQuizFeature.StepQuizState.Loading
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

                assertEquals(expectedState, state)
            }
        }
    }
}