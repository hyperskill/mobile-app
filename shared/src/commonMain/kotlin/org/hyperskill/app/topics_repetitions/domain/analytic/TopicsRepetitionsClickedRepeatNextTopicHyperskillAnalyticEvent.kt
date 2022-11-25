package org.hyperskill.app.topics_repetitions.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

class TopicsRepetitionsClickedRepeatNextTopicHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Repeat(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.REPEAT_NEXT_TOPIC,
    HyperskillAnalyticTarget.CONTINUE
)