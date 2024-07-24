package org.hyperskill.app.home.injection

import org.hyperskill.app.challenges.widget.injection.ChallengeWidgetComponent
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.gamification_toolbar.domain.model.GamificationToolbarScreen
import org.hyperskill.app.gamification_toolbar.injection.GamificationToolbarComponent
import org.hyperskill.app.home.presentation.HomeFeature
import ru.nobird.app.presentation.redux.feature.Feature

internal class HomeComponentImpl(private val appGraph: AppGraph) : HomeComponent {
    private val gamificationToolbarComponent: GamificationToolbarComponent =
        appGraph.buildGamificationToolbarComponent(GamificationToolbarScreen.HOME)

    private val challengeWidgetComponent: ChallengeWidgetComponent =
        appGraph.buildChallengeWidgetComponent()

    override val homeFeature: Feature<HomeFeature.ViewState, HomeFeature.Message, HomeFeature.Action>
        get() = HomeFeatureBuilder.build(
            currentProfileStateRepository = appGraph.profileDataComponent.currentProfileStateRepository,
            topicsRepetitionsInteractor = appGraph.buildTopicsRepetitionsDataComponent().topicsRepetitionsInteractor,
            stepInteractor = appGraph.buildStepDataComponent().stepInteractor,
            currentSubscriptionStateRepository = appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository,
            analyticInteractor = appGraph.analyticComponent.analyticInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor,
            dateFormatter = appGraph.commonComponent.dateFormatter,
            topicRepeatedFlow = appGraph.topicsRepetitionsFlowDataComponent.topicRepeatedFlow,
            topicCompletedFlow = appGraph.stepCompletionFlowDataComponent.topicCompletedFlow,
            stepCompletedFlow = appGraph.stepCompletionFlowDataComponent.stepCompletedFlow,
            purchaseInteractor = appGraph.buildPurchaseComponent().purchaseInteractor,
            gamificationToolbarReducer = gamificationToolbarComponent.gamificationToolbarReducer,
            gamificationToolbarActionDispatcher = gamificationToolbarComponent.gamificationToolbarActionDispatcher,
            challengeWidgetReducer = challengeWidgetComponent.challengeWidgetReducer,
            challengeWidgetActionDispatcher = challengeWidgetComponent.challengeWidgetActionDispatcher,
            challengeWidgetViewStateMapper = challengeWidgetComponent.challengeWidgetViewStateMapper,
            logger = appGraph.loggerComponent.logger,
            buildVariant = appGraph.commonComponent.buildKonfig.buildVariant
        )
}