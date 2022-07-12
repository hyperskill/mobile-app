package org.hyperskill.app.android.streak.view.delegate

import android.content.Context
import android.view.View
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutStreakCardBinding
import org.hyperskill.app.streak.domain.model.Streak
import org.hyperskill.app.streak.domain.model.StreakState
import kotlin.collections.zip

class StreakCardFormDelegate(
    context: Context,
    binding: LayoutStreakCardBinding,
    streak: Streak
) {
    init {
        val streakDaysFires = listOf(
            binding.streakFirstDayIncludedFire,
            binding.streakSecondDayIncludedFire,
            binding.streakThirdDayIncludedFire,
            binding.streakFourthDayIncludedFire,
            binding.streakFifthDayIncludedFire
        )

        for (pair in streakDaysFires zip streak.history.reversed()) {
            if (pair.second.state == StreakState.FROZEN) {
                pair.first.itemStreakImageView.setImageResource(R.drawable.ic_fire_freeze)
            } else if (pair.second.isCompleted) {
                pair.first.itemStreakImageView.setImageResource(R.drawable.ic_fire_enabled)
            }
        }

        if (streak.history.first().isCompleted) {
            binding.streakStatFire.setImageResource(R.drawable.ic_fire_enabled)
            binding.streakTodayIncludedFire.itemStreakImageView.setImageResource(R.drawable.ic_fire_enabled)
        }

        if (streak.isNewRecord) {
            binding.streakNewRecordCrown.visibility = View.VISIBLE
        }

        binding.streakDaysCountTextView.text = "${streak.currentStreak}"

        binding.streakTodayIncludedFire.itemStreak.strokeWidth =
            context.resources.getDimensionPixelSize(R.dimen.streak_today_streak_card_stroke_width)

        binding.streakTodayIncludedFire.itemStreak.strokeColor =
            context.getColor(R.color.color_primary)
    }
}