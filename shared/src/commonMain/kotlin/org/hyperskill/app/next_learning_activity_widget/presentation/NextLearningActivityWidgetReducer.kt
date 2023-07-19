package org.hyperskill.app.next_learning_activity_widget.presentation

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.next_learning_activity_widget.domain.analytic.NextLearningActivityWidgetClickedHyperskillAnalyticEvent
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.Action
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.ContentState
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.InternalAction
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.InternalMessage
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.Message
import org.hyperskill.app.next_learning_activity_widget.presentation.NextLearningActivityWidgetFeature.State
import org.hyperskill.app.study_plan.widget.presentation.mapper.LearningActivityTargetViewActionMapper
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias NextLearningActivityWidgetReducerResult = Pair<State, Set<Action>>

class NextLearningActivityWidgetReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): NextLearningActivityWidgetReducerResult =
        when (message) {
            is InternalMessage.Initialize -> {
                handleInitialize(state, message)
            }
            is Message.RetryContentLoading -> {
                handleRetryContentLoading(state)
            }
            InternalMessage.FetchNextLearningActivityDataError -> {
                handleFetchNextLearningActivityError(state)
            }
            is InternalMessage.FetchNextLearningActivityDataSuccess -> {
                handleFetchNextLearningActivitySuccess(state, message)
            }
            InternalMessage.PullToRefresh -> {
                handlePullToRefresh(state)
            }
            is Message.NextLearningActivityClicked -> {
                handleNextLearningActivityClicked(state)
            }
            // Flow Messages
            is InternalMessage.NextLearningActivityChanged -> {
                handleNextLearningActivityChanged(state, message)
            }
            is InternalMessage.StudyPlanChanged -> {
                state.copy(studyPlan = message.studyPlan) to emptySet()
            }
        } ?: (state to emptySet())

    private fun handleInitialize(
        state: State,
        message: InternalMessage.Initialize
    ): NextLearningActivityWidgetReducerResult? {
        val contentState = state.contentState
        val shouldReloadContent = contentState is ContentState.Idle ||
            (
                message.forceUpdate &&
                    (
                        contentState is ContentState.Empty ||
                            contentState is ContentState.Content ||
                            contentState is ContentState.NetworkError
                        )
                )

        return if (shouldReloadContent) {
            state.updateContentState(ContentState.Loading) to setOf(
                InternalAction.FetchNextLearningActivity(forceUpdate = message.forceUpdate)
            )
        } else {
            null
        }
    }

    private fun handleRetryContentLoading(state: State): NextLearningActivityWidgetReducerResult? =
        when (state.contentState) {
            is ContentState.NetworkError -> {
                state.updateContentState(ContentState.Loading) to setOf(
                    InternalAction.FetchNextLearningActivity(forceUpdate = true)
                )
            }
            else -> {
                null
            }
        }

    private fun handleFetchNextLearningActivityError(state: State): NextLearningActivityWidgetReducerResult? =
        when (state.contentState) {
            is ContentState.Content -> {
                if (state.isRefreshing) {
                    state.updateContentState(state.contentState.copy(isRefreshing = false)) to emptySet()
                } else {
                    null
                }
            }
            ContentState.Loading -> {
                state.updateContentState(ContentState.NetworkError) to emptySet()
            }
            ContentState.Idle, ContentState.NetworkError, ContentState.Empty -> {
                null
            }
        }

    private fun handleFetchNextLearningActivitySuccess(
        state: State,
        message: InternalMessage.FetchNextLearningActivityDataSuccess
    ): NextLearningActivityWidgetReducerResult? =
        when (state.contentState) {
            is ContentState.Content, ContentState.Loading -> {
                val intermediateState = state.copy(studyPlan = message.studyPlan)
                mapLearningActivityToReducerResult(intermediateState, message.learningActivity)
            }
            ContentState.Idle, ContentState.NetworkError, ContentState.Empty -> {
                null
            }
        }

    private fun handlePullToRefresh(state: State): NextLearningActivityWidgetReducerResult? =
        when (state.contentState) {
            is ContentState.Content -> {
                if (state.isRefreshing) {
                    null
                } else {
                    state.updateContentState(state.contentState.copy(isRefreshing = true)) to setOf(
                        InternalAction.FetchNextLearningActivity(forceUpdate = true)
                    )
                }
            }
            is ContentState.NetworkError, ContentState.Empty -> {
                state.updateContentState(ContentState.Loading) to setOf(
                    InternalAction.FetchNextLearningActivity(forceUpdate = true)
                )
            }
            ContentState.Idle, ContentState.Loading -> {
                null
            }
        }

    private fun handleNextLearningActivityClicked(state: State): NextLearningActivityWidgetReducerResult? {
        if (state.contentState !is ContentState.Content) {
            return null
        }

        val logAnalyticEventAction = InternalAction.LogAnalyticEvent(
            NextLearningActivityWidgetClickedHyperskillAnalyticEvent(
                activityId = state.contentState.learningActivity.id,
                activityType = state.contentState.learningActivity.typeValue,
                activityTargetType = state.contentState.learningActivity.targetTypeValue,
                activityTargetId = state.contentState.learningActivity.targetId
            )
        )

        val activityTargetAction = LearningActivityTargetViewActionMapper
            .mapLearningActivityToTargetViewAction(
                activity = state.contentState.learningActivity,
                trackId = state.studyPlan?.trackId,
                projectId = state.studyPlan?.projectId
            )
            .fold(
                onSuccess = { Action.ViewAction.NavigateTo.LearningActivityTarget(it) },
                onFailure = { InternalAction.CaptureSentryException(it) }
            )

        return state to setOfNotNull(logAnalyticEventAction, activityTargetAction)
    }

    private fun handleNextLearningActivityChanged(
        state: State,
        message: InternalMessage.NextLearningActivityChanged
    ): NextLearningActivityWidgetReducerResult? =
        when (state.contentState) {
            is ContentState.Content -> {
                if (state.isRefreshing) {
                    null
                } else {
                    mapLearningActivityToReducerResult(state, message.learningActivity)
                }
            }
            ContentState.Empty, ContentState.NetworkError -> {
                mapLearningActivityToReducerResult(state, message.learningActivity)
            }
            ContentState.Idle, ContentState.Loading -> {
                null
            }
        }

    private fun mapLearningActivityToReducerResult(
        state: State,
        learningActivity: LearningActivity?
    ): NextLearningActivityWidgetReducerResult =
        if (learningActivity != null) {
            state.updateContentState(ContentState.Content(learningActivity)) to emptySet()
        } else {
            state.updateContentState(ContentState.Empty) to emptySet()
        }

    private fun State.updateContentState(contentState: ContentState): State =
        copy(contentState = contentState)
}