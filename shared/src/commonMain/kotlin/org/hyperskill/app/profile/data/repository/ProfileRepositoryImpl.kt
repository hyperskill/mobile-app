package org.hyperskill.app.profile.data.repository

import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.profile.data.source.ProfileCacheDataSource
import org.hyperskill.app.profile.data.source.ProfileRemoteDataSource
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val profileCacheDataSource: ProfileCacheDataSource
) : ProfileRepository {
    override suspend fun getCurrentProfile(primarySourceType: DataSourceType): Result<Profile> =
        when (primarySourceType) {
            DataSourceType.REMOTE ->
                profileRemoteDataSource
                    .getCurrentProfile()
                    .onSuccess { profile ->
                        profileCacheDataSource.saveProfile(profile)
                    }

            DataSourceType.CACHE ->
                profileCacheDataSource
                    .getCurrentProfile()
        }

    override suspend fun selectTrackWithProject(profileId: Long, trackId: Long, projectId: Long): Result<Profile> =
        profileRemoteDataSource.selectTrackWithProject(profileId, trackId, projectId)

    override suspend fun selectTrack(profileId: Long, trackId: Long): Result<Profile> =
        profileRemoteDataSource.selectTrack(profileId, trackId)

    override suspend fun clearCache() {
        profileCacheDataSource.clearCache()
    }
}