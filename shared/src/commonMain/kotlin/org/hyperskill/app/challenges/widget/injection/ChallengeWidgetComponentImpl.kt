package org.hyperskill.app.challenges.widget.injection

import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetActionDispatcher
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetReducer
import org.hyperskill.app.challenges.widget.presentation.MainChallengeWidgetActionDispatcher
import org.hyperskill.app.challenges.widget.view.mapper.ChallengeWidgetViewStateMapper
import org.hyperskill.app.core.injection.AppGraph
import org.hyperskill.app.core.presentation.ActionDispatcherOptions

internal class ChallengeWidgetComponentImpl(private val appGraph: AppGraph) : ChallengeWidgetComponent {
    override val challengeWidgetReducer: ChallengeWidgetReducer
        get() = ChallengeWidgetReducer()

    private val mainChallengeWidgetActionDispatcher: MainChallengeWidgetActionDispatcher
        get() = MainChallengeWidgetActionDispatcher(
            config = ActionDispatcherOptions(),
            stepCompletedFlow = appGraph.stepCompletionFlowDataComponent.stepCompletedFlow,
            topicCompletedFlow = appGraph.stepCompletionFlowDataComponent.topicCompletedFlow,
            dailyStepCompletedFlow = appGraph.stepCompletionFlowDataComponent.dailyStepCompletedFlow,
            challengesRepository = appGraph.buildChallengesDataComponent().challengesRepository,
            magicLinksInteractor = appGraph.buildMagicLinksDataComponent().magicLinksInteractor,
            sentryInteractor = appGraph.sentryComponent.sentryInteractor
        )

    override val challengeWidgetActionDispatcher: ChallengeWidgetActionDispatcher
        get() = ChallengeWidgetActionDispatcher(
            mainChallengeWidgetActionDispatcher,
            appGraph.analyticComponent.analyticInteractor
        )

    override val challengeWidgetViewStateMapper: ChallengeWidgetViewStateMapper
        get() = ChallengeWidgetViewStateMapper(
            dateFormatter = appGraph.commonComponent.dateFormatter,
            resourceProvider = appGraph.commonComponent.resourceProvider
        )
}