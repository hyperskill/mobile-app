package org.hyperskill.app.learning_activities.remote.model

import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import ru.nobird.app.core.model.mapOfNotNull

class LearningActivitiesRequest(
    studyPlanId: Long? = null,
    pageSize: Int? = null,
    page: Int? = null,
    activitiesIds: List<Long> = emptyList(),
    states: Set<LearningActivityState> = setOf(LearningActivityState.TODO),
    types: Set<LearningActivityType>
) {
    companion object {
        private const val STUDY_PLAN = "study_plan"
        private const val PAGE_SIZE = "page_size"
        private const val PAGE = "page"
        private const val STATE = "state"
        private const val TYPES = "types"
        private const val IDS = "ids"
    }

    val parameters: Map<String, Any> =
        mapOfNotNull(
            STUDY_PLAN to studyPlanId,
            PAGE_SIZE to pageSize,
            PAGE to page,
            STATE to states.joinToString(",") { it.value.toString() }.ifEmpty { null },
            TYPES to types.joinToString(",") { it.value.toString() }.ifEmpty { null },
            IDS to activitiesIds.joinToString(separator = ",").ifEmpty { null }
        )
}