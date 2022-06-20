package org.hyperskill.app.profile.data.repository

import org.hyperskill.app.profile.data.source.ProfileRemoteDataSource
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val profileRemoteDataSource: ProfileRemoteDataSource
) : ProfileRepository {
    override suspend fun getCurrentProfile(): Result<Profile> =
        profileRemoteDataSource.getCurrentProfile()
}