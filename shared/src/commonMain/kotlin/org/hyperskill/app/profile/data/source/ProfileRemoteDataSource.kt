package org.hyperskill.app.profile.data.source

import org.hyperskill.app.profile.domain.model.Profile

interface ProfileRemoteDataSource {
    suspend fun getCurrentProfile(): Result<Profile>
}