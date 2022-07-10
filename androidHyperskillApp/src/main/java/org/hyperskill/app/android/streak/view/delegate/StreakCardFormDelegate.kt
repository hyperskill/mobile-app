package org.hyperskill.app.android.streak.view.delegate

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.decoration.HorizontalMarginItemDecoration
import org.hyperskill.app.android.databinding.LayoutStreakCardBinding
import org.hyperskill.app.android.streak.view.adapter.StreakCardAdapterDelegate
import org.hyperskill.app.streak.domain.model.Streak
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter

class StreakCardFormDelegate(
    context: Context,
    binding: LayoutStreakCardBinding,
    streak: Streak
) {
    companion object {
        const val MAX_STREAK_DAYS_TO_SHOW = 5
    }

    private val optionsAdapter = DefaultDelegateAdapter<Unit>()

    init {
        optionsAdapter += StreakCardAdapterDelegate(optionsAdapter, streak)
        optionsAdapter.items = List(5) {}

        with(binding.streakLastFiveRecyclerView) {
            adapter = optionsAdapter
            layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
                override fun canScrollHorizontally(): Boolean = false
            }

            addItemDecoration(
                HorizontalMarginItemDecoration(
                    resources.getDimensionPixelSize(R.dimen.streak_last_five_recycler_items_space),
                    0,
                    0
                )
            )
        }

//        if (streak.kind == Streak.Kind.COMPLETED || streak.kind == Streak.Kind.MANUAL_COMPLETED) {
//            binding.streakStatFire.setImageResource(R.drawable.ic_fire_enabled)
//            binding.streakTodayIncludedFire.itemStreakImageView.setImageResource(R.drawable.ic_fire_enabled)
//        }

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