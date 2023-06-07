package org.hyperskill.step_completion

import kotlin.test.Test
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import org.hyperskill.app.step_completion.presentation.StepCompletionReducer
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
                StepCompletionFeature.Message.StartPracticingClicked
            )
            assertTrue {
                actions.contains(StepCompletionFeature.Action.ViewAction.NavigateTo.Back)
                actions.none { it is StepCompletionFeature.Action.FetchNextRecommendedStep }
            }
        }
    }
}