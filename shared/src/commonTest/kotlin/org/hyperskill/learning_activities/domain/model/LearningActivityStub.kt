package org.hyperskill.learning_activities.domain.model

import org.hyperskill.app.core.domain.model.ContentType
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType

fun LearningActivity.Companion.stub(
    id: Long = 0,
    state: LearningActivityState = LearningActivityState.TODO,
    targetId: Long = 0L,
    type: LearningActivityType = LearningActivityType.LEARN_TOPIC,
    targetType: ContentType = ContentType.STEP,
    title: String = "",
    description: String? = null,
    isIdeRequired: Boolean = false,
    progress: Float = 0f,
    topicId: Long? = null
): LearningActivity =
    LearningActivity(
        id = id,
        stateValue = state.value,
        targetId = targetId,
        typeValue = type.value,
        targetType = targetType,
        title = title,
        description = description,
        isIdeRequired = isIdeRequired,
        progress = progress,
        topicId = topicId
    )