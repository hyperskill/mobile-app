package org.hyperskill.app.android.projects_selection.model

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

sealed interface ProjectSelectionRecyclerItem {

    data class Header(
        val title: String,
        val trackIcon: String?
    ) : ProjectSelectionRecyclerItem

    data class SectionTitle(
        val title: String,
        val subtitle: String?
    ) : ProjectSelectionRecyclerItem

    data class Project(
        val id: Long,
        val title: String,
        @ColorInt val strokeColor: Int,
        val formattedRating: String,
        val levelText: String?,
        val levelIcon: Drawable?,
        val formattedTimeToComplete: String?,
        val isSelected: Boolean,
        val isGraduate: Boolean,
        val isBestRated: Boolean,
        val isIdeRequired: Boolean,
        val isFastestToComplete: Boolean,
        val isCompleted: Boolean,
        val isBeta: Boolean
    ) : ProjectSelectionRecyclerItem {
        val areTagsVisible: Boolean
            get() = isSelected || isBestRated || isIdeRequired || isFastestToComplete || isBeta || isCompleted

        val isClickable: Boolean
            get() = !isSelected
    }
}