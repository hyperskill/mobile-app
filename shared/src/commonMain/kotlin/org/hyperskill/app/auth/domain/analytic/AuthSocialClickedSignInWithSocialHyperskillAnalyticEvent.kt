package org.hyperskill.app.auth.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.auth.domain.model.SocialAuthProvider

private fun SocialAuthProvider.getAnalyticTarget(): HyperskillAnalyticTarget =
    when (this) {
        SocialAuthProvider.JETBRAINS_ACCOUNT -> HyperskillAnalyticTarget.JETBRAINS_ACCOUNT
        SocialAuthProvider.GOOGLE -> HyperskillAnalyticTarget.GOOGLE
        SocialAuthProvider.GITHUB -> HyperskillAnalyticTarget.GITHUB
        SocialAuthProvider.APPLE -> HyperskillAnalyticTarget.APPLE
    }

class AuthSocialClickedSignInWithSocialHyperskillAnalyticEvent(
    socialAuthProvider: SocialAuthProvider
) : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Login(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    socialAuthProvider.getAnalyticTarget()
)