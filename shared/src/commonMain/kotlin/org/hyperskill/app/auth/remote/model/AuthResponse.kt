package org.hyperskill.app.auth.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.hyperskill.app.auth.remote.source.AuthRemoteDataSourceImpl
import org.hyperskill.app.network.NetworkBuilder

/**
 * Represents a response for an auth request.
 *
 * Warning!
 * This model is stored in the cache.
 * Adding new field or modifying old ones,
 * check that all fields will be deserialized from cache without an error.
 * All the new optional fields must have default values.
 * @see [NetworkBuilder.buildAuthorizedClient], [AuthRemoteDataSourceImpl]
 */
@Serializable
data class AuthResponse(
    @SerialName("access_token") val accessToken: String,
    @SerialName("expires_in") val expiresIn: Int,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("token_type") val tokenType: String,
    @SerialName("scope") val scope: String
)