package org.hyperskill.app.auth.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticPart
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticTarget

class AuthCredentialsClickedSignInHyperskillAnalyticEvent : HyperskillAnalyticEvent(
    HyperskillAnalyticRoute.Login.Password(),
    HyperskillAnalyticAction.CLICK,
    HyperskillAnalyticPart.MAIN,
    HyperskillAnalyticTarget.LOG_IN
)