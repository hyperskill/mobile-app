package org.hyperskill.app.profile.data.repository

import org.hyperskill.app.core.data.repository.BaseStateRepository
import org.hyperskill.app.profile.data.source.CurrentProfileStateHolder
import org.hyperskill.app.profile.data.source.ProfileRemoteDataSource
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository

class CurrentProfileStateRepositoryImpl(
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    override val stateHolder: CurrentProfileStateHolder
) : CurrentProfileStateRepository, BaseStateRepository<Profile>() {
    override suspend fun loadState(): Result<Profile> =
        profileRemoteDataSource.getCurrentProfile()
}