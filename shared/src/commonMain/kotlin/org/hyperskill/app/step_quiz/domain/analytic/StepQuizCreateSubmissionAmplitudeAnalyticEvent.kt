package org.hyperskill.app.step_quiz.domain.analytic

import org.hyperskill.app.analytic.domain.model.amplitude.AmplitudeAnalyticEvent

/**
 * Represents a beginning of a submission creation
 *
 * @param stepId Step id for which a submission was created.
 * @param blockName Step block name.
 */
class StepQuizCreateSubmissionAmplitudeAnalyticEvent(
    stepId: Long,
    blockName: String
) : AmplitudeAnalyticEvent(
    name = "create_submission",
    params = mapOf(
        StepQuizAnalyticParams.BLOCK_NAME_PARAM to blockName,
        StepQuizAnalyticParams.STEP_ID_PARAM to stepId
    )
)