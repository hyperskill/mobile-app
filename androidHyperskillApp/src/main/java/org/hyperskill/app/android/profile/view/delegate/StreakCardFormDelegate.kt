package org.hyperskill.app.android.profile.view.delegate

import android.content.Context
import androidx.core.view.isVisible
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutProfileStreakCardBinding
import org.hyperskill.app.profile.presentation.ProfileFeature
import org.hyperskill.app.streaks.domain.model.HistoricalStreak
import org.hyperskill.app.streaks.domain.model.Streak
import org.hyperskill.app.streaks.domain.model.StreakState
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class StreakCardFormDelegate(
    private val context: Context,
    private val binding: LayoutProfileStreakCardBinding,
    onFreezeButtonClick: () -> Unit
) {
    init {
        with(binding.streakTodayIncludedFire.itemStreak) {
            strokeWidth =
                context.resources.getDimensionPixelSize(R.dimen.streak_today_streak_card_stroke_width)
            strokeColor =
                context.getColor(org.hyperskill.app.R.color.color_primary)
        }
        with(binding) {
            streakCardBuyFreezeButton.setOnClickListener {
                onFreezeButtonClick()
            }
            streakFreezeAlreadyHaveTextView.setOnClickListener {
                onFreezeButtonClick()
            }
        }
    }

    fun render(streak: Streak?, freezeState: ProfileFeature.StreakFreezeState?) {
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
                org.hyperskill.app.R.plurals.days,
                streak.currentStreak,
                streak.currentStreak
            )
        )

        binding.streakCardBuyFreezeButton.isVisible =
            freezeState is ProfileFeature.StreakFreezeState.CanBuy ||
            freezeState is ProfileFeature.StreakFreezeState.NotEnoughGems
        binding.streakFreezeAlreadyHaveTextView.isVisible =
            freezeState is ProfileFeature.StreakFreezeState.AlreadyHave
    }

    private fun getFireDrawable(historicalStreak: HistoricalStreak?): Int =
        when {
            historicalStreak?.state == StreakState.FROZEN -> R.drawable.ic_frozen_streak
            historicalStreak?.isCompleted == true -> R.drawable.ic_enabled_streak
            else -> R.drawable.ic_disabled_streak
        }
}