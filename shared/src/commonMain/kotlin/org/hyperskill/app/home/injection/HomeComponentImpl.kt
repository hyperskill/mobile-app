package org.hyperskill.app.home.injection

import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.home.domain.interactor.HomeInteractor
import org.hyperskill.app.home.presentation.HomeFeature
import org.hyperskill.app.next_learning_activity_widget.injection.NextLearningActivityWidgetComponent
import org.hyperskill.app.problems_limit.domain.model.ProblemsLimitScreen
import org.hyperskill.app.problems_limit.injection.ProblemsLimitComponent
import ru.nobird.app.presentation.redux.feature.Feature

class HomeComponentImpl(private val appGraph: AppGraph) : HomeComponent {
    private val homeInteractor: HomeInteractor =
        HomeInteractor(appGraph.submissionDataComponent.submissionRepository)

    private val gamificationToolbarComponent: GamificationToolbarComponent =
        appGraph.buildGamificationToolbarComponent(GamificationToolbarScreen.HOME)

    private val problemsLimitComponent: ProblemsLimitComponent =
        appGraph.buildProblemsLimitComponent(ProblemsLimitScreen.HOME)

    private val nextLearningActivityWidgetComponent: NextLearningActivityWidgetComponent =
        appGraph.buildNextLearningActivityWidgetComponent()

    override val homeFeature: Feature<HomeFeature.State, HomeFeature.Message, HomeFeature.Action>
        get() = HomeFeatureBuilder.build(
            homeInteractor,
            appGraph.profileDataComponent.currentProfileStateRepository,
            appGraph.buildTopicsRepetitionsDataComponent().topicsRepetitionsInteractor,
            appGraph.buildStepDataComponent().stepInteractor,
            appGraph.buildFreemiumDataComponent().freemiumInteractor,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.commonComponent.dateFormatter,
            appGraph.topicsRepetitionsFlowDataComponent.topicRepeatedFlow,
            gamificationToolbarComponent.gamificationToolbarReducer,
            gamificationToolbarComponent.gamificationToolbarActionDispatcher,
            problemsLimitComponent.problemsLimitReducer,
            problemsLimitComponent.problemsLimitActionDispatcher,
            nextLearningActivityWidgetComponent.nextLearningActivityWidgetReducer,
            nextLearningActivityWidgetComponent.nextLearningActivityWidgetActionDispatcher,
            appGraph.loggerComponent.logger,
            appGraph.commonComponent.buildKonfig.buildVariant
        )
}