package org.hyperskill.step_completion

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.Step
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Action
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.InternalAction
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.InternalMessage
import org.hyperskill.app.step_completion.presentation.StepCompletionFeature.Message
import org.hyperskill.app.step_completion.presentation.StepCompletionReducer
import org.hyperskill.app.subscriptions.domain.model.FreemiumChargeLimitsStrategy
import org.hyperskill.app.topics.domain.model.Topic
import org.hyperskill.step.domain.model.stub

class StepCompletionTest {
    @Test
    fun `Start practicing navigate to back if theory is opened from toolbar`() {
        val stepId = 1L
        val toolbarStepRoutes = listOf(
            StepRoute.Learn.TheoryOpenedFromPractice(stepId, null),
            StepRoute.Repeat.Theory(stepId)
        )

        toolbarStepRoutes.forEach { stepRoute ->
            val reducer = StepCompletionReducer(stepRoute)
            val (_, actions) = reducer.reduce(
                StepCompletionFeature.createState(Step.stub(stepId), stepRoute),
                Message.StartPracticingClicked
            )
            assertTrue {
                actions.contains(Action.ViewAction.NavigateTo.Back)
                actions.none { it is InternalAction.FetchNextRecommendedStep }
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
            InternalMessage.StepSolved(2L)
        )

        assertEquals(initialState, actualState)
        assertTrue(actualActions.isEmpty())
    }

    @Test
    fun `Solved step with limited attempts updates problems limits`() {
        val stepRoute = StepRoute.Learn.Step(1L, null)
        val initialState = StepCompletionFeature.createState(Step.stub(stepRoute.stepId), stepRoute)

        val reducer = StepCompletionReducer(stepRoute)
        val (actualState, actualActions) = reducer.reduce(
            initialState,
            InternalMessage.StepSolved(stepRoute.stepId)
        )

        assertEquals(initialState, actualState)
        assertContains(
            actualActions,
            InternalAction.UpdateProblemsLimit(FreemiumChargeLimitsStrategy.AFTER_CORRECT_SUBMISSION)
        )
    }

    @Test
    fun `Solved step without limited attempts not updates problems limits`() {
        val stepRoute = StepRoute.LearnDaily(1L)
        val initialState = StepCompletionFeature.createState(Step.stub(stepRoute.stepId), stepRoute)

        val reducer = StepCompletionReducer(stepRoute)
        val (actualState, actualActions) = reducer.reduce(
            initialState,
            InternalMessage.StepSolved(stepRoute.stepId)
        )

        assertEquals(initialState, actualState)
        assertTrue(actualActions.isEmpty())
    }

    @Test
    fun `HapticFeedbackTopicCompleted triggers when topic is completed`() {
        val stepRoute = StepRoute.Learn.Step(1L, null)
        val initialState = StepCompletionFeature.createState(Step.stub(stepRoute.stepId), stepRoute)

        val reducer = StepCompletionReducer(stepRoute)
        val (_, actions) = reducer.reduce(
            initialState,
            Message.CheckTopicCompletionStatus.Completed(
                topic = Topic(id = 1, progressId = "", title = ""),
                passedTopicsCount = 5,
                nextLearningActivity = null,
                isTopicsLimitReached = false
            )
        )

        assertContains(actions, Action.ViewAction.HapticFeedback.TopicCompleted)
    }
}