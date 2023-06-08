package org.hyperskill.app.project_selection.details.presentation

import org.hyperskill.app.project_selection.details.domain.analytic.ProjectSelectionDetailsClickedRetryContentLoadingHyperskillAnalyticsEvent
import org.hyperskill.app.project_selection.details.domain.analytic.ProjectSelectionDetailsClickedSelectThisProjectHyperskillAnalyticsEvent
import org.hyperskill.app.project_selection.details.domain.analytic.ProjectSelectionDetailsViewedHyperskillAnalyticEvent
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.Action
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.ContentState
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.InternalAction
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.Message
import org.hyperskill.app.project_selection.details.presentation.ProjectSelectionDetailsFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ProjectSelectionDetailsReducerResult = Pair<State, Set<Action>>

internal class ProjectSelectionDetailsReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ProjectSelectionDetailsReducerResult =
        when (message) {
            Message.Initialize -> {
                handleInitialize(state)
            }
            ProjectSelectionDetailsFeature.ContentFetchResult.Error -> {
                handleContentFetchResultError(state)
            }
            is ProjectSelectionDetailsFeature.ContentFetchResult.Success -> {
                handleContentFetchResultSuccess(state, message)
            }
            Message.RetryContentLoading -> {
                handleRetryContentLoading(state)
            }
            Message.SelectProjectButtonClicked -> {
                handleSelectProjectButtonClicked(state)
            }
            is ProjectSelectionDetailsFeature.ProjectSelectionResult -> {
                handleProjectSelectionResult(state, message)
            }
            Message.ViewedEventMessage -> {
                val analyticEvent = ProjectSelectionDetailsViewedHyperskillAnalyticEvent(
                    projectId = state.projectId,
                    trackId = state.trackId
                )
                state to setOf(InternalAction.LogAnalyticEvent(analyticEvent))
            }
        }

    private fun handleInitialize(state: State): ProjectSelectionDetailsReducerResult =
        if (state.contentState is ContentState.Idle) {
            state.updateContentState(ContentState.Loading) to
                fetchContent(state)
        } else {
            state to emptySet()
        }

    private fun handleContentFetchResultError(state: State): ProjectSelectionDetailsReducerResult =
        if (state.contentState is ContentState.Loading) {
            state.updateContentState(ContentState.Error) to emptySet()
        } else {
            state to emptySet()
        }

    private fun handleContentFetchResultSuccess(
        state: State,
        message: ProjectSelectionDetailsFeature.ContentFetchResult.Success
    ): ProjectSelectionDetailsReducerResult =
        if (state.contentState is ContentState.Loading) {
            state.updateContentState(ContentState.Content(message.data)) to emptySet()
        } else {
            state to emptySet()
        }

    private fun handleRetryContentLoading(state: State): ProjectSelectionDetailsReducerResult {
        val analyticEventAction = InternalAction.LogAnalyticEvent(
            ProjectSelectionDetailsClickedRetryContentLoadingHyperskillAnalyticsEvent(
                projectId = state.projectId,
                trackId = state.trackId
            )
        )

        return if (state.contentState is ContentState.Error) {
            state.updateContentState(ContentState.Loading) to
                fetchContent(state, forceLoadFromNetwork = true) + analyticEventAction
        } else {
            state to setOf(analyticEventAction)
        }
    }

    private fun fetchContent(
        state: State,
        forceLoadFromNetwork: Boolean = false
    ): Set<Action> =
        setOf(
            InternalAction.FetchContent(
                trackId = state.trackId,
                projectId = state.projectId,
                forceLoadFromNetwork = forceLoadFromNetwork
            )
        )

    private fun handleSelectProjectButtonClicked(state: State): ProjectSelectionDetailsReducerResult {
        val analyticEventAction = InternalAction.LogAnalyticEvent(
            ProjectSelectionDetailsClickedSelectThisProjectHyperskillAnalyticsEvent(
                projectId = state.projectId,
                trackId = state.trackId
            )
        )

        return if (state.isProjectSelected) {
            state to setOf(analyticEventAction)
        } else {
            state.copy(isSelectProjectLoadingShowed = true) to setOf(
                InternalAction.SelectProject(
                    trackId = state.trackId,
                    projectId = state.projectId
                ),
                analyticEventAction
            )
        }
    }

    private fun handleProjectSelectionResult(
        state: State,
        message: ProjectSelectionDetailsFeature.ProjectSelectionResult
    ): ProjectSelectionDetailsReducerResult =
        state.copy(isSelectProjectLoadingShowed = false) to
            when (message) {
                ProjectSelectionDetailsFeature.ProjectSelectionResult.Success ->
                    setOf(
                        InternalAction.ClearProjectsCache,
                        Action.ViewAction.ShowProjectSelectionStatus.Success,
                        Action.ViewAction.NavigateTo.StudyPlan(
                            if (state.isNewUserMode) {
                                Action.ViewAction.NavigateTo.StudyPlan.NavigationCommand.NewRootScreen
                            } else {
                                Action.ViewAction.NavigateTo.StudyPlan.NavigationCommand.BackTo
                            }
                        )
                    )
                ProjectSelectionDetailsFeature.ProjectSelectionResult.Error ->
                    setOf(Action.ViewAction.ShowProjectSelectionStatus.Error)
            }

    private fun State.updateContentState(contentState: ContentState): State =
        copy(contentState = contentState)
}