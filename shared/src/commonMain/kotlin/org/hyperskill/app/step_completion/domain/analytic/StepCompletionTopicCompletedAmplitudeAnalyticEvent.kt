package org.hyperskill.app.step_completion.domain.analytic

import org.hyperskill.app.analytic.domain.model.amplitude.AmplitudeAnalyticEvent
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents step completion analytic event when a user has completed a topic.
 *
 * @param topicId Completed topic id.
 * @param trackTitle Title of the track.
 */
class StepCompletionTopicCompletedAmplitudeAnalyticEvent(
    topicId: Long,
    trackTitle: String?
) : AmplitudeAnalyticEvent(
    name = "complete_topic",
    params = mapOfNotNull(
        StepCompletionHyperskillAnalyticParams.PARAM_TOPIC_ID to topicId,
        StepCompletionHyperskillAnalyticParams.PARAM_TRACK_TITLE to trackTitle
    )
)