package org.hyperskill.app.auth.domain.model

enum class AuthSocialError {
    SOCIAL_SIGNUP_WITH_EXISTING_EMAIL,
    SOCIAL_SIGNUP_WITHOUT_EMAIL,
    CONNECTION_PROBLEM
}