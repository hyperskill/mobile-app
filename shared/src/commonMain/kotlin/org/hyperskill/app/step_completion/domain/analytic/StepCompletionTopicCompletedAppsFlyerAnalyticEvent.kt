package org.hyperskill.app.step_completion.domain.analytic

import org.hyperskill.app.analytic.domain.model.apps_flyer.AppsFlyerAnalyticEvent
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents step completion analytic event when user has completed a topic.
 *
 * @param topicId Completed topic id.
 * @param trackTitle Title of the track.
 * @param trackIsCompleted true if track is completed, false otherwise.
 */
class StepCompletionTopicCompletedAppsFlyerAnalyticEvent(
    topicId: Long,
    trackTitle: String?,
    trackIsCompleted: Boolean
) : AppsFlyerAnalyticEvent(
    name = "af_topic_completed",
    params = mapOfNotNull(
        PARAM_TOPIC_ID to topicId,
        PARAM_TRACK_TITLE to trackTitle,
        PARAM_TRACK_COMPLETED to if (trackIsCompleted) "yes" else "no"
    )
) {
    companion object {
        private const val PARAM_TOPIC_ID = "topic_id"
        private const val PARAM_TRACK_TITLE = "track_name"
        private const val PARAM_TRACK_COMPLETED = "track_completed"
    }
}