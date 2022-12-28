package org.hyperskill.app.auth.view.mapper

import org.hyperskill.app.auth.domain.model.SocialAuthProvider
import org.hyperskill.app.network.domain.model.NetworkEndpointConfigInfo

object SocialAuthProviderRequestURLBuilder {
    fun build(provider: SocialAuthProvider, networkEndpointConfigInfo: NetworkEndpointConfigInfo): String {
        val builder = StringBuilder()

        builder
            .append(networkEndpointConfigInfo.baseUrl)
            .append("accounts/")
            .append(provider.title)
            .append("/login?next=%2Foauth2%2Fauthorize%2F%3Fclient_id%3D")
            .append(networkEndpointConfigInfo.oauthClientId)
            .append("%26response_type%3Dcode")

        return builder.toString()
    }
}