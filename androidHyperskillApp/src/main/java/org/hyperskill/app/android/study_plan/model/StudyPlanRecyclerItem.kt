package org.hyperskill.app.android.study_plan.model

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
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

    data class SectionLoading(
        val index: Int
    ) : StudyPlanRecyclerItem, Identifiable<String> {
        override val id: String
            get() = "sections-list-loading-item-$index"
    }

    object PaywallBanner : StudyPlanRecyclerItem, Identifiable<String> {
        override val id: String
            get() = "study-plan-paywall-banner"
    }

    data class ActivityLoading(
        val sectionId: Long,
        val index: Int
    ) : StudyPlanRecyclerItem, Identifiable<String> {
        override val id: String
            get() = "activity-loading-item-$sectionId-$index"
    }

    data class ActivitiesError(val sectionId: Long) : StudyPlanRecyclerItem, Identifiable<String> {
        override val id: String
            get() = "section-content-error-$sectionId"
    }

    data class Activity(
        override val id: Long,
        val sectionId: Long,
        val title: String,
        val subtitle: String?,
        @ColorInt val titleTextColor: Int,
        val progress: Int,
        val formattedProgress: String?,
        val endIcon: Drawable?,
        val isIdeRequired: Boolean,
    ) : StudyPlanRecyclerItem, Identifiable<Long>
}