package org.hyperskill.app.track_selection.details.presentation

import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Action
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.ContentState
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.InternalAction
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Message
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.State
import org.hyperskill.app.track_selection.domain.analytic.TrackSelectionDetailsClickedRetryContentLoadingHyperskillAnalyticsEvent
import org.hyperskill.app.track_selection.domain.analytic.TrackSelectionDetailsSelectButtonClickedHyperskillAnalyticEvent
import org.hyperskill.app.track_selection.domain.analytic.TrackSelectionDetailsViewedHyperskillAnalyticEvent
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

internal class TrackSelectionDetailsReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): Pair<State, Set<Action>> =
        when (message) {
            is Message.Initialize -> {
                state.updateContentState(ContentState.Loading) to
                    fetchAdditionalInfo(state, forceLoadFromNetwork = false)
            }
            is Message.RetryContentLoading ->
                handleRetryContentLoading(state)
            is TrackSelectionDetailsFeature.FetchProvidersAndFreemiumStatusResult ->
                handleAdditionalInfoFetchResult(state, message)
            Message.SelectTrackButtonClicked ->
                handleSelectTrackButtonClicked(state)
            is TrackSelectionDetailsFeature.TrackSelectionResult ->
                handleTrackSelectionResult(state, message)
            is Message.ViewedEventMessage ->
                state to setOf(
                    InternalAction.LogAnalyticEvent(
                        TrackSelectionDetailsViewedHyperskillAnalyticEvent(state.trackWithProgress.track.id)
                    )
                )
        }

    private fun handleRetryContentLoading(
        state: State
    ): ReducerResult {
        val logAnalyticsAction = InternalAction.LogAnalyticEvent(
            TrackSelectionDetailsClickedRetryContentLoadingHyperskillAnalyticsEvent(
                trackId = state.trackWithProgress.track.id
            )
        )
        return if (state.contentState is ContentState.NetworkError) {
            state.updateContentState(ContentState.Loading) to
                fetchAdditionalInfo(state, forceLoadFromNetwork = true) + logAnalyticsAction
        } else {
            state to setOf(logAnalyticsAction)
        }
    }

    private fun fetchAdditionalInfo(
        state: State,
        forceLoadFromNetwork: Boolean
    ): Set<Action> =
        setOf(
            InternalAction.FetchProvidersAndFreemiumStatus(
                providerIds = state.trackWithProgress.track.topicProviders,
                forceLoadFromNetwork = forceLoadFromNetwork
            )
        )

    private fun handleAdditionalInfoFetchResult(
        state: State,
        message: TrackSelectionDetailsFeature.FetchProvidersAndFreemiumStatusResult
    ): ReducerResult {
        val contentState = when (message) {
            is TrackSelectionDetailsFeature.FetchProvidersAndFreemiumStatusResult.Success -> {
                ContentState.Content(
                    isFreemiumEnabled = message.isFreemiumEnabled,
                    providers = message.providers
                )
            }
            TrackSelectionDetailsFeature.FetchProvidersAndFreemiumStatusResult.Error ->
                ContentState.NetworkError
        }
        return state.updateContentState(contentState) to emptySet()
    }

    private fun handleSelectTrackButtonClicked(
        state: State
    ): ReducerResult =
        state.copy(isTrackLoadingShowed = true) to
            setOf(
                InternalAction.SelectTrack(
                    trackId = state.trackWithProgress.track.id
                ),
                InternalAction.LogAnalyticEvent(
                    TrackSelectionDetailsSelectButtonClickedHyperskillAnalyticEvent(
                        state.trackWithProgress.track.id
                    )
                )
            )

    private fun handleTrackSelectionResult(
        state: State,
        message: TrackSelectionDetailsFeature.TrackSelectionResult
    ): ReducerResult =
        state.copy(isTrackLoadingShowed = false) to
            setOf(
                when (message) {
                    TrackSelectionDetailsFeature.TrackSelectionResult.Success ->
                        Action.ViewAction.ShowTrackSelectionStatus.Success
                    TrackSelectionDetailsFeature.TrackSelectionResult.Error ->
                        Action.ViewAction.ShowTrackSelectionStatus.Error
                }
            )

    private fun State.updateContentState(contentState: ContentState): State =
        copy(contentState = contentState)
}