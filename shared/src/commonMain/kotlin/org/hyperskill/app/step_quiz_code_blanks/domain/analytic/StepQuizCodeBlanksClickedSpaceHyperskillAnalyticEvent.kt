package org.hyperskill.app.step_quiz_code_blanks.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents click on the "Space" button in the code block analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "code_blanks",
 *     "target": "space",
 *     "context":
 *     {
 *         "code_block": "Print(isActive=true, suggestions=[ConstantString(text=suggestion)], selectedSuggestion=null)"
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepQuizCodeBlanksClickedSpaceHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    codeBlock: CodeBlock?
) : HyperskillAnalyticEvent(
    route = route,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.CODE_BLANKS,
    target = HyperskillAnalyticTarget.SPACE,
    context = mapOfNotNull(
        StepQuizCodeBlanksAnalyticParams.PARAM_CODE_BLOCK to codeBlock?.analyticRepresentation
    )
)