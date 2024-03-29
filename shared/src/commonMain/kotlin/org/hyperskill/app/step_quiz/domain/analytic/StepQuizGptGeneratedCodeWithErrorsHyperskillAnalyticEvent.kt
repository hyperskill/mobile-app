package org.hyperskill.app.step_quiz.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.step.domain.model.StepRoute
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents an analytic event of the GPT generated code with errors in the code editor.
 *
 * @constructor Create analytic event of the GPT generated code with errors in the code editor.
 *
 * @param stepRoute step route.
 * @param code GPT generated code with errors.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "shown",
 *     "part": "code_editor",
 *     "target": "gpt_generated_code_with_errors",
 *     "context":
 *     {
 *         "step": 1,
 *         "code": "fun main() {\n    println(\"Hello, World!\")\n}"
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepQuizGptGeneratedCodeWithErrorsHyperskillAnalyticEvent(
    stepRoute: StepRoute,
    code: String?
) : HyperskillAnalyticEvent(
    route = stepRoute.analyticRoute,
    action = HyperskillAnalyticAction.SHOWN,
    part = HyperskillAnalyticPart.CODE_EDITOR,
    target = HyperskillAnalyticTarget.GPT_GENERATED_CODE_WITH_ERRORS,
    context = mapOfNotNull(
        PARAM_STEP to stepRoute.stepId,
        CODE to code
    )
) {
    companion object {
        private const val PARAM_STEP = "step"
        private const val CODE = "code"
    }
}