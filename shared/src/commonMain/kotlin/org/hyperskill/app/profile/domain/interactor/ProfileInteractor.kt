package org.hyperskill.app.profile.domain.interactor

import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.repository.ProfileRepository

class ProfileInteractor(
    private val profileRepository: ProfileRepository
) {
    suspend fun getCurrentProfile(sourceType: DataSourceType = DataSourceType.CACHE): Result<Profile> =
        profileRepository.getCurrentProfile(sourceType)
}