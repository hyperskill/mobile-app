package org.hyperskill.app.request_review.modal.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

/**
 * Represents a common click analytic event for the request review modal.
 *
 * Click on the "Yes" button:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "request_review_modal",
 *     "target": "yes"
 * }
 * ```
 *
 * Click on the "No" button:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "request_review_modal",
 *     "target": "no"
 * }
 * ```
 *
 * Click on the "Write a request" button:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "request_review_modal",
 *     "target": "write_a_request"
 * }
 * ```
 *
 * Click on the "Maybe later" button:
 * ```
 * {
 *     "route": "/learn/step/1",
 *     "action": "click",
 *     "part": "request_review_modal",
 *     "target": "maybe_later"
 * }
 * ```
 *
 * @see HyperskillAnalyticEvent
 */
class RequestReviewModalClickHyperskillAnalyticEvent(
    route: HyperskillAnalyticRoute,
    target: HyperskillAnalyticTarget
) : HyperskillAnalyticEvent(
    route,
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.REQUEST_REVIEW_MODAL,
    target
)