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
 *     "part": "full_screen_code_editor",
 *     "context" :
 *     {
 *         "screen_orientation": "portrait"
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class StepQuizCodeFullScreenEditorOrientationChangedAnalyticEvent(
    route: HyperskillAnalyticRoute,
    private val isPortraitOrientation: Boolean
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.ORIENTATION_CHANGED,
    HyperskillAnalyticPart.FULL_SCREEN_CODE_EDITOR
) {
    companion object {
        private const val SCREEN_ORIENTATION_KEY = "screen_orientation"
        private const val PORTRAIT_SCREEN_ORIENTATION_KEY = "portrait"
        private const val LANDSCAPE_SCREEN_ORIENTATION_KEY = "landscape"
    }

    override val params: Map<String, Any>
        get() = super.params +
            mapOf(
                PARAM_CONTEXT to mapOf(
                    SCREEN_ORIENTATION_KEY to when (isPortraitOrientation) {
                        true -> PORTRAIT_SCREEN_ORIENTATION_KEY
                        false -> LANDSCAPE_SCREEN_ORIENTATION_KEY
                    }
                )
            )
}