package org.hyperskill.app.home.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.home.domain.interactor.HomeInteractor
import org.hyperskill.app.home.presentation.HomeFeature
import org.hyperskill.app.step.data.repository.StepRepositoryImpl
import org.hyperskill.app.step.data.source.StepRemoteDataSource
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step.domain.repository.StepRepository
import org.hyperskill.app.step.remote.StepRemoteDataSourceImpl
import org.hyperskill.app.topics_to_discover_next.injection.TopicsToDiscoverNextComponent
import ru.nobird.app.presentation.redux.feature.Feature

class HomeComponentImpl(private val appGraph: AppGraph) : HomeComponent {
    private val stepRemoteDataSource: StepRemoteDataSource = StepRemoteDataSourceImpl(
        appGraph.networkComponent.authorizedHttpClient
    )
    private val stepRepository: StepRepository = StepRepositoryImpl(stepRemoteDataSource)
    private val stepInteractor: StepInteractor = StepInteractor(stepRepository)
    private val homeInteractor: HomeInteractor =
        HomeInteractor(appGraph.submissionDataComponent.submissionRepository)

    private val gamificationToolbarComponent: GamificationToolbarComponent =
        appGraph.buildGamificationToolbarComponent()

    private val topicsToDiscoverNextComponent: TopicsToDiscoverNextComponent =
        appGraph.buildTopicsToDiscoverNextComponent()

    override val homeFeature: Feature<HomeFeature.State, HomeFeature.Message, HomeFeature.Action>
        get() = HomeFeatureBuilder.build(
            homeInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.topicsRepetitionsDataComponent.topicsRepetitionsInteractor,
            stepInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.buildMagicLinksDataComponent().urlPathProcessor,
            appGraph.commonComponent.dateFormatter,
            gamificationToolbarComponent.gamificationToolbarReducer,
            gamificationToolbarComponent.gamificationToolbarActionDispatcher,
            topicsToDiscoverNextComponent.topicsToDiscoverNextReducer,
            topicsToDiscoverNextComponent.topicsToDiscoverNextActionDispatcher
        )
}