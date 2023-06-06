package org.hyperskill.app.profile.injection

import kotlinx.coroutines.flow.MutableSharedFlow
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.profile.cache.CurrentProfileStateHolderImpl
import org.hyperskill.app.profile.data.repository.CurrentProfileStateRepositoryImpl
import org.hyperskill.app.profile.data.repository.ProfileRepositoryImpl
import org.hyperskill.app.profile.data.source.CurrentProfileStateHolder
import org.hyperskill.app.profile.data.source.ProfileRemoteDataSource
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.domain.model.Profile
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.profile.remote.ProfileRemoteDataSourceImpl

class ProfileDataComponentImpl(private val appGraph: AppGraph) : ProfileDataComponent {

    companion object {
        private val currentProfileSharedFlow: MutableSharedFlow<Profile> by lazy {
            MutableSharedFlow()
        }
    }

    private val profileRemoteDataSource: ProfileRemoteDataSource =
        ProfileRemoteDataSourceImpl(
            appGraph.networkComponent.authorizedHttpClient
        )

    private val currentProfileStateHolder: CurrentProfileStateHolder by lazy {
        CurrentProfileStateHolderImpl(
            appGraph.commonComponent.json,
            appGraph.commonComponent.settings
        )
    }

    override val profileRepository: ProfileRepository
        get() = ProfileRepositoryImpl(profileRemoteDataSource)

    override val currentProfileStateRepository: CurrentProfileStateRepository
        get() = CurrentProfileStateRepositoryImpl(
            profileRemoteDataSource = profileRemoteDataSource,
            stateHolder = currentProfileStateHolder,
            mutableSharedFlow = currentProfileSharedFlow
        )

    override val profileInteractor: ProfileInteractor
        get() = ProfileInteractor(
            profileRepository,
            appGraph.submissionDataComponent.submissionRepository
        )
}