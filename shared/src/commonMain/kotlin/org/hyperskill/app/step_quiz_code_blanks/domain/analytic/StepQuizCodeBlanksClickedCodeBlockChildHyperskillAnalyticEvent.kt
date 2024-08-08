package org.hyperskill.app.step_quiz_code_blanks.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlockChild
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents click on the code block child in the code blanks analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "code_block",
 *     "target": "code_block_child",
 *     "context":
 *     {
 *         "code_block": "Blank(isActive=true, suggestions=[Print])",
 *         "code_block_child": "SelectSuggestion(isActive=true, suggestions=[Print], selectedSuggestion=null)"
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepQuizCodeBlanksClickedCodeBlockChildHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    codeBlock: CodeBlock?,
    codeBlockChild: CodeBlockChild?
) : HyperskillAnalyticEvent(
    route = route,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.CODE_BLOCK,
    target = HyperskillAnalyticTarget.CODE_BLOCK_CHILD,
    context = mapOfNotNull(
        StepQuizCodeBlanksAnalyticParams.PARAM_CODE_BLOCK to codeBlock?.analyticRepresentation,
        StepQuizCodeBlanksAnalyticParams.PARAM_CODE_BLOCK_CHILD to codeBlockChild?.analyticRepresentation
    )
)