package org.hyperskill.app.share_streak.domain.interactor

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.hyperskill.app.share_streak.domain.repository.ShareStreakRepository
import org.hyperskill.app.streaks.domain.model.StreakState

class ShareStreakInteractor(private val shareStreakRepository: ShareStreakRepository) {
    companion object {
        private val SHAREABLE_STREAKS = setOf(5, 10, 25, 50, 100)
    }

    fun setLastTimeShareStreakShown() {
        shareStreakRepository.setLastTimeShareStreakShown()
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
        val lastTimeShownLocalDateTime =
            shareStreakRepository.getLastTimeShareStreakShown()?.toLocalDateTime(TimeZone.UTC) ?: return false

        val nowLocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.UTC)

        return lastTimeShownLocalDateTime.year == nowLocalDateTime.year &&
            lastTimeShownLocalDateTime.monthNumber == nowLocalDateTime.monthNumber &&
            lastTimeShownLocalDateTime.dayOfMonth == nowLocalDateTime.dayOfMonth
    }
}