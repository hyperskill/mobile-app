package org.hyperskill.app.auth.data.source

import org.hyperskill.app.auth.domain.model.SocialAuthProvider

interface AuthRemoteDataSource {
    suspend fun authWithSocial(authCode: String, socialProvider: SocialAuthProvider): Result<Unit>
    suspend fun authWithEmail(email: String, password: String): Result<Unit>
}