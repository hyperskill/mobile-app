package org.hyperskill.app.step_quiz.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.step.domain.model.StepRoute

/**
 * Represents click on the question mark of the GPT generated code with errors fix code mistakes badge.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "gpt_generated_code_with_errors_fix_code_mistakes_badge",
 *     "target": "question_mark"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class StepQuizFixGptGeneratedCodeMistakesBadgeClickedQuestionMarkHyperskillAnalyticEvent(
    stepRoute: StepRoute,
) : HyperskillAnalyticEvent(
    route = stepRoute.analyticRoute,
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.GPT_GENERATED_CODE_WITH_ERRORS_FIX_CODE_MISTAKES_BADGE,
    target = HyperskillAnalyticTarget.QUESTION_MARK
)