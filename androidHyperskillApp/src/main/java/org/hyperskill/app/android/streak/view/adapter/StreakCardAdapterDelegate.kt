package org.hyperskill.app.android.streak.view.adapter

import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.ItemStreakBinding
import org.hyperskill.app.android.streak.view.delegate.StreakCardFormDelegate.Companion.MAX_STREAK_DAYS_TO_SHOW
import org.hyperskill.app.streak.domain.model.Streak
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter

class StreakCardAdapterDelegate(
    private val adapter: DefaultDelegateAdapter<Unit>,
    private val streak: Streak
) : AdapterDelegate<Unit, DelegateViewHolder<Unit>>() {
    override fun isForViewType(position: Int, data: Unit): Boolean =
        true

    override fun onCreateViewHolder(parent: ViewGroup): DelegateViewHolder<Unit> =
        ViewHolder(createView(parent, R.layout.item_streak))

    private inner class ViewHolder(root: View) : DelegateViewHolder<Unit>(root) {
        private val viewBinding: ItemStreakBinding by viewBinding(ItemStreakBinding::bind)

        override fun onBind(data: Unit) {
            if (adapterPosition + streak.currentStreak >= MAX_STREAK_DAYS_TO_SHOW) {
                viewBinding.itemStreakImageView.setImageResource(R.drawable.ic_fire_enabled)
            }

            if (adapterPosition == adapter.itemCount - 1 && streak.kind == Streak.Kind.FROZEN) {
                viewBinding.itemStreakImageView.setImageResource(R.drawable.ic_fire_freeze)
            }
        }
    }
}
