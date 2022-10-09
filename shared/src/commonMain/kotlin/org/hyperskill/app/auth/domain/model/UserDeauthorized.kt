package org.hyperskill.app.auth.domain.model

data class UserDeauthorized(val reason: Reason) {
    enum class Reason {
        TOKEN_REFRESH_FAILURE,
        SIGN_OUT
    }
}