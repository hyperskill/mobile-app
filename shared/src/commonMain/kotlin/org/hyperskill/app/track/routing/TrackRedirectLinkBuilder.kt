package org.hyperskill.app.track.routing

import org.hyperskill.app.config.BuildKonfig

class TrackRedirectLinkBuilder {
    companion object {
        fun getStudyPlanLink(): String =
            BuildKonfig.BASE_URL + "study-plan"
    }
}