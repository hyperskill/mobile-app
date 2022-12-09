package org.hyperskill.app.step_quiz_hints.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a shown analytic event of the prompt to report hint.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "shown",
 *     "part": "notice",
 *     "target": "report_hint_notice"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StepQuizHintsShownReportHintNoticeHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.SHOWN,
    HyperskillAnalyticPart.NOTICE,
    HyperskillAnalyticTarget.REPORT_HINT_NOTICE
)