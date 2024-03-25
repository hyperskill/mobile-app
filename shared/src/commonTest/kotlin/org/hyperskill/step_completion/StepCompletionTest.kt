package org.hyperskill.step_completion

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import org.hyperskill.app.step_completion.presentation.StepCompletionReducer
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.step.domain.model.stub

class StepCompletionTest {
    @Test
    fun `Start practicing navigate to back if theory is opened from toolbar`() {
        val stepId = 1L
        val toolbarStepRoutes = listOf(
            StepRoute.Learn.TheoryOpenedFromPractice(stepId),
            StepRoute.Repeat.Theory(stepId)
        )

        toolbarStepRoutes.forEach { stepRoute ->
            val reducer = StepCompletionReducer(stepRoute)
            val (_, actions) = reducer.reduce(
                StepCompletionFeature.createState(Step.stub(stepId), stepRoute),
                StepCompletionFeature.Message.StartPracticingClicked(isLocatedAtBeginning = true)
            )
            assertTrue {
                actions.contains(StepCompletionFeature.Action.ViewAction.NavigateTo.Back)
                actions.none { it is StepCompletionFeature.Action.FetchNextRecommendedStep }
            }
        }
    }

    @Test
    fun `Not current step solved do nothing`() {
        val stepRoute = StepRoute.LearnDaily(1L)
        val initialState = StepCompletionFeature.createState(Step.stub(stepRoute.stepId), stepRoute)

        val reducer = StepCompletionReducer(stepRoute)
        val (actualState, actualActions) = reducer.reduce(
            initialState,
            StepCompletionFeature.Message.StepSolved(2L)
        )

        assertEquals(initialState, actualState)
        assertTrue(actualActions.isEmpty())
    }

    @Test
    fun `Solved step with limited attempts updates problems limits`() {
        val stepRoute = StepRoute.Learn.Step(1L)
        val initialState = StepCompletionFeature.createState(Step.stub(stepRoute.stepId), stepRoute)

        val reducer = StepCompletionReducer(stepRoute)
        val (actualState, actualActions) = reducer.reduce(
            initialState,
            StepCompletionFeature.Message.StepSolved(stepRoute.stepId)
        )

        assertEquals(initialState, actualState)
        assertContains(
            actualActions,
            StepCompletionFeature.Action.UpdateProblemsLimit(FreemiumChargeLimitsStrategy.AFTER_CORRECT_SUBMISSION)
        )
    }

    @Test
    fun `Solved step without limited attempts not updates problems limits`() {
        val stepRoute = StepRoute.LearnDaily(1L)
        val initialState = StepCompletionFeature.createState(Step.stub(stepRoute.stepId), stepRoute)

        val reducer = StepCompletionReducer(stepRoute)
        val (actualState, actualActions) = reducer.reduce(
            initialState,
            StepCompletionFeature.Message.StepSolved(stepRoute.stepId)
        )

        assertEquals(initialState, actualState)
        assertTrue(actualActions.isEmpty())
    }
}