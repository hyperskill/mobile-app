package org.hyperskill.app.android.streak.delegate

import androidx.recyclerview.widget.LinearLayoutManager
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.LayoutStreakCardBinding
import org.hyperskill.app.android.streak.adapter.StreakCardDisabledAdapterDelegate
import org.hyperskill.app.android.streak.adapter.StreakCardEnabledAdapterDelegate
import org.hyperskill.app.android.streak.decorator.SpacingDecorator
import org.hyperskill.app.streak.domain.model.Streak
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter

class StreakCardFormDelegate(
    binding: LayoutStreakCardBinding,
) {
    companion object {
        const val MAX_STREAK_DAYS_TO_SHOW = 5
    }
    private val optionsAdapter = DefaultDelegateAdapter<Unit>()

    init {
        // TODO test streak, replace from GET request
        val streak = Streak(0, "", 3, 0, false)

        optionsAdapter += StreakCardEnabledAdapterDelegate(streak)
        optionsAdapter += StreakCardDisabledAdapterDelegate(streak)
        optionsAdapter.items = List(5) {}

        with(binding.streakLastFiveRecyclerView) {
            adapter = optionsAdapter
            layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
                override fun canScrollHorizontally(): Boolean = false
            }

            addItemDecoration(SpacingDecorator(resources.getDimensionPixelSize(R.dimen.streak_last_five_recycler_items_space)))
        }
    }
}