package org.hyperskill.app.android.track_selection.list.adapter

import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import coil.ImageLoader
import coil.load
import coil.size.Scale
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.ItemTrackBinding
import org.hyperskill.app.android.track_selection.list.model.TrackSelectionRecyclerItem
import ru.nobird.android.ui.adapterdelegates.AdapterDelegate
import ru.nobird.android.ui.adapterdelegates.DelegateViewHolder
import ru.nobird.android.view.base.ui.extension.setTextIfChanged

class TrackAdapterDelegate(
    private val imageLoader: ImageLoader,
    private val onTrackClicked: (Long) -> Unit
) : AdapterDelegate<TrackSelectionRecyclerItem, DelegateViewHolder<TrackSelectionRecyclerItem>>() {

    override fun isForViewType(position: Int, data: TrackSelectionRecyclerItem): Boolean =
        data is TrackSelectionRecyclerItem.Track

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder =
        ViewHolder(createView(parent, R.layout.item_track))

    inner class ViewHolder(rootView: View) : DelegateViewHolder<TrackSelectionRecyclerItem>(rootView) {

        val viewBinding = ItemTrackBinding.bind(itemView)

        private val tagsTopMargin = context.resources.getDimensionPixelOffset(
            R.dimen.track_selection_list_item_tags_top_margin
        )

        init {
            itemView.setOnClickListener {
                val trackId = (itemData as? TrackSelectionRecyclerItem.Track)?.id
                if (trackId != null) {
                    onTrackClicked(trackId)
                }
            }
        }

        override fun onBind(data: TrackSelectionRecyclerItem) {
            data as TrackSelectionRecyclerItem.Track

            with(viewBinding) {
                root.isClickable = data.isClickable
                root.strokeColor = data.strokeColor

                trackNameTextView.setTextIfChanged(data.title)
                trackRatingTextView.setTextIfChanged(data.rating)

                with(trackTimeToCompleteTextView) {
                    val timeToComplete = data.timeToComplete
                    isVisible = timeToComplete != null
                    if (timeToComplete != null) {
                        setTextIfChanged(timeToComplete)
                    }
                }

                trackIconImageView.load(data.imageSource, imageLoader) {
                    scale(Scale.FILL)
                    crossfade(true)
                }

                trackSelectedBadge.root.isVisible = data.isSelected
                trackBetaBadge.root.isVisible = data.isBeta
                trackCompletedBadge.root.isVisible = data.isCompleted

                // Remove tags top padding when none of theme are shown.
                // If set some value to trackTags.isVisible, then all the tags will have that visibility.
                trackTags.paddingTop = if (data.areTagsVisible) tagsTopMargin else 0
            }
        }
    }
}