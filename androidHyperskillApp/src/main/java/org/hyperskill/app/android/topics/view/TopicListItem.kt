package org.hyperskill.app.android.topics.view

import androidx.annotation.DrawableRes
import org.hyperskill.app.topics.domain.model.Topic as DomainTopic
import org.hyperskill.app.android.R
import org.hyperskill.app.topics.domain.model.completenessPercentage
import kotlin.math.roundToInt

sealed interface TopicListItem {
    data class Topic(
        val id: Long,
        val title: String,
        val completenessText: String?,
        @DrawableRes val completenessDrawable: Int,
        val completenessPercentage: Float
    ) : TopicListItem

    object LoadingPlaceholder : TopicListItem

    companion object {
        fun fromDomainTopic(topic: DomainTopic): Topic {
            val progress = topic.progress
            return Topic(
                id = topic.id,
                title = topic.title,
                completenessText = when {
                    progress == null || progress.isSkipped || progress.isCompleted -> null
                    else -> "${progress.completenessPercentage.roundToInt()}%"
                },
                completenessDrawable = when {
                    progress == null -> 0
                    progress.isSkipped -> R.drawable.ic_topic_skipped
                    progress.isCompleted -> R.drawable.ic_topic_completed
                    else -> 0
                },
                completenessPercentage = if (progress != null) progress.completenessPercentage / 100 else 0f
            )
        }
    }
}