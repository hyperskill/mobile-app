package org.hyperskill.app.home.view.mapper

import org.hyperskill.app.challenges.widget.view.mapper.ChallengeWidgetViewStateMapper
import org.hyperskill.app.gamification_toolbar.view.mapper.GamificationToolbarViewStateMapper
import org.hyperskill.app.home.presentation.HomeFeature

internal class HomeViewStateMapper(
    private val challengeWidgetViewStateMapper: ChallengeWidgetViewStateMapper
) {
    fun map(state: HomeFeature.State): HomeFeature.ViewState =
        HomeFeature.ViewState(
            homeState = state.homeState,
            toolbarViewState = GamificationToolbarViewStateMapper.map(state.toolbarState),
            challengeWidgetViewState = challengeWidgetViewStateMapper.map(state.challengeWidgetState),
            isRefreshing = state.isRefreshing
        )
}