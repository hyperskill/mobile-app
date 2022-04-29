package org.hyperskill.app.auth.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.core.view.mapper.ResourceProvider

class AuthSocialErrorMapper(
    private val resourceProvider: ResourceProvider
) {
    fun getAuthSocialErrorText(authSocialError: AuthSocialError): String =
        when (authSocialError) {
            AuthSocialError.SOCIAL_SIGNUP_WITH_EXISTING_EMAIL ->
                resourceProvider.getString(SharedResources.strings.auth_social_email_already_used)
            AuthSocialError.SOCIAL_SIGNUP_WITHOUT_EMAIL ->
                resourceProvider.getString(SharedResources.strings.auth_social_email_not_provided_by_social)
            AuthSocialError.CONNECTION_PROBLEM ->
                resourceProvider.getString(SharedResources.strings.connection_error)
        }
}