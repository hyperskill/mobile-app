package org.hyperskill.app.android.streak.view.delegate

import androidx.recyclerview.widget.LinearLayoutManager
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.decoration.HorizontalMarginItemDecoration
import org.hyperskill.app.android.databinding.LayoutStreakCardBinding
import org.hyperskill.app.android.streak.view.adapter.StreakCardAdapterDelegate
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
        val streak = Streak(0, "", 2, 0, false, emptyList())

        optionsAdapter += StreakCardAdapterDelegate(streak)
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
    }
}