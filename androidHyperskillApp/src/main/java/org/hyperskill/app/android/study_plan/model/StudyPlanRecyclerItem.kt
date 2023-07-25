package org.hyperskill.app.android.study_plan.model

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import org.hyperskill.app.android.R
import ru.nobird.app.core.model.Identifiable

interface StudyPlanRecyclerItem {
    data class Section(
        override val id: Long,
        val title: String,
        @ColorInt val titleTextColor: Int,
        val subtitle: String?,
        val formattedTopicsCount: String?,
        val formattedTimeToComplete: String?,
        val isExpanded: Boolean,
        val isCurrentBadgeShown: Boolean
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
        val subtitle: String?,
        @ColorInt val titleTextColor: Int,
        val progress: Int,
        val formattedProgress: String?,
        val endIcon: Drawable?,
        val isClickable: Boolean,
        val isIdeRequired: Boolean
    ) : StudyPlanRecyclerItem, Identifiable<Long> {
        companion object {
            val activeTextColorRes: Int = org.hyperskill.app.R.color.color_on_surface_alpha_87
            val inactiveTextColorRes: Int = org.hyperskill.app.R.color.color_on_surface_alpha_60

            const val nextActivityIconRes: Int = R.drawable.ic_home_screen_arrow_button
            const val lockedActivityIconRes: Int = R.drawable.ic_activity_locked
            const val skippedActivityIconRes: Int = R.drawable.ic_topic_skipped
            const val completedActivityIconRes: Int = R.drawable.ic_topic_completed
        }
    }
}