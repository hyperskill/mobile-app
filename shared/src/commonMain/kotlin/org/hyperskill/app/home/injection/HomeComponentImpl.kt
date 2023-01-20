package org.hyperskill.app.home.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.home.domain.interactor.HomeInteractor
import org.hyperskill.app.home.presentation.HomeFeature
import ru.nobird.app.presentation.redux.feature.Feature

class HomeComponentImpl(private val appGraph: AppGraph) : HomeComponent {
    private val homeInteractor: HomeInteractor =
        HomeInteractor(appGraph.submissionDataComponent.submissionRepository)

    private val gamificationToolbarComponent: GamificationToolbarComponent =
        appGraph.buildGamificationToolbarComponent()

    override val homeFeature: Feature<HomeFeature.State, HomeFeature.Message, HomeFeature.Action>
        get() = HomeFeatureBuilder.build(
            homeInteractor,
            appGraph.buildProfileDataComponent().profileInteractor,
            appGraph.topicsRepetitionsDataComponent.topicsRepetitionsInteractor,
            appGraph.stepDataComponent.stepInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.buildMagicLinksDataComponent().urlPathProcessor,
            appGraph.commonComponent.dateFormatter,
            gamificationToolbarComponent.gamificationToolbarReducer,
            gamificationToolbarComponent.gamificationToolbarActionDispatcher
        )
}