package org.hyperskill.app.profile.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.profile.data.repository.ProfileRepositoryImpl
import org.hyperskill.app.profile.data.source.ProfileRemoteDataSource
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.profile.presentation.ProfileFeature
import org.hyperskill.app.profile.remote.ProfileRemoteDataSourceImpl
import org.hyperskill.app.streak.data.repository.StreakRepositoryImpl
import org.hyperskill.app.streak.data.source.StreakRemoteDataSource
import org.hyperskill.app.streak.domain.interactor.StreakInteractor
import org.hyperskill.app.streak.domain.repository.StreakRepository
import org.hyperskill.app.streak.remote.StreakRemoteDataSourceImpl
import ru.nobird.app.presentation.redux.feature.Feature

class ProfileComponentImpl(private val appGraph: AppGraph) : ProfileComponent {
    private val profileRemoteDataSource: ProfileRemoteDataSource = ProfileRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val profileRepository: ProfileRepository = ProfileRepositoryImpl(profileRemoteDataSource)
    private val profileInteractor: ProfileInteractor = ProfileInteractor(profileRepository)

    private val streakRemoteDataSource: StreakRemoteDataSource = StreakRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val streakRepository: StreakRepository = StreakRepositoryImpl(streakRemoteDataSource)
    private val streakInteractor: StreakInteractor = StreakInteractor(streakRepository)

    override val profileFeature: Feature<ProfileFeature.State, ProfileFeature.Message, ProfileFeature.Action>
        get() = ProfileFeatureBuilder.build(profileInteractor, streakInteractor)
}