package org.hyperskill.app.study_plan.presentation

import org.hyperskill.app.study_plan.domain.model.StudyPlan
import org.hyperskill.app.study_plan.domain.model.StudyPlanSection

object StudyPlanFeature {

    data class State(
        private val studyPlan: StudyPlan? = null,
        private val studyPlanSections: List<StudyPlanSection> = emptyList(),
        private val contentStatus: ContentStatus = ContentStatus.IDLE
    )

    enum class ContentStatus {
        IDLE,
        LOADING,
        ERROR,
        SUCCESS
    }

    sealed interface Message {
        object Initialize : Message
    }

    internal sealed interface StudyPlanFetchResult : Message {
        data class Success(val studyPlan: StudyPlan) : StudyPlanFetchResult

        object Failed : StudyPlanFetchResult
    }

    internal sealed interface StudyPlanSectionsFetchResult : Message {
        data class Success(val sections: List<StudyPlanSection>) : StudyPlanSectionsFetchResult

        object Failed : StudyPlanSectionsFetchResult
    }

    sealed interface Action {
        object FetchStudyPlan : Action

        data class FetchSections(val sectionsIds: List<Long>) : Action
    }
}