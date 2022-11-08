package org.hyperskill.app.step_quiz_hints.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

class StepQuizHintsHiddenReportHintNoticeHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    isReported: Boolean
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.HIDDEN,
    HyperskillAnalyticPart.REPORT_HINT_NOTICE,
    target = if (isReported) HyperskillAnalyticTarget.YES else HyperskillAnalyticTarget.NO
)