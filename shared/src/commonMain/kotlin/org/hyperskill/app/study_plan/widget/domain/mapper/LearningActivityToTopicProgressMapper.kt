package org.hyperskill.app.study_plan.widget.domain.mapper

import org.hyperskill.app.learning_activities.domain.model.LearningActivity
import org.hyperskill.app.topics.domain.model.TopicProgress

internal object LearningActivityToTopicProgressMapper {
    fun map(activity: LearningActivity): TopicProgress? =
        activity.topicId?.let { topicId ->
            TopicProgress(
                vid = "topic-$topicId",
                isCompleted = activity.progress == 1f,
                isSkipped = false,
                capacity = activity.progress
            )
        }
}