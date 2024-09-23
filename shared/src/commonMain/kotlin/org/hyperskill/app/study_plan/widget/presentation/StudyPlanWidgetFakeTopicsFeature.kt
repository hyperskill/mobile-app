package org.hyperskill.app.study_plan.widget.presentation

import org.hyperskill.app.core.domain.model.ContentType
import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.learning_activities.domain.model.LearningActivityState
import org.hyperskill.app.learning_activities.domain.model.LearningActivityType
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.isFreemium

internal object StudyPlanWidgetFakeTopicsFeature {
    private const val PYTHON_MOBILE_ADOPTED_TRACK_ID = 139L

    private val topicsTitles: List<String> =
        listOf(
            "Fake topic 1",
            "Fake topic 2",
            "Fake topic 3",
            "Fake topic 4",
            "Fake topic 5"
        )

    val topics: List<LearningActivity> by lazy {
        topicsTitles.mapIndexed { index, title ->
            LearningActivity(
                id = -index - 1L,
                stateValue = LearningActivityState.TODO.value,
                targetId = null,
                targetType = ContentType.UNKNOWN,
                typeValue = LearningActivityType.LEARN_TOPIC.value,
                title = title,
                topicId = null
            )
        }
    }
    val topicsIds: Set<Long> by lazy { topics.map { it.id }.toSet() }

    fun isFakeTopicsFeatureAvailable(trackId: Long?, subscription: Subscription): Boolean =
        trackId == PYTHON_MOBILE_ADOPTED_TRACK_ID &&
            (subscription.isFreemium || subscription.type == SubscriptionType.MOBILE_CONTENT_TRIAL)
}