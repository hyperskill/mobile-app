package org.hyperskill.app.step_quiz.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

class StepQuizClickedSendHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.SEND
)