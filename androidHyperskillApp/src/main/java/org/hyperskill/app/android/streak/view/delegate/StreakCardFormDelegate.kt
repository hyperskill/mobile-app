package org.hyperskill.app.android.streak.view.delegate

import android.content.Context
import android.view.View
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutStreakCardBinding
import org.hyperskill.app.streak.domain.model.Streak
import org.hyperskill.app.streak.domain.model.StreakState

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

        for (i in streakDaysFires.indices) {
            if (streak.currentStreak + i >= streakDaysFires.size) {
                streakDaysFires[i].itemStreakImageView.setImageResource(R.drawable.ic_fire_enabled)
            }
        }

        if (streak.history.lastOrNull()?.state == StreakState.FROZEN) {
            streakDaysFires.last().itemStreakImageView.setImageResource(R.drawable.ic_fire_freeze)
        }

        if (streak.history.lastOrNull()?.isCompleted == true) {
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