package org.hyperskill.app.config

import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.debug.domain.model.EndpointConfigType

class BuildKonfig(
    val buildVariant: BuildVariant,
    endpointConfigType: EndpointConfigType
) {
    companion object {
        val IS_INTERNAL_TESTING: Boolean? = InternalBuildKonfig.IS_INTERNAL_TESTING
    }

    val flavor: String =
        when (endpointConfigType) {
            EndpointConfigType.DEV -> InternalBuildKonfig.DEV_FLAVOR
            EndpointConfigType.PRODUCTION -> InternalBuildKonfig.PRODUCTION_FLAVOR
            EndpointConfigType.RELEASE -> InternalBuildKonfig.RELEASE_FLAVOR
        }

    internal val baseUrl: String =
        when (endpointConfigType) {
            EndpointConfigType.DEV -> InternalBuildKonfig.DEV_BASE_URL
            EndpointConfigType.PRODUCTION -> InternalBuildKonfig.PRODUCTION_BASE_URL
            EndpointConfigType.RELEASE -> InternalBuildKonfig.RELEASE_BASE_URL
        }

    internal val host: String =
        when (endpointConfigType) {
            EndpointConfigType.DEV -> InternalBuildKonfig.DEV_HOST
            EndpointConfigType.PRODUCTION -> InternalBuildKonfig.PRODUCTION_HOST
            EndpointConfigType.RELEASE -> InternalBuildKonfig.RELEASE_HOST
        }

    internal val oauthClientId: String =
        when (endpointConfigType) {
            EndpointConfigType.DEV -> InternalBuildKonfig.DEV_OAUTH_CLIENT_ID
            EndpointConfigType.PRODUCTION -> InternalBuildKonfig.PRODUCTION_OAUTH_CLIENT_ID
            EndpointConfigType.RELEASE -> InternalBuildKonfig.RELEASE_OAUTH_CLIENT_ID
        }

    internal val oauthClientSecret: String =
        when (endpointConfigType) {
            EndpointConfigType.DEV -> InternalBuildKonfig.DEV_OAUTH_CLIENT_SECRET
            EndpointConfigType.PRODUCTION -> InternalBuildKonfig.PRODUCTION_OAUTH_CLIENT_SECRET
            EndpointConfigType.RELEASE -> InternalBuildKonfig.RELEASE_OAUTH_CLIENT_SECRET
        }

    internal val redirectUri: String =
        when (endpointConfigType) {
            EndpointConfigType.DEV -> InternalBuildKonfig.DEV_REDIRECT_URI
            EndpointConfigType.PRODUCTION -> InternalBuildKonfig.PRODUCTION_REDIRECT_URI
            EndpointConfigType.RELEASE -> InternalBuildKonfig.RELEASE_REDIRECT_URI
        }

    internal val credentialsClientId: String =
        when (endpointConfigType) {
            EndpointConfigType.DEV -> InternalBuildKonfig.DEV_CREDENTIALS_CLIENT_ID
            EndpointConfigType.PRODUCTION -> InternalBuildKonfig.PRODUCTION_CREDENTIALS_CLIENT_ID
            EndpointConfigType.RELEASE -> InternalBuildKonfig.RELEASE_CREDENTIALS_CLIENT_ID
        }

    internal val credentialsClientSecret: String =
        when (endpointConfigType) {
            EndpointConfigType.DEV -> InternalBuildKonfig.DEV_CREDENTIALS_CLIENT_SECRET
            EndpointConfigType.PRODUCTION -> InternalBuildKonfig.PRODUCTION_CREDENTIALS_CLIENT_SECRET
            EndpointConfigType.RELEASE -> InternalBuildKonfig.RELEASE_CREDENTIALS_CLIENT_SECRET
        }
}