package org.hyperskill.app.placeholder_new_user.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

class PlaceholderNewUserViewedHyperskillAnalyticEvent :
    HyperskillAnalyticEvent(HyperskillAnalyticRoute.Register(), HyperskillAnalyticAction.VIEW)