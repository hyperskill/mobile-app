package org.hyperskill.app.auth.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.auth.domain.model.AuthSocialError
import org.hyperskill.app.core.view.mapper.ResourceProvider

class AuthSocialErrorMapper(
    private val resourceProvider: ResourceProvider
) {
    fun getAuthSocialErrorText(authSocialError: AuthSocialError): String =
        when (authSocialError) {
            is AuthSocialError.ServerError -> authSocialError.errorText
            AuthSocialError.ConnectionProblem ->
                resourceProvider.getString(SharedResources.strings.connection_error)
        }
}