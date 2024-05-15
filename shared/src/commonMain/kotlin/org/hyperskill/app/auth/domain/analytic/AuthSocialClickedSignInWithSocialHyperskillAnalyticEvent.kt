package org.hyperskill.app.auth.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.auth.domain.model.SocialAuthProvider

/**
 * Represents a click on sign in with social auth provider button analytic event.
 *
 * JSON payload:
 * ```
 * {
 *     "route": "/login",
 *     "action": "click",
 *     "part": "main",
 *     "target": "jetbrains_account / google / github / apple"
 * }
 * ```
 * @see HyperskillAnalyticEvent
 */
class AuthSocialClickedSignInWithSocialHyperskillAnalyticEvent(
    socialAuthProvider: SocialAuthProvider
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Login(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    socialAuthProvider.getAnalyticTarget()
)