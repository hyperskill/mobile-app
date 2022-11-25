package org.hyperskill.app.home.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

class HomeClickedTopicsRepetitionsCardHyperskillAnalyticEvent(
    val isCompleted: Boolean
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Home(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.TOPICS_REPETITIONS_CARD,
    HyperskillAnalyticTarget.CONTINUE
) {
    companion object {
        private const val PARAM_IS_COMPLETED = "is_completed"
    }

    override val params: Map<String, Any>
        get() = super.params + mapOf(PARAM_CONTEXT to mapOf(PARAM_IS_COMPLETED to isCompleted))
}