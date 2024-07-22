package org.hyperskill.app.gamification_toolbar.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.gamification_toolbar.domain.repository.CurrentGamificationToolbarDataStateRepository
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Action
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.InternalAction
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.InternalMessage
import org.hyperskill.app.gamification_toolbar.presentation.GamificationToolbarFeature.Message
import org.hyperskill.app.profile.domain.model.freemiumChargeLimitsStrategy
import org.hyperskill.app.profile.domain.model.isMobileContentTrialEnabled
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.purchases.domain.interactor.PurchaseInteractor
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.step_completion.domain.flow.StepCompletedFlow
import org.hyperskill.app.step_completion.domain.flow.TopicCompletedFlow
import org.hyperskill.app.streaks.domain.flow.StreakFlow
import org.hyperskill.app.study_plan.domain.repository.CurrentStudyPlanStateRepository
import org.hyperskill.app.subscriptions.domain.model.Subscription
import org.hyperskill.app.subscriptions.domain.model.orContentTrial
import org.hyperskill.app.subscriptions.domain.repository.CurrentSubscriptionStateRepository
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class MainGamificationToolbarActionDispatcher(
    config: ActionDispatcherOptions,
    stepCompletedFlow: StepCompletedFlow,
    streakFlow: StreakFlow,
    currentStudyPlanStateRepository: CurrentStudyPlanStateRepository,
    topicCompletedFlow: TopicCompletedFlow,
    private val currentGamificationToolbarDataStateRepository: CurrentGamificationToolbarDataStateRepository,
    private val currentSubscriptionStateRepository: CurrentSubscriptionStateRepository,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val purchaseInteractor: PurchaseInteractor,
    private val sentryInteractor: SentryInteractor
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        stepCompletedFlow.observe()
            .onEach { onNewMessage(InternalMessage.StepSolved) }
            .launchIn(actionScope)

        streakFlow.observe()
            .distinctUntilChanged()
            .onEach { streak ->
                onNewMessage(InternalMessage.StreakChanged(streak))
            }
            .launchIn(actionScope)

        currentStudyPlanStateRepository.changes
            .distinctUntilChanged()
            .onEach { studyPlan ->
                onNewMessage(InternalMessage.StudyPlanChanged(studyPlan))
            }
            .launchIn(actionScope)

        topicCompletedFlow.observe()
            .distinctUntilChanged()
            .onEach {
                onNewMessage(InternalMessage.TopicCompleted)
            }
            .launchIn(actionScope)

        currentGamificationToolbarDataStateRepository.changes
            .distinctUntilChanged()
            .onEach { onNewMessage(InternalMessage.GamificationToolbarDataChanged(it)) }
            .launchIn(actionScope)

        currentSubscriptionStateRepository.changes
            .distinctUntilChanged()
            .onEach { onNewMessage(InternalMessage.SubscriptionChanged(it)) }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is InternalAction.FetchGamificationToolbarData ->
                handleFetchGamificationToolbarDataAction(action, ::onNewMessage)
            else -> {
                // no op
            }
        }
    }

    private suspend fun handleFetchGamificationToolbarDataAction(
        action: InternalAction.FetchGamificationToolbarData,
        onNewMessage: (Message) -> Unit
    ) {
        sentryInteractor.withTransaction(
            action.screen.fetchContentSentryTransaction,
            onError = { InternalMessage.FetchGamificationToolbarDataError }
        ) {
            coroutineScope {
                val toolbarDataDeferred = async {
                    currentGamificationToolbarDataStateRepository.getStateWithSource(forceUpdate = action.forceUpdate)
                }

                val profileDeferred = async {
                    currentProfileStateRepository
                        .getState(forceUpdate = action.forceUpdate)
                }

                val gamificationToolbarDataWithSource = toolbarDataDeferred.await().getOrThrow()
                val profile = profileDeferred.await().getOrThrow()

                val canMakePayments = purchaseInteractor.canMakePayments().getOrElse { false }

                val subscription = getSubscription(
                    isMobileContentTrialEnabled = profile.features.isMobileContentTrialEnabled,
                    canMakePayments = canMakePayments,
                    forceUpdate = action.forceUpdate,
                    gamificationToolbarDataSourceType = gamificationToolbarDataWithSource.usedDataSourceType
                )

                InternalMessage.FetchGamificationToolbarDataSuccess(
                    gamificationToolbarData = gamificationToolbarDataWithSource.state,
                    subscription = subscription,
                    chargeLimitsStrategy = profile.freemiumChargeLimitsStrategy,
                    isMobileContentTrialEnabled = profile.features.isMobileContentTrialEnabled,
                    canMakePayments = canMakePayments
                )
            }
        }.let(onNewMessage)
    }

    private suspend fun getSubscription(
        isMobileContentTrialEnabled: Boolean,
        canMakePayments: Boolean,
        forceUpdate: Boolean,
        gamificationToolbarDataSourceType: DataSourceType
    ): Subscription {
        val subscriptionWithSource =
            currentSubscriptionStateRepository
                .getStateWithSource(forceUpdate = forceUpdate)
                .map {
                    it.copy(
                        state = it.state.orContentTrial(
                            isMobileContentTrialEnabled = isMobileContentTrialEnabled,
                            canMakePayments = canMakePayments
                        )
                    )
                }
                .getOrThrow()

        // Fetch subscription from remote
        // if gamification toolbar data is from remote and subscription is from cache.
        // That means there was no toolbar data in-memory and subscription from disk cache,
        // so we need to fetch subscription from remote to get the latest data (happens on app launch).
        val shouldFetchSubscriptionFromRemote = !forceUpdate &&
            gamificationToolbarDataSourceType == DataSourceType.REMOTE &&
            subscriptionWithSource.usedDataSourceType == DataSourceType.CACHE

        return if (shouldFetchSubscriptionFromRemote) {
            currentSubscriptionStateRepository
                .getState(forceUpdate = true)
                .map { subscription ->
                    subscription.orContentTrial(
                        isMobileContentTrialEnabled = isMobileContentTrialEnabled,
                        canMakePayments = canMakePayments
                    )
                }
                .getOrThrow()
        } else {
            subscriptionWithSource.state
        }
    }
}