package org.hyperskill.app.home.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.home.domain.interactor.HomeInteractor
import org.hyperskill.app.home.presentation.HomeFeature
import org.hyperskill.app.magic_links.domain.interactor.MagicLinksInteractor
import org.hyperskill.app.profile.cache.ProfileCacheDataSourceImpl
import org.hyperskill.app.profile.data.repository.ProfileRepositoryImpl
import org.hyperskill.app.profile.data.source.ProfileCacheDataSource
import org.hyperskill.app.profile.data.source.ProfileRemoteDataSource
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.domain.repository.ProfileRepository
import org.hyperskill.app.profile.remote.ProfileRemoteDataSourceImpl
import org.hyperskill.app.step.data.repository.StepRepositoryImpl
import org.hyperskill.app.step.data.source.StepRemoteDataSource
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.domain.repository.StepRepository
import org.hyperskill.app.step.remote.StepRemoteDataSourceImpl
import org.hyperskill.app.streak.data.repository.StreakRepositoryImpl
import org.hyperskill.app.streak.data.source.StreakRemoteDataSource
import org.hyperskill.app.streak.domain.interactor.StreakInteractor
import org.hyperskill.app.streak.domain.repository.StreakRepository
import org.hyperskill.app.streak.remote.StreakRemoteDataSourceImpl
import ru.nobird.app.presentation.redux.feature.Feature

class HomeComponentImpl(private val appGraph: AppGraph) : HomeComponent {
    private val streakRemoteDataSource: StreakRemoteDataSource = StreakRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val streakRepository: StreakRepository = StreakRepositoryImpl(streakRemoteDataSource)
    private val streakInteractor: StreakInteractor = StreakInteractor(streakRepository)

    private val profileRemoteDataSource: ProfileRemoteDataSource = ProfileRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val profileCacheDataSource: ProfileCacheDataSource = ProfileCacheDataSourceImpl(
        appGraph.commonComponent.json, appGraph.commonComponent.settings
    )
    private val profileRepository: ProfileRepository =
        ProfileRepositoryImpl(profileRemoteDataSource, profileCacheDataSource)
    private val profileInteractor: ProfileInteractor =
        ProfileInteractor(profileRepository, appGraph.submissionDataComponent.submissionRepository)

    private val stepRemoteDataSource: StepRemoteDataSource = StepRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val stepRepository: StepRepository = StepRepositoryImpl(stepRemoteDataSource)
    private val stepInteractor: StepInteractor = StepInteractor(stepRepository)
    private val homeInteractor: HomeInteractor =
        HomeInteractor(appGraph.submissionDataComponent.submissionRepository)

    private val analyticInteractor: AnalyticInteractor =
        appGraph.analyticComponent.analyticInteractor

    private val magicLinksInteractor: MagicLinksInteractor =
        appGraph.buildMagicLinksDataComponent().magicLinksInteractor

    override val homeFeature: Feature<HomeFeature.State, HomeFeature.Message, HomeFeature.Action>
        get() = HomeFeatureBuilder.build(
            analyticInteractor,
            homeInteractor,
            streakInteractor,
            profileInteractor,
            stepInteractor,
            magicLinksInteractor
        )
}