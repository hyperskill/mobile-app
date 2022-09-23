package org.hyperskill.app.track.domain.analytic

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticAction
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticRoute

class TrackViewedHyperskillAnalyticEvent :
    HyperskillAnalyticEvent(HyperskillAnalyticRoute.Track(), HyperskillAnalyticAction.VIEW)