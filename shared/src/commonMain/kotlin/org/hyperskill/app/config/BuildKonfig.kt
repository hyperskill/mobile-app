package org.hyperskill.app.config

import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.debug.domain.model.EndpointConfigType

class BuildKonfig(
    val buildVariant: BuildVariant,
    endpointConfigType: EndpointConfigType
) {
    companion object {
        val IS_INTERNAL_TESTING: Boolean? = InternalBuildKonfig.IS_INTERNAL_TESTING
        const val APPS_FLYER_DEV_KEY: String = InternalBuildKonfig.PRODUCTION_APPS_FLYER_DEV_KEY
        const val AMPLITUDE_DEV_KEY: String = InternalBuildKonfig.PRODUCTION_AMPLITUDE_DEV_KEY
    }

    val flavor: String =
        when (endpointConfigType) {
            EndpointConfigType.PRODUCTION -> InternalBuildKonfig.PRODUCTION_FLAVOR
            EndpointConfigType.MAIN -> InternalBuildKonfig.MAIN_FLAVOR
        }

    internal val baseUrl: String =
        when (endpointConfigType) {
            EndpointConfigType.PRODUCTION -> InternalBuildKonfig.PRODUCTION_BASE_URL
            EndpointConfigType.MAIN -> InternalBuildKonfig.MAIN_BASE_URL
        }

    internal val host: String =
        when (endpointConfigType) {
            EndpointConfigType.PRODUCTION -> InternalBuildKonfig.PRODUCTION_HOST
            EndpointConfigType.MAIN -> InternalBuildKonfig.MAIN_HOST
        }

    internal val oauthClientId: String =
        when (endpointConfigType) {
            EndpointConfigType.PRODUCTION -> InternalBuildKonfig.PRODUCTION_OAUTH_CLIENT_ID
            EndpointConfigType.MAIN -> InternalBuildKonfig.MAIN_OAUTH_CLIENT_ID
        }

    internal val oauthClientSecret: String =
        when (endpointConfigType) {
            EndpointConfigType.PRODUCTION -> InternalBuildKonfig.PRODUCTION_OAUTH_CLIENT_SECRET
            EndpointConfigType.MAIN -> InternalBuildKonfig.MAIN_OAUTH_CLIENT_SECRET
        }

    internal val redirectUri: String =
        when (endpointConfigType) {
            EndpointConfigType.PRODUCTION -> InternalBuildKonfig.PRODUCTION_REDIRECT_URI
            EndpointConfigType.MAIN -> InternalBuildKonfig.MAIN_REDIRECT_URI
        }

    internal val credentialsClientId: String =
        when (endpointConfigType) {
            EndpointConfigType.PRODUCTION -> InternalBuildKonfig.PRODUCTION_CREDENTIALS_CLIENT_ID
            EndpointConfigType.MAIN -> InternalBuildKonfig.MAIN_CREDENTIALS_CLIENT_ID
        }

    internal val credentialsClientSecret: String =
        when (endpointConfigType) {
            EndpointConfigType.PRODUCTION -> InternalBuildKonfig.PRODUCTION_CREDENTIALS_CLIENT_SECRET
            EndpointConfigType.MAIN -> InternalBuildKonfig.MAIN_CREDENTIALS_CLIENT_SECRET
        }
}