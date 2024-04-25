package org.hyperskill.step_quiz_toolbar

import kotlin.test.Test
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.InternalAction
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.InternalMessage
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarFeature.State
import org.hyperskill.app.step_quiz_toolbar.presentation.StepQuizToolbarReducer

class StepQuizToolbarTest {
    @Test
    fun `Initialization in Idle state transitions to Loading if toolbar is available`() {
        val initialState = State.Idle
        val (state, actions) = reducer().reduce(initialState, InternalMessage.Initialize)

        assertTrue(state is State.Loading)
        assertTrue(actions.contains(InternalAction.FetchSubscription))
    }

    @Test
    fun `Initialization in Idle state transitions to Unavailable if toolbar is unavailable`() {
        val stepRoute = StepRoute.LearnDaily(stepId = 1L)
        val initialState = State.Idle

        val (state, actions) = reducer(stepRoute).reduce(initialState, InternalMessage.Initialize)

        assertTrue(state is State.Unavailable)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `Initialization in Unavailable state remains Unavailable`() {
        val initialState = State.Unavailable
        val (state, actions) = reducer().reduce(initialState, InternalMessage.Initialize)

        assertTrue(state is State.Unavailable)
        assertTrue(actions.isEmpty())
    }

    private fun reducer(stepRoute: StepRoute = StepRoute.Learn.Step(stepId = 1L, topicId = null)) =
        StepQuizToolbarReducer(stepRoute)
}