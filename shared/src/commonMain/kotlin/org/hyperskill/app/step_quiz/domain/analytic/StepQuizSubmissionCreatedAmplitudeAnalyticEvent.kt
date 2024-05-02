package org.hyperskill.app.step_quiz.domain.analytic

import org.hyperskill.app.analytic.domain.model.amplitude.AmplitudeAnalyticEvent
import org.hyperskill.app.submissions.domain.model.SubmissionStatus
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents a successful submission creation analytic event.
 *
 * @param stepId Step id for which a submission was created.
 * @param blockName Step block name.
 * @param submissionStatus Result submission status after receiving a response from backend.
 */
class StepQuizSubmissionCreatedAmplitudeAnalyticEvent(
    stepId: Long,
    blockName: String,
    submissionStatus: SubmissionStatus?
) : AmplitudeAnalyticEvent(
    name = "submission_created",
    params = mapOfNotNull(
        StepQuizAnalyticParams.BLOCK_NAME_PARAM to blockName,
        StepQuizAnalyticParams.STEP_ID_PARAM to stepId,
        StepQuizAnalyticParams.SUBMISSION_STATUS_PARAM to submissionStatus?.name
    )
)