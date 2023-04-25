package org.hyperskill.app.study_plan.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a hidden analytic event of the stage implement unsupported bottom sheet.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/study-plan",
 *     "action": "hidden",
 *     "part": "stage_implement_unsupported_modal",
 *     "target": "close"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StudyPlanStageImplementUnsupportedModalHiddenHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.StudyPlan(),
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.STAGE_IMPLEMENT_UNSUPPORTED_MODAL,
    HyperskillAnalyticTarget.CLOSE
)