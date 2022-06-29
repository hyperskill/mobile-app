package org.hyperskill.app.android.streak.adapter

import android.view.View
import android.view.ViewGroup
import org.hyperskill.app.android.R
import org.hyperskill.app.android.streak.delegate.StreakCardFormDelegate.Companion.MAX_STREAK_DAYS_TO_SHOW
import org.hyperskill.app.streak.domain.model.Streak
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder

class StreakCardEnabledAdapterDelegate(
    private val streak: Streak
) : AdapterDelegate<Unit, DelegateViewHolder<Unit>>() {
    override fun isForViewType(position: Int, data: Unit): Boolean =
        (position + streak.currentStreak >= MAX_STREAK_DAYS_TO_SHOW)

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<Unit> =
        ViewHolder(createView(parent, R.layout.item_fire_enabled))

    private inner class ViewHolder(root: View) : DelegateViewHolder<Unit>(root)
}
