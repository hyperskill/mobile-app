package org.hyperskill.app.step_quiz.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents click on the code input accessory button.
 *
 * The custom accessory view is displayed when the text view becomes the first responder (over keyboard).
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "code_editor",
 *     "target": "code_input_accessory_button",
 *     "context":
 *     {
 *         "symbol": "tab"
 *     }
 * }
 * ```
 *
 * @property symbol Tab, hide keyboard and other actions are handled by the accessory view.
 *
 * @see HyperskillAnalyticEvent
 */
class StepQuizCodeEditorClickedInputAccessoryButtonHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    val symbol: String
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.CODE_EDITOR,
    HyperskillAnalyticTarget.CODE_INPUT_ACCESSORY_BUTTON
) {
    companion object {
        private const val SYMBOL = "symbol"
    }

    override val params: Map<String, Any>
        get() = super.params +
            mapOf(
                PARAM_CONTEXT to mapOf(
                    SYMBOL to symbol
                )
            )
}