package org.hyperskill.app.onboarding.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

class OnboardingViewedHyperskillAnalyticEvent :
    HyperskillAnalyticEvent(HyperskillAnalyticRoute.Onboarding(), HyperskillAnalyticAction.VIEW)