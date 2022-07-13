package org.hyperskill.app.track.routing

import org.hyperskill.app.config.BuildKonfig

class TrackRedirectLinkBuilder {
    companion object {
        fun getTrackLink(id: Long): String =
            BuildKonfig.BASE_URL + "tracks/$id"
    }
}