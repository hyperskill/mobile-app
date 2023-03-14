package org.hyperskill.app.study_plan.presentation

import org.hyperskill.app.study_plan.domain.model.StudyPlan

object StudyPlanFeature {

    data class State(
        private val studyPlan: StudyPlan? = null,
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

    sealed interface Action {
        object FetchStudyPlan : Action
    }
}