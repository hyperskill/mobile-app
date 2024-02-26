package org.hyperskill.app.profile.injection

import org.hyperskill.app.core.injection.CommonComponent
import org.hyperskill.app.network.injection.NetworkComponent
import org.hyperskill.app.profile.cache.CurrentProfileStateHolderImpl
import org.hyperskill.app.profile.data.repository.CurrentProfileStateRepositoryImpl
import org.hyperskill.app.profile.data.repository.ProfileRepositoryImpl
import org.hyperskill.app.profile.data.source.CurrentProfileStateHolder
import org.hyperskill.app.profile.data.source.ProfileRemoteDataSource
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.profile.remote.ProfileRemoteDataSourceImpl

internal class ProfileDataComponentImpl(
    networkComponent: NetworkComponent,
    commonComponent: CommonComponent
) : ProfileDataComponent {

    private val profileRemoteDataSource: ProfileRemoteDataSource by lazy {
        ProfileRemoteDataSourceImpl(
            networkComponent.authorizedHttpClient
        )
    }

    private val currentProfileStateHolder: CurrentProfileStateHolder by lazy {
        CurrentProfileStateHolderImpl(
            commonComponent.json,
            commonComponent.settings
        )
    }

    override val profileRepository: ProfileRepository
        get() = ProfileRepositoryImpl(profileRemoteDataSource)

    override val currentProfileStateRepository: CurrentProfileStateRepository by lazy {
        CurrentProfileStateRepositoryImpl(
            profileRemoteDataSource = profileRemoteDataSource,
            stateHolder = currentProfileStateHolder
        )
    }
}