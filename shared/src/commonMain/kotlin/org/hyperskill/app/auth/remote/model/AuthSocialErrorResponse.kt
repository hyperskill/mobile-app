package org.hyperskill.app.auth.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthSocialErrorResponse(
    @SerialName("error")
    val error: String,
    @SerialName("provider")
    val provider: String,
    @SerialName("email")
    val email: String
)