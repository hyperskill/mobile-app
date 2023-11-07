package org.hyperskill.app.share_streak.domain.interactor

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.hyperskill.app.share_streak.domain.repository.ShareStreakRepository
import org.hyperskill.app.streaks.domain.model.StreakState

class ShareStreakInteractor(private val shareStreakRepository: ShareStreakRepository) {
    companion object {
        private val SHAREABLE_STREAKS = setOf(1, 5, 10, 25, 50, 100)
    }

    fun setLastTimeShareStreakShown() {
        val nowEpochMilliseconds = Clock.System.now().toEpochMilliseconds()
        shareStreakRepository.setLastTimeShareStreakShown(epochMilliseconds = nowEpochMilliseconds)
    }

    fun shouldShareStreakAfterStepSolved(streak: Int, streakState: StreakState): Boolean {
        if (isUserSawShareStreakToday()) {
            return false
        }
        return when (streakState) {
            StreakState.NOTHING,
            StreakState.COMPLETED,
            StreakState.MANUAL_COMPLETED ->
                SHAREABLE_STREAKS.contains(streak)
            else -> false
        }
    }

    private fun isUserSawShareStreakToday(): Boolean {
        val lastTimeShownEpochMilliseconds =
            shareStreakRepository.getLastTimeShareStreakShownEpochMilliseconds() ?: return false
        val lastTimeShownLocalDateTime = Instant
            .fromEpochMilliseconds(lastTimeShownEpochMilliseconds)
            .toLocalDateTime(TimeZone.UTC)

        val nowLocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)

        return lastTimeShownLocalDateTime.year == nowLocalDateTime.year &&
            lastTimeShownLocalDateTime.monthNumber == nowLocalDateTime.monthNumber &&
            lastTimeShownLocalDateTime.dayOfMonth == nowLocalDateTime.dayOfMonth
    }
}