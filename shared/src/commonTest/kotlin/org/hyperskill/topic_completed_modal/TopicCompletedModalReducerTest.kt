package org.hyperskill.topic_completed_modal

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.hyperskill.app.step.domain.model.StepRoute
import org.hyperskill.app.topic_completed_modal.domain.analytic.TopicCompletedModalClickedCloseHyperskillAnalyticEvent
import org.hyperskill.app.topic_completed_modal.domain.analytic.TopicCompletedModalClickedContinueNextTopicHyperskillAnalyticEvent
import org.hyperskill.app.topic_completed_modal.domain.analytic.TopicCompletedModalClickedGoToStudyPlanHyperskillAnalyticEvent
import org.hyperskill.app.topic_completed_modal.domain.analytic.TopicCompletedModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.topic_completed_modal.domain.analytic.TopicCompletedModalShownHyperskillAnalyticEvent
import org.hyperskill.app.topic_completed_modal.domain.analytic.TopicCompletedModalUserDidTakeScreenshotHyperskillAnalyticEvent
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.Action
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.InternalAction
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.Message
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.State
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalReducer
import org.hyperskill.app.topics.domain.model.Topic

class TopicCompletedModalReducerTest {
    private val analyticRoute = StepRoute.Learn.Step(stepId = 1, topicId = null).analyticRoute
    private val reducer = TopicCompletedModalReducer(analyticRoute)

    @Test
    fun `CloseButtonClicked triggers LogAnalyticEvent and Dismiss action`() {
        val initialState = createInitialState(canContinueWithNextTopic = true)
        val message = Message.CloseButtonClicked

        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertContains(actions, Action.ViewAction.Dismiss)
        assertTrue {
            actions.any {
                it is InternalAction.LogAnalyticEvent &&
                    it.event is TopicCompletedModalClickedCloseHyperskillAnalyticEvent &&
                    it.event.route == analyticRoute
            }
        }
    }

    @Test
    fun `CallToActionButtonClicked triggers appropriate navigation and logging when can continue with next topic`() {
        val initialState = createInitialState(canContinueWithNextTopic = true)
        val message = Message.CallToActionButtonClicked

        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertContains(actions, Action.ViewAction.NavigateTo.NextTopic)
        assertTrue {
            actions.any {
                it is InternalAction.LogAnalyticEvent &&
                    it.event is TopicCompletedModalClickedContinueNextTopicHyperskillAnalyticEvent &&
                    it.event.route == analyticRoute
            }
        }
    }

    @Test
    fun `CallToActionButtonClicked triggers appropriate navigation and logging when cannot continue with next topic`() {
        val initialState = createInitialState(canContinueWithNextTopic = false)
        val message = Message.CallToActionButtonClicked

        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertContains(actions, Action.ViewAction.NavigateTo.StudyPlan)
        assertTrue {
            actions.any {
                it is InternalAction.LogAnalyticEvent &&
                    it.event is TopicCompletedModalClickedGoToStudyPlanHyperskillAnalyticEvent &&
                    it.event.route == analyticRoute
            }
        }
    }

    @Test
    fun `ShownEventMessage triggers LogAnalyticEvent with shown event`() {
        val initialState = createInitialState(canContinueWithNextTopic = true)
        val message = Message.ShownEventMessage

        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertTrue {
            actions.any {
                it is InternalAction.LogAnalyticEvent &&
                    it.event is TopicCompletedModalShownHyperskillAnalyticEvent &&
                    it.event.route == analyticRoute
            }
        }
    }

    @Test
    fun `HiddenEventMessage triggers LogAnalyticEvent with hidden event`() {
        val initialState = createInitialState(canContinueWithNextTopic = true)
        val message = Message.HiddenEventMessage

        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertTrue {
            actions.any {
                it is InternalAction.LogAnalyticEvent &&
                    it.event is TopicCompletedModalHiddenHyperskillAnalyticEvent &&
                    it.event.route == analyticRoute
            }
        }
    }

    @Test
    fun `UserDidTakeScreenshotEventMessage triggers LogAnalyticEvent with screenshot event`() {
        val initialState = createInitialState(canContinueWithNextTopic = true)
        val message = Message.UserDidTakeScreenshotEventMessage

        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertTrue {
            actions.any {
                it is InternalAction.LogAnalyticEvent &&
                    it.event is TopicCompletedModalUserDidTakeScreenshotHyperskillAnalyticEvent &&
                    it.event.route == analyticRoute
            }
        }
    }

    private fun createInitialState(canContinueWithNextTopic: Boolean): State =
        State(
            topic = Topic(id = 1, title = "Sample Topic", progressId = ""),
            passedTopicsCount = 5,
            canContinueWithNextTopic = canContinueWithNextTopic
        )
}