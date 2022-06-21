package org.hyperskill.app.profile.domain.repository

import org.hyperskill.app.profile.domain.model.Profile

interface ProfileRepository {
    suspend fun getCurrentProfile(): Result<Profile>
}