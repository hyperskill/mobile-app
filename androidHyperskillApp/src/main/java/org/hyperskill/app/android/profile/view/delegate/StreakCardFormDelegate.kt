package org.hyperskill.app.android.profile.view.delegate

import android.content.Context
import androidx.core.view.isVisible
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutProfileStreakCardBinding
import org.hyperskill.app.streak.domain.model.HistoricalStreak
import org.hyperskill.app.streak.domain.model.Streak
import org.hyperskill.app.streak.domain.model.StreakState
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class StreakCardFormDelegate(
    private val context: Context,
    private val binding: LayoutProfileStreakCardBinding
) {
    init {
        with(binding.streakTodayIncludedFire.itemStreak) {
            strokeWidth =
                context.resources.getDimensionPixelSize(R.dimen.streak_today_streak_card_stroke_width)
            strokeColor =
                context.getColor(R.color.color_primary)
        }
    }

    fun render(streak: Streak?) {
        binding.root.isVisible = streak != null
        if (streak == null) return

        val historicalStreaks = streak.history.reversed()
        listOf(
            binding.streakFirstDayIncludedFire,
            binding.streakSecondDayIncludedFire,
            binding.streakThirdDayIncludedFire,
            binding.streakFourthDayIncludedFire,
            binding.streakFifthDayIncludedFire
        ).forEachIndexed { index, fireItemBinding ->
            val historicalStreak = historicalStreaks.getOrNull(index)
            fireItemBinding.itemStreakImageView.setImageResource(getFireDrawable(historicalStreak))
        }

        val todayFireDrawable = getFireDrawable(streak.history.firstOrNull())
        binding.streakStatFire.setImageResource(todayFireDrawable)
        binding.streakTodayIncludedFire.itemStreakImageView.setImageResource(todayFireDrawable)

        binding.streakNewRecordCrown.isVisible = streak.isNewRecord

        binding.streakDaysCountTextView.setTextIfChanged(
            context.resources.getQuantityString(
                R.plurals.days,
                streak.currentStreak,
                streak.currentStreak
            )
        )
    }

    private fun getFireDrawable(historicalStreak: HistoricalStreak?): Int =
        when {
            historicalStreak?.state == StreakState.FROZEN -> R.drawable.ic_frozen_streak
            historicalStreak?.isCompleted == true -> R.drawable.ic_enabled_streak
            else -> R.drawable.ic_disabled_streak
        }
}