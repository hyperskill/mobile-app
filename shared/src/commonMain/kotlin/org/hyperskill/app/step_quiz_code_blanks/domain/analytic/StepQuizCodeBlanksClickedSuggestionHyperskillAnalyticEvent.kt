package org.hyperskill.app.step_quiz_code_blanks.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.step_quiz_code_blanks.domain.model.CodeBlock
import org.hyperskill.app.step_quiz_code_blanks.domain.model.Suggestion
import ru.nobird.app.core.model.mapOfNotNull

/**
 * Represents click on the code block suggestion in the code blanks analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "code_blanks",
 *     "target": "code_block_suggestion",
 *     "context":
 *     {
 *         "code_block": "Blank(isActive=true, suggestions=[Print])",
 *         "suggestion": "ConstantString(text='suggestion')"
 *     }
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepQuizCodeBlanksClickedSuggestionHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    codeBlock: CodeBlock?,
    suggestion: Suggestion
) : HyperskillAnalyticEvent(
    route = route,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.CODE_BLANKS,
    target = HyperskillAnalyticTarget.CODE_BLOCK_SUGGESTION,
    context = mapOfNotNull(
        StepQuizCodeBlanksAnalyticParams.PARAM_CODE_BLOCK to codeBlock?.analyticRepresentation,
        StepQuizCodeBlanksAnalyticParams.PARAM_SUGGESTION to suggestion.analyticRepresentation
    )
)