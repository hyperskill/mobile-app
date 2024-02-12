package org.hyperskill.app.home.view.mapper

import org.hyperskill.app.challenges.widget.view.mapper.ChallengeWidgetViewStateMapper
import org.hyperskill.app.gamification_toolbar.view.mapper.GamificationToolbarViewStateMapper
import org.hyperskill.app.home.presentation.HomeFeature
import org.hyperskill.app.interview_preparation.view.mapper.InterviewPreparationWidgetViewStateMapper

internal class HomeViewStateMapper(
    private val challengeWidgetViewStateMapper: ChallengeWidgetViewStateMapper,
    private val interviewPreparationWidgetViewStateMapper: InterviewPreparationWidgetViewStateMapper
) {
    fun map(state: HomeFeature.State): HomeFeature.ViewState =
        HomeFeature.ViewState(
            homeState = state.homeState,
            toolbarViewState = GamificationToolbarViewStateMapper.map(state.toolbarState),
            challengeWidgetViewState = challengeWidgetViewStateMapper.map(state.challengeWidgetState),
            interviewPreparationWidgetViewState = interviewPreparationWidgetViewStateMapper.map(
                state.interviewPreparationWidgetState
            ),
            usersQuestionnaireWidgetState = state.usersQuestionnaireWidgetState,
            isRefreshing = state.isRefreshing
        )
}