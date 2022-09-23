package org.hyperskill.app.profile.routing

import org.hyperskill.app.config.BuildKonfig

class ProfileRedirectLinkBuilder {
    companion object {
        fun getProfileLink(id: Long): String =
            BuildKonfig.BASE_URL + "profile/$id"
    }
}