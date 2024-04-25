package org.hyperskill.app.step_completion.domain.analytic

import org.hyperskill.app.analytic.domain.model.apps_flyer.AppsFlyerAnalyticEvent
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents step completion analytic event when a user has completed a topic.
 *
 * @param topicId Completed topic id.
 * @param trackTitle Title of the track.
 * @param trackIsCompleted true if the track is completed, false otherwise.
 */
class StepCompletionTopicCompletedAppsFlyerAnalyticEvent(
    topicId: Long,
    trackTitle: String?,
    trackIsCompleted: Boolean
) : AppsFlyerAnalyticEvent(
    name = "af_topic_completed",
    params = mapOfNotNull(
        StepCompletionHyperskillAnalyticParams.PARAM_TOPIC_ID to topicId,
        StepCompletionHyperskillAnalyticParams.PARAM_TRACK_TITLE to trackTitle,
        StepCompletionHyperskillAnalyticParams.PARAM_TRACK_COMPLETED to if (trackIsCompleted) "yes" else "no"
    )
)