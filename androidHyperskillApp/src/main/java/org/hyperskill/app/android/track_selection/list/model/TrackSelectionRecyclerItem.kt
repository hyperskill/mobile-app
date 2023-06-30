package org.hyperskill.app.android.track_selection.list.model

import androidx.annotation.ColorInt
import ru.nobird.app.core.model.Identifiable

sealed interface TrackSelectionRecyclerItem {
    object Header : TrackSelectionRecyclerItem

    object TrackLoading : TrackSelectionRecyclerItem

    data class Track(
        override val id: Long,
        val title: String,
        val timeToComplete: String?,
        val imageSource: String?,
        val rating: String,
        val isBeta: Boolean,
        val isCompleted: Boolean,
        val isSelected: Boolean,
        @ColorInt val strokeColor: Int,
        val progress: Int
    ) : TrackSelectionRecyclerItem, Identifiable<Long> {
        val areTagsVisible: Boolean
            get() = isBeta || isCompleted || isSelected

        val isClickable: Boolean
            get() = !isSelected
    }
}