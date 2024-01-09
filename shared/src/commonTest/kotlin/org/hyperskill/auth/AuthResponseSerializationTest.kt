package org.hyperskill.auth

import kotlin.test.Test
import kotlin.test.assertEquals
import org.hyperskill.app.auth.remote.model.AuthResponse
import org.hyperskill.app.network.injection.NetworkModule

class AuthResponseSerializationTest {
    companion object {
        private const val TEST_JSON = """
            {
                "access_token": "some_access_toke",
                "expires_in": "1000",
                "refresh_token": "some_refresh_token",
                "token_type": "some_token_type",
                "scope": "some_scope"
            }
        """

        private val EXPECTED_AUTH_RESPONSE: AuthResponse =
            AuthResponse(
                accessToken = "some_access_toke",
                expiresIn = 1000,
                refreshToken = "some_refresh_token",
                tokenType = "some_token_type",
                scope = "some_scope"
            )
    }

    @Test
    fun `Serialized AuthResponse should be deserialized normally`() {
        val json = NetworkModule.provideJson()
        val actualAuthResponse = json.decodeFromString(AuthResponse.serializer(), TEST_JSON)
        assertEquals(EXPECTED_AUTH_RESPONSE, actualAuthResponse)
    }
}