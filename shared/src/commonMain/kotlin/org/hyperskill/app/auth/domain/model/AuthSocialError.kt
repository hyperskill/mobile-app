package org.hyperskill.app.auth.domain.model

sealed interface AuthSocialError {
    data class ServerError(val errorText: String) : AuthSocialError
    object ConnectionProblem : AuthSocialError
}