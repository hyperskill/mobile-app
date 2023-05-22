package org.hyperskill.app.android.track_selection.delegate

import android.content.Context
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.adapter.decoration.itemDecoration
import org.hyperskill.app.android.track_selection.adapter.TrackAdapterDelegate
import org.hyperskill.app.android.track_selection.model.TrackSelectionRecyclerItem
import org.hyperskill.app.track_selection.list.presentation.TrackSelectionListFeature
import ru.nobird.android.ui.adapterdelegates.dsl.adapterDelegate
import ru.nobird.android.ui.adapters.DefaultDelegateAdapter

class TrackSelectionListDelegate(
    context: Context,
    recyclerView: RecyclerView,
    private val imageLoader: ImageLoader,
    private val onNewMessage: (TrackSelectionListFeature.Message) -> Unit
) {

    private val trackSelectionListAdapterDelegate =
        DefaultDelegateAdapter<TrackSelectionRecyclerItem>().apply {
            addDelegate(headerAdapterDelegate())
            addDelegate(loadingTrackAdapterDelegate())
            addDelegate(
                TrackAdapterDelegate(imageLoader) { trackId ->
                    onNewMessage(TrackSelectionListFeature.Message.TrackClicked(trackId))
                }
            )
        }

    @ColorInt
    private val selectedStrokeColor: Int =
        ContextCompat.getColor(context, org.hyperskill.app.R.color.color_overlay_blue)

    @ColorInt
    private val notSelectedStrokeColor: Int =
        ContextCompat.getColor(context, org.hyperskill.app.R.color.color_on_surface_alpha_9)

    private val itemHorizontalMargin =
        context.resources.getDimensionPixelOffset(R.dimen.screen_horizontal_padding)

    private val lastItemBottomMargin =
        context.resources.getDimensionPixelOffset(
            R.dimen.project_selection_last_item_bottom_margin
        )

    private val firstItemTopMargin =
        context.resources.getDimensionPixelOffset(
            R.dimen.project_selection_list_first_item_top_margin
        )

    private val itemTopMargin =
        context.resources.getDimensionPixelOffset(
            R.dimen.track_selection_list_item_top_margin
        )

    private val loadingItems by lazy(LazyThreadSafetyMode.NONE) {
        buildList {
            add(TrackSelectionRecyclerItem.Header)
            repeat(5) {
                add(TrackSelectionRecyclerItem.TrackLoading)
            }
        }
    }

    init {
        with(recyclerView) {
            this.adapter = trackSelectionListAdapterDelegate
            layoutManager = LinearLayoutManager(context)
            setupDecorations(this, trackSelectionListAdapterDelegate)
            itemAnimator = null
        }
    }

    private fun setupDecorations(
        recyclerView: RecyclerView,
        adapter: DefaultDelegateAdapter<TrackSelectionRecyclerItem>
    ) {
        recyclerView.itemDecoration { position, rect, _ ->
            rect.left = itemHorizontalMargin
            rect.right = itemHorizontalMargin
            if (position == adapter.itemCount - 1) {
                rect.bottom = lastItemBottomMargin
            }
            rect.top = when (adapter.items.getOrNull(position)) {
                is TrackSelectionRecyclerItem.Track,
                is TrackSelectionRecyclerItem.TrackLoading -> {
                    val previousItem = adapter.items.getOrNull(position - 1)
                    if (previousItem is TrackSelectionRecyclerItem.Header) {
                        firstItemTopMargin
                    } else {
                        itemTopMargin
                    }
                }
                else -> 0
            }
        }
    }

    fun render(state: TrackSelectionListFeature.ViewState) {
        when (state) {
            is TrackSelectionListFeature.ViewState.Content ->
                trackSelectionListAdapterDelegate.items = buildList {
                    add(TrackSelectionRecyclerItem.Header)
                    addAll(state.tracks.map(::mapTrack))
                }
            TrackSelectionListFeature.ViewState.Loading ->
                trackSelectionListAdapterDelegate.items = loadingItems
            else -> {
                // no op
            }
        }
    }

    private fun mapTrack(track: TrackSelectionListFeature.ViewState.Track): TrackSelectionRecyclerItem.Track =
        TrackSelectionRecyclerItem.Track(
            id = track.id,
            title = track.title,
            rating = track.rating,
            timeToComplete = track.timeToComplete,
            imageSource = track.imageSource,
            isBeta = track.isBeta,
            isSelected = track.isSelected,
            isCompleted = track.isCompleted,
            strokeColor = if (track.isSelected) selectedStrokeColor else notSelectedStrokeColor
        )

    private fun headerAdapterDelegate() =
        adapterDelegate<TrackSelectionRecyclerItem, TrackSelectionRecyclerItem.Header>(
            R.layout.item_track_selection_list_header
        )

    private fun loadingTrackAdapterDelegate() =
        adapterDelegate<TrackSelectionRecyclerItem, TrackSelectionRecyclerItem.TrackLoading>(
            R.layout.item_track_skeleton
        )
}