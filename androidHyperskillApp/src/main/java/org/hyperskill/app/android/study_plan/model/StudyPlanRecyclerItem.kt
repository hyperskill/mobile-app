package org.hyperskill.app.android.study_plan.model

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import ru.nobird.app.core.model.Identifiable

sealed interface StudyPlanRecyclerItem {
    data class Section(
        override val id: Long,
        val title: String,
        @ColorInt val titleTextColor: Int,
        val subtitle: String?,
        val formattedTopicsCount: String?,
        val formattedTimeToComplete: String?,
        val isExpanded: Boolean
    ) : StudyPlanRecyclerItem, Identifiable<Long>

    object SectionLoading : StudyPlanRecyclerItem

    data class ActivityLoading(
        val sectionId: Long,
        val index: Int
    ) : StudyPlanRecyclerItem, Identifiable<String> {
        override val id: String
            get() = "$sectionId-$index"
    }

    data class ActivitiesError(val sectionId: Long) : StudyPlanRecyclerItem, Identifiable<String> {
        override val id: String
            get() = "section-content-error-$sectionId"
    }

    data class Activity(
        override val id: Long,
        val title: String,
        @ColorInt val titleTextColor: Int,
        val progress: Float?,
        val formattedProgress: String?,
        val endIcon: Drawable?,
        val isClickable: Boolean,
        val isIdeRequired: Boolean
    ) : StudyPlanRecyclerItem, Identifiable<Long>
}