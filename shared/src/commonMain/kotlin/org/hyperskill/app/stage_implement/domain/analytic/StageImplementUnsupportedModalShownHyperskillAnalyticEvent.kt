package org.hyperskill.app.stage_implement.domain.analytic

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
 *     "route": "/projects/{PROJECT_ID}/stages/{STAGE_ID}/implement",
 *     "action": "shown",
 *     "part": "modal",
 *     "target": "stage_implement_unsupported_modal"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StageImplementUnsupportedModalShownHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.SHOWN,
    HyperskillAnalyticPart.MODAL,
    HyperskillAnalyticTarget.STAGE_IMPLEMENT_UNSUPPORTED_MODAL
)