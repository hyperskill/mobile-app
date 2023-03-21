package org.hyperskill.app.android.study_plan.model

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

sealed interface StudyPlanRecyclerItem {
    data class Section(
        val id: Long,
        val title: String,
        @ColorInt val titleTextColor: Int,
        val subtitle: String?,
        val formattedTopicsCount: String?,
        val formattedTimeToComplete: String?,
        val isExpanded: Boolean
    ) : StudyPlanRecyclerItem

    object SectionLoading : StudyPlanRecyclerItem

    object ActivitiesLoading : StudyPlanRecyclerItem

    data class Activity(
        val id: Long,
        val title: String,
        @ColorInt val titleTextColor: Int,
        val progress: Float?,
        val formattedProgress: String?,
        val endIcon: Drawable?,
        val isClickable: Boolean
    ) : StudyPlanRecyclerItem
}