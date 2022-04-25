package org.hyperskill.app.auth.domain.model

import dev.icerock.moko.resources.StringResource
import org.hyperskill.app.SharedResources

enum class AuthSocialError(val stringResource: StringResource) {
    SOCIAL_SIGNUP_WITH_EXISTING_EMAIL(SharedResources.strings.auth_social_email_already_used),
    SOCIAL_SIGNUP_WITHOUT_EMAIL(SharedResources.strings.auth_social_email_not_provided_by_social),
    CONNECTION_PROBLEM(SharedResources.strings.connection_error)
}