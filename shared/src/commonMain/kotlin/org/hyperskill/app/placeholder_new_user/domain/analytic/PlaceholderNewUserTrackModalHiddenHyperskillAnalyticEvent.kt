package org.hyperskill.app.placeholder_new_user.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

class PlaceholderNewUserTrackModalHiddenHyperskillAnalyticEvent(val trackId: Long) :
    HyperskillAnalyticEvent(
        HyperskillAnalyticRoute.Register(),
        HyperskillAnalyticAction.HIDDEN,
        HyperskillAnalyticPart.TRACK_MODAL,
        HyperskillAnalyticTarget.CLOSE
    ) {
    companion object {
        private const val PARAM_TRACK_ID = "track_id"
    }

    override val params: Map<String, Any>
        get() = super.params +
            mapOf(PARAM_CONTEXT to mapOf(PARAM_TRACK_ID to trackId))
}