package org.hyperskill.app.study_plan.domain.analytics

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a shown analytic event of the stage implement unsupported bottom sheet.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/study-plan",
 *     "action": "shown",
 *     "part": "modal",
 *     "target": "stage_implement_unsupported_modal"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StageImplementUnsupportedModalShownHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.StudyPlan(),
    HyperskillAnalyticAction.SHOWN,
    HyperskillAnalyticPart.MODAL,
    HyperskillAnalyticTarget.STAGE_IMPLEMENT_UNSUPPORTED_MODAL
)