package org.hyperskill.app.learning_activities.remote.model

import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType

class NextLearningActivityRequest(
    state: LearningActivityState = LearningActivityState.TODO,
    types: Set<LearningActivityType> = LearningActivityType.supportedTypes()
) {
    val parameters: Map<String, String> =
        mapOf(
            LearningActivitiesRequestParams.STATE to state.value.toString(),
            LearningActivitiesRequestParams.TYPES to types.joinToString(",") { it.value.toString() }
        )
}