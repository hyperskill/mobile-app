package org.hyperskill.app.step_quiz_hints.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a hidden analytic event of the prompt to report hint.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "hidden",
 *     "part": "report_hint_notice",
 *     "target": "yes / no"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StepQuizHintsHiddenReportHintNoticeHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    isReported: Boolean
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.REPORT_HINT_NOTICE,
    target = if (isReported) HyperskillAnalyticTarget.YES else HyperskillAnalyticTarget.NO
)