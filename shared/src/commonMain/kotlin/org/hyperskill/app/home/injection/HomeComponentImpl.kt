package org.hyperskill.app.home.injection

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.home.domain.interactor.HomeInteractor
import org.hyperskill.app.home.presentation.HomeFeature
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
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
import org.hyperskill.app.topics_repetitions.domain.interactor.TopicsRepetitionsInteractor
import ru.nobird.app.presentation.redux.feature.Feature

class HomeComponentImpl(private val appGraph: AppGraph) : HomeComponent {
    private val streakRemoteDataSource: StreakRemoteDataSource = StreakRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val streakRepository: StreakRepository = StreakRepositoryImpl(streakRemoteDataSource)
    private val streakInteractor: StreakInteractor = StreakInteractor(streakRepository)

    private val profileInteractor: ProfileInteractor =
        appGraph.buildProfileDataComponent().profileInteractor

    private val stepRemoteDataSource: StepRemoteDataSource = StepRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val stepRepository: StepRepository = StepRepositoryImpl(stepRemoteDataSource)
    private val stepInteractor: StepInteractor = StepInteractor(stepRepository)
    private val homeInteractor: HomeInteractor =
        HomeInteractor(appGraph.submissionDataComponent.submissionRepository)

    private val analyticInteractor: AnalyticInteractor =
        appGraph.analyticComponent.analyticInteractor

    private val sentryInteractor: SentryInteractor =
        appGraph.sentryComponent.sentryInteractor

    private val urlPathProcessor: UrlPathProcessor =
        appGraph.buildMagicLinksDataComponent().urlPathProcessor

    private val topicsRepetitionsInteractor: TopicsRepetitionsInteractor =
        appGraph.topicsRepetitionsDataComponent.topicsRepetitionsInteractor

    override val homeFeature: Feature<HomeFeature.State, HomeFeature.Message, HomeFeature.Action>
        get() = HomeFeatureBuilder.build(
            homeInteractor,
            streakInteractor,
            profileInteractor,
            topicsRepetitionsInteractor,
            stepInteractor,
            analyticInteractor,
            sentryInteractor,
            urlPathProcessor,
            appGraph.commonComponent.dateFormatter
        )
}