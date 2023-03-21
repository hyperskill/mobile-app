package org.hyperskill.app.android.study_plan.model

import androidx.annotation.ColorInt

sealed interface StudyPlanRecyclerItem {
    data class Section(
        val id: Long,
        val title: String,
        @ColorInt val titleTextColor: Int,
        val subtitle: String?,
        val formattedTopicsCount: String?,
        val formattedTimeToComplete: String?
    ) : StudyPlanRecyclerItem
}