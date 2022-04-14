package org.hyperskill.app.auth.remote.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class AuthResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("scope")  val scope: String
)