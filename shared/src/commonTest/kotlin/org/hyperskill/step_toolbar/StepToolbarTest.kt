package org.hyperskill.step_toolbar

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.InternalAction
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.InternalMessage
import org.hyperskill.app.step_toolbar.presentation.StepToolbarFeature.State
import org.hyperskill.app.step_toolbar.presentation.StepToolbarReducer
import org.hyperskill.app.topics.domain.model.TopicProgress

class StepToolbarTest {
    private val reducer = StepToolbarReducer(StepRoute.Learn.Step(stepId = 1L, topicId = null))

    @Test
    fun `Initialize in Idle state transitions to Loading when progress bar is available`() {
        val initialState = State.Idle
        val topicId = 123L
        val (state, actions) = reducer.reduce(
            initialState,
            InternalMessage.Initialize(topicId = topicId, forceUpdate = false)
        )

        assertTrue(state is State.Loading)
        assertContains(actions, InternalAction.FetchTopicProgress(topicId, false))
    }

    @Test
    fun `Initialize in Unavailable state remains Unavailable`() {
        val initialState = State.Unavailable
        val (state, actions) = reducer.reduce(
            initialState,
            InternalMessage.Initialize(topicId = 123L, forceUpdate = false)
        )

        assertTrue(state is State.Unavailable)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `Initialize with forceUpdate transitions to Loading from Content state`() {
        val topicId = 123L
        val initialState = State.Content(TopicProgress(vid = "", isCompleted = false, isSkipped = false))
        val (state, actions) = reducer.reduce(
            initialState,
            InternalMessage.Initialize(topicId = topicId, forceUpdate = true)
        )

        assertTrue(state is State.Loading)
        assertContains(actions, InternalAction.FetchTopicProgress(topicId, true))
    }

    @Test
    fun `Initialize without forceUpdate not transitions to Loading from Content state`() {
        val topicProgress = TopicProgress(vid = "", isCompleted = false, isSkipped = false)
        val initialState = State.Content(topicProgress)
        val topicId = 123L
        val (state, actions) = reducer.reduce(
            initialState,
            InternalMessage.Initialize(topicId = topicId, forceUpdate = false)
        )

        assertTrue(state is State.Content)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `Initialize in Error state transitions to Loading`() {
        val initialState = State.Error
        val topicId = 123L
        val (state, actions) = reducer.reduce(
            initialState,
            InternalMessage.Initialize(topicId = topicId, forceUpdate = true)
        )

        assertTrue(state is State.Loading)
        assertContains(actions, InternalAction.FetchTopicProgress(topicId, true))
    }

    @Test
    fun `Initialize does not transition if progressBar is not available`() {
        val initialState = State.Idle
        val stepRoute = StepRoute.LearnDaily(stepId = 1L, topicId = null)
        val reducer = StepToolbarReducer(stepRoute)
        val (state, actions) = reducer.reduce(
            initialState,
            InternalMessage.Initialize(topicId = 123L, forceUpdate = false)
        )

        assertTrue(state is State.Unavailable)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `Initialize without topicId results in no state transition or action`() {
        val initialState = State.Idle
        val (state, actions) = reducer.reduce(
            initialState,
            InternalMessage.Initialize(topicId = null, forceUpdate = false)
        )

        assertEquals(State.Idle, state)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `FetchTopicProgressError message should transition state to Error from Loading`() {
        val initialState = State.Loading
        val (state, actions) = reducer.reduce(initialState, InternalMessage.FetchTopicProgressError)

        assertTrue(state is State.Error)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `FetchTopicProgressSuccess message should update state to Content with topic progress`() {
        val initialState = State.Loading
        val topicProgress = TopicProgress(vid = "", isCompleted = false, isSkipped = false)
        val (state, actions) = reducer.reduce(
            initialState,
            InternalMessage.FetchTopicProgressSuccess(topicProgress)
        )

        assertTrue(state is State.Content)
        assertEquals(topicProgress, state.topicProgress)
        assertTrue(actions.isEmpty())
    }

    @Test
    fun `TopicCompleted message should update state to Content with completed status`() {
        val initialState = State.Content(TopicProgress(vid = "topic-1", isCompleted = false, isSkipped = false))
        val (state, actions) = reducer.reduce(
            initialState,
            InternalMessage.TopicCompleted(topicId = 1L)
        )

        assertTrue(state is State.Content)
        assertTrue(state.topicProgress.isCompleted)
        assertEquals(1.0f, state.topicProgress.capacity)
        assertTrue(actions.isEmpty())
    }
}
