package org.hyperskill.app.profile.data.repository

import org.hyperskill.app.core.data.repository.BaseStateRepository
import org.hyperskill.app.profile.data.source.CurrentProfileStateHolder
import org.hyperskill.app.profile.data.source.ProfileRemoteDataSource
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    override val stateHolder: CurrentProfileStateHolder
) : ProfileRepository, BaseStateRepository<Profile>() {
    override suspend fun loadState(): Result<Profile> =
        profileRemoteDataSource.getCurrentProfile()

    override suspend fun selectTrackWithProject(profileId: Long, trackId: Long, projectId: Long): Result<Profile> =
        profileRemoteDataSource.selectTrackWithProject(profileId, trackId, projectId)

    override suspend fun selectTrack(profileId: Long, trackId: Long): Result<Profile> =
        profileRemoteDataSource.selectTrack(profileId, trackId)
}