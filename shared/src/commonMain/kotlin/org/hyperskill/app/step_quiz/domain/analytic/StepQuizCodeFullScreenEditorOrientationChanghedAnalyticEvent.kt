package org.hyperskill.app.step_quiz.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

/**
 * Represents orientation change analytic event of the full-screen code editor.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "orientation_changed",
 *     "part": "problems_limit_reached_modal",
 *     "context" :
 *     {
 *         "screen_orientation": "portrait_screen_orientation"
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StepQuizCodeFullScreenEditorOrientationChanghedAnalyticEvent(
    route: HyperskillAnalyticRoute,
    private val isPortraitOrientation: Boolean
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.ORIENTATION_CHANGED,
    HyperskillAnalyticPart.FULL_SCREEN_CODE_EDITOR
) {
    companion object {
        private const val SCREEN_ORIENTATION_KEY = "screen_orientation"
        private const val PORTRAIT_SCREEN_ORIENTATION_KEY = "portrait_screen_orientation"
        private const val LANDSCAPE_SCREEN_ORIENTATION_KEY = "landscape_screen_orientation"
    }

    override val params: Map<String, Any>
        get() = super.params +
            mapOf(
                PARAM_CONTEXT to mapOf(
                    SCREEN_ORIENTATION_KEY to when(isPortraitOrientation) {
                        true -> PORTRAIT_SCREEN_ORIENTATION_KEY
                        false -> LANDSCAPE_SCREEN_ORIENTATION_KEY
                    }
                )
        )
}