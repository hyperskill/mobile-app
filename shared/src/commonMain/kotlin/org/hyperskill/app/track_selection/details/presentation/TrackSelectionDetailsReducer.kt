package org.hyperskill.app.track_selection.details.presentation

import org.hyperskill.app.subscriptions.domain.model.SubscriptionType
import org.hyperskill.app.track.domain.model.getAllProjects
import org.hyperskill.app.track_selection.details.domain.analytic.TrackSelectionDetailsClickedRetryContentLoadingHyperskillAnalyticsEvent
import org.hyperskill.app.track_selection.details.domain.analytic.TrackSelectionDetailsSelectButtonClickedHyperskillAnalyticEvent
import org.hyperskill.app.track_selection.details.domain.analytic.TrackSelectionDetailsViewedHyperskillAnalyticEvent
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Action
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Action.ViewAction
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.ContentState
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.InternalAction
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.Message
import org.hyperskill.app.track_selection.details.presentation.TrackSelectionDetailsFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

internal class TrackSelectionDetailsReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            is Message.Initialize -> {
                state.updateContentState(ContentState.Loading) to
                    fetchAdditionalInfo(state, forceLoadFromNetwork = false)
            }
            is Message.RetryContentLoading ->
                handleRetryContentLoading(state)
            is TrackSelectionDetailsFeature.FetchAdditionalInfoResult ->
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
            InternalAction.FetchAdditionalInfo(
                providerIds = state.trackWithProgress.track.topicProviders,
                forceLoadFromNetwork = forceLoadFromNetwork
            )
        )

    private fun handleAdditionalInfoFetchResult(
        state: State,
        message: TrackSelectionDetailsFeature.FetchAdditionalInfoResult
    ): ReducerResult {
        val contentState = when (message) {
            is TrackSelectionDetailsFeature.FetchAdditionalInfoResult.Success -> {
                ContentState.Content(
                    subscriptionType = message.subscriptionType,
                    profile = message.profile,
                    providers = message.providers
                )
            }
            TrackSelectionDetailsFeature.FetchAdditionalInfoResult.Error ->
                ContentState.NetworkError
        }
        return state.updateContentState(contentState) to emptySet()
    }

    private fun handleSelectTrackButtonClicked(
        state: State
    ): ReducerResult =
        if (state.isTrackSelected) {
            state to emptySet()
        } else {
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
        }

    private fun handleTrackSelectionResult(
        state: State,
        message: TrackSelectionDetailsFeature.TrackSelectionResult
    ): ReducerResult =
        state.copy(isTrackLoadingShowed = false) to
            when (message) {
                TrackSelectionDetailsFeature.TrackSelectionResult.Success -> {
                    val navigationAction = when {
                        !state.isNewUserMode ->
                            ViewAction.NavigateTo.StudyPlan
                        isProjectSelectionAvailable(state) ->
                            ViewAction.NavigateTo.ProjectSelectionList(
                                trackId = state.trackWithProgress.track.id,
                                isNewUserMode = state.isNewUserMode
                            )
                        else ->
                            ViewAction.NavigateTo.Home
                    }
                    setOf(
                        ViewAction.ShowTrackSelectionStatus.Success,
                        navigationAction
                    )
                }
                TrackSelectionDetailsFeature.TrackSelectionResult.Error ->
                    setOf(ViewAction.ShowTrackSelectionStatus.Error)
            }

    private fun isProjectSelectionAvailable(state: State): Boolean {
        if (state.contentState !is ContentState.Content) {
            return false
        }
        return when (state.contentState.subscriptionType) {
            SubscriptionType.PREMIUM,
            SubscriptionType.PERSONAL,
            SubscriptionType.TRIAL -> {
                val trackRelatedProjects =
                    state.trackWithProgress.track.getAllProjects(state.contentState.profile.isBeta)
                trackRelatedProjects.isNotEmpty()
            }
            else -> false
        }
    }

    private fun State.updateContentState(contentState: ContentState): State =
        copy(contentState = contentState)
}