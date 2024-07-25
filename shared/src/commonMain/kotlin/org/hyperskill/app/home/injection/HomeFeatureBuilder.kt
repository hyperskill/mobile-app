package org.hyperskill.app.home.injection

import co.touchlab.kermit.Logger
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.analytic.presentation.wrapWithAnalyticLogger
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetActionDispatcher
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetReducer
import org.hyperskill.app.challenges.widget.view.mapper.ChallengeWidgetViewStateMapper
import org.hyperskill.app.core.domain.BuildVariant
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.presentation.transformState
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarActionDispatcher
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarReducer
import org.hyperskill.app.home.presentation.HomeActionDispatcher
import org.hyperskill.app.home.presentation.HomeFeature
import org.hyperskill.app.home.presentation.HomeReducer
import org.hyperskill.app.home.view.mapper.HomeViewStateMapper
import org.hyperskill.app.logging.presentation.wrapWithLogger
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.step.domain.interactor.StepInteractor
import org.hyperskill.app.step_completion.domain.flow.StepCompletedFlow
import org.hyperskill.app.step_completion.domain.flow.TopicCompletedFlow
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import org.hyperskill.app.topics_repetitions.domain.flow.TopicRepeatedFlow
import org.hyperskill.app.topics_repetitions.domain.interactor.TopicsRepetitionsInteractor
import ru.nobird.app.core.model.safeCast
import ru.nobird.app.presentation.redux.dispatcher.transform
import ru.nobird.app.presentation.redux.dispatcher.wrapWithActionDispatcher
import ru.nobird.app.presentation.redux.feature.Feature
import ru.nobird.app.presentation.redux.feature.ReduxFeature

internal object HomeFeatureBuilder {
    private const val LOG_TAG = "HomeFeature"

    fun build(
        currentProfileStateRepository: CurrentProfileStateRepository,
        topicsRepetitionsInteractor: TopicsRepetitionsInteractor,
        stepInteractor: StepInteractor,
        currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
        analyticInteractor: AnalyticInteractor,
        sentryInteractor: SentryInteractor,
        dateFormatter: SharedDateFormatter,
        topicRepeatedFlow: TopicRepeatedFlow,
        topicCompletedFlow: TopicCompletedFlow,
        stepCompletedFlow: StepCompletedFlow,
        purchaseInteractor: PurchaseInteractor,
        gamificationToolbarReducer: GamificationToolbarReducer,
        gamificationToolbarActionDispatcher: GamificationToolbarActionDispatcher,
        challengeWidgetReducer: ChallengeWidgetReducer,
        challengeWidgetActionDispatcher: ChallengeWidgetActionDispatcher,
        challengeWidgetViewStateMapper: ChallengeWidgetViewStateMapper,
        logger: Logger,
        buildVariant: BuildVariant
    ): Feature<HomeFeature.ViewState, HomeFeature.Message, HomeFeature.Action> {
        val homeReducer = HomeReducer(
            gamificationToolbarReducer = gamificationToolbarReducer,
            challengeWidgetReducer = challengeWidgetReducer
        ).wrapWithLogger(buildVariant, logger, LOG_TAG)
        val homeActionDispatcher = HomeActionDispatcher(
            config = ActionDispatcherOptions(),
            currentProfileStateRepository = currentProfileStateRepository,
            topicsRepetitionsInteractor = topicsRepetitionsInteractor,
            stepInteractor = stepInteractor,
            currentSubscriptionStateRepository = currentSubscriptionStateRepository,
            sentryInteractor = sentryInteractor,
            purchaseInteractor = purchaseInteractor,
            dateFormatter = dateFormatter,
            topicRepeatedFlow = topicRepeatedFlow,
            topicCompletedFlow = topicCompletedFlow,
            stepCompletedFlow = stepCompletedFlow
        )
        val homeViewStateMapper = HomeViewStateMapper(
            challengeWidgetViewStateMapper = challengeWidgetViewStateMapper
        )

        return ReduxFeature(
            HomeFeature.State(
                homeState = HomeFeature.HomeState.Idle,
                toolbarState = GamificationToolbarFeature.State.Idle,
                challengeWidgetState = ChallengeWidgetFeature.State.Idle
            ),
            homeReducer
        )
            .transformState(homeViewStateMapper::map)
            .wrapWithActionDispatcher(homeActionDispatcher)
            .wrapWithActionDispatcher(
                gamificationToolbarActionDispatcher.transform(
                    transformAction = { it.safeCast<HomeFeature.InternalAction.GamificationToolbarAction>()?.action },
                    transformMessage = HomeFeature.Message::GamificationToolbarMessage
                )
            )
            .wrapWithActionDispatcher(
                challengeWidgetActionDispatcher.transform(
                    transformAction = { it.safeCast<HomeFeature.InternalAction.ChallengeWidgetAction>()?.action },
                    transformMessage = HomeFeature.Message::ChallengeWidgetMessage
                )
            )
            .wrapWithAnalyticLogger(analyticInteractor) {
                it.safeCast<HomeFeature.InternalAction.LogAnalyticEvent>()?.analyticEvent
            }
    }
}