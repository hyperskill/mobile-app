package org.hyperskill.app.auth.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthErrorResponse(
    @SerialName("error")
    val error: String,
    @SerialName("error_description")
    val errorDescription: String
)