package org.hyperskill.app.share_streak.domain.interactor

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.hyperskill.app.gamification_toolbar.domain.repository.CurrentGamificationToolbarDataStateRepository
import org.hyperskill.app.share_streak.domain.repository.ShareStreakRepository
import org.hyperskill.app.streaks.domain.model.StreakState

class ShareStreakInteractor(
    private val shareStreakRepository: ShareStreakRepository,
    private val currentGamificationToolbarDataStateRepository: CurrentGamificationToolbarDataStateRepository
) {
    companion object {
        private val SHAREABLE_STREAKS = setOf(1, 5, 10, 25, 50, 100)
    }

    suspend fun shouldShareStreakAfterStepSolved(): Boolean {
        if (isUserSawShareStreakToday()) {
            return false
        }

        val currentGamificationToolbarData = currentGamificationToolbarDataStateRepository
            .getState(forceUpdate = false)
            .getOrElse { return false }

        val isNewStreakReached = currentGamificationToolbarData.streakState == StreakState.NOTHING
        if (isNewStreakReached) {
            val newStreakValue = currentGamificationToolbarData.currentStreak + 1
            return SHAREABLE_STREAKS.contains(newStreakValue)
        }

        return false
    }

    private fun isUserSawShareStreakToday(): Boolean {
        val lastTimeModalShownEpochMilliseconds = shareStreakRepository.getLastTimeModalShown() ?: return false
        val lastTimeModalShownLocalDateTime = Instant
            .fromEpochMilliseconds(lastTimeModalShownEpochMilliseconds)
            .toLocalDateTime(TimeZone.UTC)

        val instantNow = Clock.System.now()
        val nowLocalDateTime = instantNow.toLocalDateTime(TimeZone.UTC)

        return lastTimeModalShownLocalDateTime.year == nowLocalDateTime.year &&
            lastTimeModalShownLocalDateTime.monthNumber == nowLocalDateTime.monthNumber &&
            lastTimeModalShownLocalDateTime.dayOfMonth == nowLocalDateTime.dayOfMonth
    }
}