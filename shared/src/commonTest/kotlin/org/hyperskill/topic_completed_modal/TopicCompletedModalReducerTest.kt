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
import org.hyperskill.app.topic_completed_modal.domain.model.TopicCompletedModalFeatureParams.ContinueBehaviour
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
        val initialState = createInitialState(continueBehaviour = ContinueBehaviour.CONTINUE_WITH_NEXT_TOPIC)
        val message = Message.CloseButtonClicked

        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertContains(actions, Action.ViewAction.Dismiss)
        assertTrue {
            actions.any {
                it is InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is TopicCompletedModalClickedCloseHyperskillAnalyticEvent &&
                    it.analyticEvent.route == analyticRoute
            }
        }
    }

    @Test
    fun `CallToActionButtonClicked triggers appropriate navigation and logging when can continue with next topic`() {
        val initialState = createInitialState(continueBehaviour = ContinueBehaviour.CONTINUE_WITH_NEXT_TOPIC)
        val message = Message.CallToActionButtonClicked

        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertContains(actions, Action.ViewAction.NavigateTo.NextTopic)
        assertTrue {
            actions.any {
                it is InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is TopicCompletedModalClickedContinueNextTopicHyperskillAnalyticEvent &&
                    it.analyticEvent.route == analyticRoute
            }
        }
    }

    @Test
    fun `CallToActionButtonClicked triggers appropriate navigation and logging when cannot continue with next topic`() {
        val initialState = createInitialState(continueBehaviour = ContinueBehaviour.GO_TO_STUDY_PLAN)
        val message = Message.CallToActionButtonClicked

        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertContains(actions, Action.ViewAction.NavigateTo.StudyPlan)
        assertTrue {
            actions.any {
                it is InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is TopicCompletedModalClickedGoToStudyPlanHyperskillAnalyticEvent &&
                    it.analyticEvent.route == analyticRoute
            }
        }
    }

    @Test
    fun `ShownEventMessage triggers LogAnalyticEvent with shown event`() {
        val initialState = createInitialState(continueBehaviour = ContinueBehaviour.CONTINUE_WITH_NEXT_TOPIC)
        val message = Message.ShownEventMessage

        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertTrue {
            actions.any {
                it is InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is TopicCompletedModalShownHyperskillAnalyticEvent &&
                    it.analyticEvent.route == analyticRoute
            }
        }
    }

    @Test
    fun `HiddenEventMessage triggers LogAnalyticEvent with hidden event`() {
        val initialState = createInitialState(continueBehaviour = ContinueBehaviour.CONTINUE_WITH_NEXT_TOPIC)
        val message = Message.HiddenEventMessage

        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertTrue {
            actions.any {
                it is InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is TopicCompletedModalHiddenHyperskillAnalyticEvent &&
                    it.analyticEvent.route == analyticRoute
            }
        }
    }

    @Test
    fun `UserDidTakeScreenshotEventMessage triggers LogAnalyticEvent with screenshot event`() {
        val initialState = createInitialState(continueBehaviour = ContinueBehaviour.CONTINUE_WITH_NEXT_TOPIC)
        val message = Message.UserDidTakeScreenshotEventMessage

        val (state, actions) = reducer.reduce(initialState, message)

        assertEquals(initialState, state)
        assertTrue {
            actions.any {
                it is InternalAction.LogAnalyticEvent &&
                    it.analyticEvent is TopicCompletedModalUserDidTakeScreenshotHyperskillAnalyticEvent &&
                    it.analyticEvent.route == analyticRoute
            }
        }
    }

    private fun createInitialState(continueBehaviour: ContinueBehaviour): State =
        State(
            topic = Topic(id = 1, title = "Sample Topic", progressId = ""),
            passedTopicsCount = 5,
            continueBehaviour = continueBehaviour
        )
}