package org.hyperskill.app.gamification_toolbar.view.mapper

import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarTrackProgress
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.streaks.domain.model.StreakState
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.areProblemsLimited

internal object GamificationToolbarViewStateMapper {
    fun map(state: GamificationToolbarFeature.State): GamificationToolbarFeature.ViewState =
        when (state) {
            GamificationToolbarFeature.State.Idle -> GamificationToolbarFeature.ViewState.Idle
            GamificationToolbarFeature.State.Error -> GamificationToolbarFeature.ViewState.Error
            GamificationToolbarFeature.State.Loading -> GamificationToolbarFeature.ViewState.Loading
            is GamificationToolbarFeature.State.Content -> getLoadedContent(state)
        }

    private fun getLoadedContent(
        state: GamificationToolbarFeature.State.Content
    ): GamificationToolbarFeature.ViewState.Content =
        GamificationToolbarFeature.ViewState.Content(
            progress = state.trackProgress?.let { getProgress(it) },
            streak = GamificationToolbarFeature.ViewState.Content.Streak(
                formattedValue = state.currentStreak.toString(),
                isCompleted = state.historicalStreak.isCompleted,
                isRecovered = state.historicalStreak.state == StreakState.RECOVERED
            ),
            problemsLimit = getProblemsLimitState(state.subscription)
        )

    private fun getProgress(
        trackProgress: GamificationToolbarTrackProgress
    ): GamificationToolbarFeature.ViewState.Content.Progress {
        val normalizedAverageProgress = trackProgress.averageProgress
            .coerceAtMost(100)
            .coerceAtLeast(0)
        return GamificationToolbarFeature.ViewState.Content.Progress(
            value = normalizedAverageProgress / 100f,
            formattedValue = "$normalizedAverageProgress%",
            isCompleted = trackProgress.isCompleted
        )
    }

    private fun getProblemsLimitState(
        subscription: Subscription
    ): GamificationToolbarFeature.ViewState.Content.ProblemsLimit? {
        val stepsLimitLeft = subscription.stepsLimitLeft
        return if (!subscription.areProblemsLimited || stepsLimitLeft == null) {
            null
        } else {
            GamificationToolbarFeature.ViewState.Content.ProblemsLimit(limitLabel = stepsLimitLeft.toString())
        }
    }
}