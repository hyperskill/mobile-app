package org.hyperskill.app.application_shortcuts.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the home screen quick action analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "SpringBoard",
 *     "action": "click",
 *     "part": "main",
 *     "target": "home_screen_quick_action",
 *     "context":
 *     {
 *         "type": "org.hyperskill.App.SendFeedback"
 *     }
 * }
 * ```
 *
 * @property shortcutItemIdentifier The identifier of the clicked quick action.
 * @see HyperskillAnalyticEvent
 */
class ApplicationShortcutItemClickedHyperskillAnalyticEvent(
    private val shortcutItemIdentifier: String
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.IosSpringBoard(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.HOME_SCREEN_QUICK_ACTION
) {
    companion object {
        private const val PARAM_TYPE = "type"
    }

    override val params: Map<String, Any>
        get() = super.params + mapOf(
            PARAM_CONTEXT to mapOf(
                PARAM_TYPE to shortcutItemIdentifier
            )
        )
}