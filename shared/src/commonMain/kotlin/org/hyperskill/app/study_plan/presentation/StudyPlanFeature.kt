package org.hyperskill.app.study_plan.presentation

import org.hyperskill.app.study_plan.domain.model.LearningActivity
import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection

object StudyPlanFeature {

    internal data class State(
        val studyPlan: StudyPlan? = null,

        val studyPlanSections: Map<Long, StudyPlanSectionInfo> = emptyMap(),

        /**
         * Describes status of sections loading, including [studyPlan] loading
         */
        val sectionsStatus: ContentStatus = ContentStatus.IDLE,

        /**
         * Map of sections ids to section's activities
         */
        val activities: Map<Long, Set<LearningActivity>> = mapOf(),

        /**
         * Describes status of section's activities loading
         * Key is a section id
         * Value is a status of section content loading
         */
        val sectionsContentStatuses: Map<Long, ContentStatus> = mapOf()
    )

    enum class ContentStatus {
        IDLE,
        LOADING,
        ERROR,
        LOADED
    }

    data class StudyPlanSectionInfo(
        val studyPlanSection: StudyPlanSection,
        val isExpanded: Boolean
    )

    sealed interface Message {
        object Initialize : Message
    }

    internal sealed interface StudyPlanFetchResult : Message {
        data class Success(val studyPlan: StudyPlan) : StudyPlanFetchResult

        object Failed : StudyPlanFetchResult
    }

    internal sealed interface SectionsFetchResult : Message {
        data class Success(val sections: List<StudyPlanSection>) : SectionsFetchResult

        object Failed : SectionsFetchResult
    }

    internal sealed interface LearningActivitiesFetchResult : Message {
        data class Success(
            val sectionId: Long,
            val activities: List<LearningActivity>
        ) : LearningActivitiesFetchResult

        object Failed : LearningActivitiesFetchResult
    }

    sealed interface Action {
        object FetchStudyPlan : Action

        data class FetchSections(val sectionsIds: List<Long>) : Action

        data class FetchActivities(
            val sectionId: Long,
            val activitiesIds: List<Long>
        ) : Action
    }
}