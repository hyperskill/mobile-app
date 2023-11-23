package org.hyperskill.app.learning_activities.remote.model

import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.study_plan.domain.model.StudyPlanSectionType
import ru.nobird.app.core.model.mapOfNotNull

class LearningActivitiesWithSectionsRequest(
    studyPlanSectionTypes: Set<StudyPlanSectionType>,
    learningActivityTypes: Set<LearningActivityType>,
    learningActivityStates: Set<LearningActivityState>
) {
    companion object {
        private const val PARAM_SECTION_TYPES = "section_types"
    }

    val parameters: Map<String, Any> =
        mapOfNotNull(
            PARAM_SECTION_TYPES to
                studyPlanSectionTypes.joinToString(",") { it.value }.ifEmpty { null },
            LearningActivitiesRequestParams.PARAM_TYPES to
                learningActivityTypes.joinToString(",") { it.value.toString() }.ifEmpty { null },
            LearningActivitiesRequestParams.PARAM_STATE to
                learningActivityStates.joinToString(",") { it.value.toString() }.ifEmpty { null }
        )
}