package org.hyperskill.app.stage_implement.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the "Go to homescreen" button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/projects/{PROJECT_ID}/stages/{STAGE_ID}/implement",
 *     "action": "click",
 *     "part": "stage_implement_unsupported_modal",
 *     "target": "go_to_home_screen"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StageImplementUnsupportedModalClickedGoToHomeScreenHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.STAGE_IMPLEMENT_UNSUPPORTED_MODAL,
    HyperskillAnalyticTarget.GO_TO_HOME_SCREEN
)