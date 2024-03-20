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
            appGraph.profileDataComponent.currentProfileStateRepository,
            appGraph.buildTopicsRepetitionsDataComponent().topicsRepetitionsInteractor,
            appGraph.buildStepDataComponent().stepInteractor,
            appGraph.stateRepositoriesComponent.currentSubscriptionStateRepository,
            appGraph.analyticComponent.analyticInteractor,
            appGraph.sentryComponent.sentryInteractor,
            appGraph.commonComponent.dateFormatter,
            appGraph.topicsRepetitionsFlowDataComponent.topicRepeatedFlow,
            appGraph.stepCompletionFlowDataComponent.topicCompletedFlow,
            appGraph.stepCompletionFlowDataComponent.stepCompletedFlow,
            gamificationToolbarComponent.gamificationToolbarReducer,
            gamificationToolbarComponent.gamificationToolbarActionDispatcher,
            challengeWidgetComponent.challengeWidgetReducer,
            challengeWidgetComponent.challengeWidgetActionDispatcher,
            challengeWidgetComponent.challengeWidgetViewStateMapper,
            appGraph.loggerComponent.logger,
            appGraph.commonComponent.buildKonfig.buildVariant
        )
}