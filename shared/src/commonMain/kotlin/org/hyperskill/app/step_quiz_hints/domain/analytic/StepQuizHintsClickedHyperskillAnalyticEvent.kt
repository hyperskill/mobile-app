package org.hyperskill.app.step_quiz_hints.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

class StepQuizHintsClickedHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    target: HyperskillAnalyticTarget,
    val commentId: Long? = null
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.STEP_HINTS,
    target
) {
    companion object {
        private const val PARAM_COMMENT_ID = "comment_id"
    }

    override val params: Map<String, Any>
        get() = super.params +
            if (commentId != null) mapOf(PARAM_CONTEXT to mapOf(PARAM_COMMENT_ID to commentId))
            else emptyMap()
}