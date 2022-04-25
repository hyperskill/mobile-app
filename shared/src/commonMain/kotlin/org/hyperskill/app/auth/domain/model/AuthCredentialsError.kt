package org.hyperskill.app.auth.domain.model

import dev.icerock.moko.resources.StringResource
import org.hyperskill.app.SharedResources

enum class AuthCredentialsError(val stringResource: StringResource) {
    ERROR_CREDENTIALS_AUTH(SharedResources.strings.auth_credentials_error_text),
    ERROR_EMAIL_EMPTY(SharedResources.strings.auth_credentials_email_empty),
    ERROR_PASSWORD_EMPTY(SharedResources.strings.auth_credentials_password_empty),
    CONNECTION_PROBLEM(SharedResources.strings.connection_error)
}