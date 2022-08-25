package org.hyperskill.app.profile.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

class ProfileViewedHyperskillAnalyticEvent :
    HyperskillAnalyticEvent(HyperskillAnalyticRoute.Profile(), HyperskillAnalyticAction.VIEW)