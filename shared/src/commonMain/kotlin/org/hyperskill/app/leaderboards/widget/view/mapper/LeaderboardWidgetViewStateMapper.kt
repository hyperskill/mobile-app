package org.hyperskill.app.leaderboards.widget.view.mapper

import org.hyperskill.app.leaderboards.widget.presentation.LeaderboardWidgetFeature

class LeaderboardWidgetViewStateMapper {
    fun map(state: LeaderboardWidgetFeature.State): LeaderboardWidgetFeature.ViewState =
        when (state) {
            LeaderboardWidgetFeature.State.Idle -> LeaderboardWidgetFeature.ViewState.Idle
            LeaderboardWidgetFeature.State.Loading -> LeaderboardWidgetFeature.ViewState.Loading
            LeaderboardWidgetFeature.State.Error -> LeaderboardWidgetFeature.ViewState.Error
            is LeaderboardWidgetFeature.State.Content -> LeaderboardWidgetFeature.ViewState.Content
        }
}