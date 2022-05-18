package org.hyperskill.app.auth.view.mapper

import org.hyperskill.app.SharedResources
import org.hyperskill.app.auth.domain.model.AuthCredentialsError
import org.hyperskill.app.core.view.mapper.ResourceProvider

class AuthCredentialsErrorMapper(
    private val resourceProvider: ResourceProvider
) {
    fun getAuthCredentialsErrorText(authCredentialsError: AuthCredentialsError): String =
        when (authCredentialsError) {
            AuthCredentialsError.ERROR_CREDENTIALS_AUTH ->
                resourceProvider.getString(SharedResources.strings.auth_credentials_error_text)
            AuthCredentialsError.ERROR_EMAIL_EMPTY ->
                resourceProvider.getString(SharedResources.strings.auth_credentials_email_empty)
            AuthCredentialsError.ERROR_PASSWORD_EMPTY ->
                resourceProvider.getString(SharedResources.strings.auth_credentials_password_empty)
            AuthCredentialsError.CONNECTION_PROBLEM ->
                resourceProvider.getString(SharedResources.strings.connection_error)
        }
}