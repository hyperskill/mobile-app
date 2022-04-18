package org.hyperskill.app.auth.view.mapper

import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.config.BuildKonfig

object SocialAuthProviderRequestURLBuilder {
    fun build(provider: SocialAuthProvider): String {
        val builder = StringBuilder()

        builder
            .append("https://")
            .append(BuildKonfig.HOST)
            .append("/accounts/")
            .append(SocialAuthProviderNameMapper.getName(provider))
            .append("/login?next=%2Foauth2%2Fauthorize%2F%3Fclient_id%3D")
            .append(BuildKonfig.OAUTH_CLIENT_ID)
            .append("%26response_type%3Dcode")

        return builder.toString()
    }
}