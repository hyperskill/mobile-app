package org.hyperskill.app.auth.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget
import org.hyperskill.app.auth.domain.model.SocialAuthProvider

fun SocialAuthProvider.getAnalyticTarget(): HyperskillAnalyticTarget =
    when (this) {
        SocialAuthProvider.JETBRAINS_ACCOUNT -> HyperskillAnalyticTarget.JETBRAINS_ACCOUNT
        SocialAuthProvider.GOOGLE -> HyperskillAnalyticTarget.GOOGLE
        SocialAuthProvider.GITHUB -> HyperskillAnalyticTarget.GITHUB
        SocialAuthProvider.APPLE -> HyperskillAnalyticTarget.APPLE
    }