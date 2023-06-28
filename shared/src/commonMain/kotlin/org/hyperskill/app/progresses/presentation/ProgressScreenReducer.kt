package org.hyperskill.app.progresses.presentation

import org.hyperskill.app.progresses.domain.analytic.ProgressScreenClickedBackHyperskillAnalyticsEvent
import org.hyperskill.app.progresses.domain.analytic.ProgressScreenClickedPullToRefreshHyperskillAnalyticEvent
import org.hyperskill.app.progresses.domain.analytic.ProgressScreenViewedHyperskillAnalyticEvent
import org.hyperskill.app.progresses.presentation.ProgressScreenFeature.Action
import org.hyperskill.app.progresses.presentation.ProgressScreenFeature.InternalAction
import org.hyperskill.app.progresses.presentation.ProgressScreenFeature.Message
import org.hyperskill.app.progresses.presentation.ProgressScreenFeature.ProjectProgressState
import org.hyperskill.app.progresses.presentation.ProgressScreenFeature.State
import org.hyperskill.app.progresses.presentation.ProgressScreenFeature.TrackProgressState
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ProgressScreenReducerResult = Pair<State, Set<Action>>

internal class ProgressScreenReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ProgressScreenReducerResult =
        when (message) {
            Message.Initialize -> {
                handleInitialize(state)
            }
            Message.PullToRefresh -> {
                handlePullToRefresh(state)
            }
            Message.RetryTrackProgressLoading -> {
                handleRetryTrackProgressLoading(state)
            }
            Message.RetryProjectProgressLoading -> {
                handleRetryProjectProgressLoading(state)
            }
            is ProgressScreenFeature.TrackWithProgressFetchResult -> {
                handleTrackWithProgressFetchResult(state, message)
            }
            is ProgressScreenFeature.ProjectWithProgressFetchResult -> {
                handleProjectWithProgressFetchResult(state, message)
            }
            Message.BackButtonClicked -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        ProgressScreenClickedBackHyperskillAnalyticsEvent()
                    ),
                    Action.ViewAction.NavigateTo.Back
                )
            }
            Message.ViewedEventMessage -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        ProgressScreenViewedHyperskillAnalyticEvent()
                    )
                )
            }
        } ?: (state to emptySet())

    private fun handleInitialize(state: State): ProgressScreenReducerResult? =
        if (state.trackProgressState is TrackProgressState.Idle &&
            state.projectProgressState is ProjectProgressState.Idle
        ) {
            state.copy(
                trackProgressState = TrackProgressState.Loading,
                projectProgressState = ProjectProgressState.Loading
            ) to fetchContent()
        } else {
            null
        }

    private fun handlePullToRefresh(state: State): ProgressScreenReducerResult =
        state.copy(
            isTrackProgressRefreshing = true,
            isProjectProgressRefreshing = true
        ) to fetchContent(forceLoadFromNetwork = true) + setOf(
            InternalAction.LogAnalyticEvent(
                ProgressScreenClickedPullToRefreshHyperskillAnalyticEvent()
            )
        )

    private fun handleRetryTrackProgressLoading(state: State): ProgressScreenReducerResult? =
        if (state.trackProgressState is TrackProgressState.Error) {
            state.copy(
                trackProgressState = TrackProgressState.Loading
            ) to setOf(
                InternalAction.FetchTrackWithProgress(forceLoadFromNetwork = true)
            )
        } else {
            null
        }

    private fun handleRetryProjectProgressLoading(state: State): ProgressScreenReducerResult? =
        if (state.projectProgressState is ProjectProgressState.Error) {
            state.copy(
                projectProgressState = ProjectProgressState.Loading
            ) to setOf(
                InternalAction.FetchProjectWithProgress(forceLoadFromNetwork = true)
            )
        } else {
            null
        }

    private fun handleTrackWithProgressFetchResult(
        state: State,
        message: ProgressScreenFeature.TrackWithProgressFetchResult
    ): ProgressScreenReducerResult =
        state.copy(
            trackProgressState = when (message) {
                ProgressScreenFeature.TrackWithProgressFetchResult.Error ->
                    TrackProgressState.Error
                is ProgressScreenFeature.TrackWithProgressFetchResult.Success ->
                    TrackProgressState.Content(message.trackWithProgress)
            },
            isTrackProgressRefreshing = false
        ) to emptySet()

    private fun handleProjectWithProgressFetchResult(
        state: State,
        message: ProgressScreenFeature.ProjectWithProgressFetchResult
    ): ProgressScreenReducerResult =
        state.copy(
            projectProgressState = when (message) {
                ProgressScreenFeature.ProjectWithProgressFetchResult.Empty ->
                    ProjectProgressState.Idle
                ProgressScreenFeature.ProjectWithProgressFetchResult.Error ->
                    ProjectProgressState.Error
                is ProgressScreenFeature.ProjectWithProgressFetchResult.Success ->
                    ProjectProgressState.Content(message.projectWithProgress)
            },
            isProjectProgressRefreshing = false
        ) to emptySet()

    private fun fetchContent(forceLoadFromNetwork: Boolean = false): Set<Action> =
        setOf(
            InternalAction.FetchTrackWithProgress(forceLoadFromNetwork),
            InternalAction.FetchProjectWithProgress(forceLoadFromNetwork)
        )
}