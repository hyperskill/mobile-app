package org.hyperskill.app.comments.screen.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.reactions.domain.model.ReactionType
import org.hyperskill.app.step.domain.model.StepRoute

/**
 * Represents click on the reaction button in the comments screen analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/learn/step/1#comment",
 *     "action": "click",
 *     "part": "main",
 *     "target": "comment",
 *     "context": {
 *         "comment_id": "1",
 *         "reaction_type": ":smile:"
 *     }
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class CommentsScreenClickedReactionHyperskillAnalyticEvent(
    stepRoute: StepRoute,
    commentId: Long,
    reactionType: ReactionType
) : HyperskillAnalyticEvent(
    route = HyperskillAnalyticRoute.Comment(stepRoute),
    action = HyperskillAnalyticAction.CLICK,
    part = HyperskillAnalyticPart.MAIN,
    target = HyperskillAnalyticTarget.COMMENT,
    context = mapOf(
        COMMENT_ID_KEY to commentId.toString(),
        REACTION_TYPE_KEY to reactionType.name.lowercase()
    )
) {
    companion object {
        private const val COMMENT_ID_KEY = "comment_id"
        private const val REACTION_TYPE_KEY = "reaction_type"
    }
}