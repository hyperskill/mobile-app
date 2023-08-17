package org.hyperskill.app.profile.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.badges.domain.repository.BadgesRepository
import org.hyperskill.app.core.domain.repository.updateState
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.notification.local.domain.flow.DailyStudyRemindersEnabledFlow
import org.hyperskill.app.notification.local.domain.interactor.NotificationInteractor
import org.hyperskill.app.notification.remote.domain.interactor.PushNotificationsInteractor
import org.hyperskill.app.products.domain.interactor.ProductsInteractor
import org.hyperskill.app.products.domain.model.Product
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.profile.domain.model.copy
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.profile.presentation.ProfileFeature.Action
import org.hyperskill.app.profile.presentation.ProfileFeature.Message
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.streaks.domain.flow.StreakFlow
import org.hyperskill.app.streaks.domain.interactor.StreaksInteractor
import org.hyperskill.app.streaks.domain.model.Streak
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class ProfileActionDispatcher(
    config: ActionDispatcherOptions,
    profileInteractor: ProfileInteractor,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val streaksInteractor: StreaksInteractor,
    private val productsInteractor: ProductsInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val notificationInteractor: NotificationInteractor,
    private val pushNotificationsInteractor: PushNotificationsInteractor,
    private val urlPathProcessor: UrlPathProcessor,
    private val streakFlow: StreakFlow,
    dailyStudyRemindersEnabledFlow: DailyStudyRemindersEnabledFlow,
    private val badgesRepository: BadgesRepository
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        profileInteractor.solvedStepsSharedFlow
            .onEach { onNewMessage(Message.StepQuizSolved) }
            .launchIn(actionScope)

        currentProfileStateRepository.changes
            .distinctUntilChanged()
            .onEach { profile ->
                onNewMessage(Message.ProfileChanged(profile))
            }
            .launchIn(actionScope)

        streakFlow.observe()
            .onEach { streak ->
                onNewMessage(Message.StreakChanged(streak))
            }
            .launchIn(actionScope)

        dailyStudyRemindersEnabledFlow.observe()
            .onEach { isDailyStudyRemindersEnabled ->
                onNewMessage(Message.DailyStudyRemindersIsEnabledChanged(isDailyStudyRemindersEnabled))
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchCurrentProfile -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildProfileScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                val profileResult = fetchProfileData().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    onNewMessage(Message.ProfileFetchResult.Error)
                    return
                }

                sentryInteractor.finishTransaction(sentryTransaction)

                streakFlow.notifyDataChanged(profileResult.streak)

                onNewMessage(profileResult)
            }
            is Action.BuyStreakFreeze -> {
                productsInteractor.buyStreakFreeze(action.streakFreezeProductId)
                    .getOrElse {
                        return onNewMessage(Message.BuyStreakFreezeResult.Error)
                    }

                currentProfileStateRepository.updateState { currentProfile ->
                    currentProfile.copy(
                        hypercoinsBalance = currentProfile.gamification.hypercoinsBalance - action.streakFreezePrice
                    )
                }

                onNewMessage(Message.BuyStreakFreezeResult.Success)
            }
            is Action.SaveDailyStudyRemindersIsEnabled ->
                handleSaveDailyStudyRemindersIsEnabled(action)
            is Action.SaveDailyStudyRemindersIntervalStartHour ->
                handleSaveDailyStudyRemindersIntervalStartHour(action)
            is Action.FetchProfile -> {
                // TODO add code when GET on any profile is implemented
            }
            is Action.LogAnalyticEvent ->
                analyticInteractor.logEvent(action.analyticEvent)
            is Action.GetMagicLink ->
                getLink(action.path, ::onNewMessage)
            else -> {}
        }
    }

    private suspend fun getLink(path: HyperskillUrlPath, onNewMessage: (Message) -> Unit): Unit =
        urlPathProcessor.processUrlPath(path)
            .fold(
                onSuccess = { url ->
                    onNewMessage(Message.GetMagicLinkReceiveSuccess(url))
                },
                onFailure = {
                    onNewMessage(Message.GetMagicLinkReceiveFailure)
                }
            )

    private suspend fun fetchProfileData(): Result<Message.ProfileFetchResult.Success> =
        runCatching {
            coroutineScope {
                val currentProfile =
                    currentProfileStateRepository
                        .getState(forceUpdate = true)
                        .getOrThrow()

                val streakResult = async { streaksInteractor.getUserStreak(currentProfile.id) }
                val streakFreezeProductResult = async { productsInteractor.getStreakFreezeProduct() }
                val badgesDeferred = async { badgesRepository.getReceivedBadges() }

                val streak = streakResult.await().getOrThrow()
                val streakFreezeProduct = streakFreezeProductResult.await().getOrNull()
                val badges = badgesDeferred.await().getOrThrow()

                Message.ProfileFetchResult.Success(
                    profile = currentProfile,
                    streak = streak,
                    streakFreezeState = getStreakFreezeState(streakFreezeProduct, streak),
                    dailyStudyRemindersState = ProfileFeature.DailyStudyRemindersState(
                        isEnabled = notificationInteractor.isDailyStudyRemindersEnabled(),
                        startHour = notificationInteractor.getDailyStudyRemindersIntervalStartHour()
                    ),
                    badges = badges
                )
            }
        }

    private fun getStreakFreezeState(
        streakFreezeProduct: Product?,
        streak: Streak?
    ): ProfileFeature.StreakFreezeState? =
        when {
            streakFreezeProduct == null || streak == null -> null
            streak.canFreeze -> ProfileFeature.StreakFreezeState.AlreadyHave
            streak.canBuyFreeze -> ProfileFeature.StreakFreezeState.CanBuy(
                streakFreezeProduct.id,
                streakFreezeProduct.price
            )
            else -> ProfileFeature.StreakFreezeState.NotEnoughGems(streakFreezeProduct.id, streakFreezeProduct.price)
        }

    private suspend fun handleSaveDailyStudyRemindersIsEnabled(action: Action.SaveDailyStudyRemindersIsEnabled) {
        val result = if (action.isEnabled) {
            val defaultStartHour = notificationInteractor.getDailyStudyRemindersIntervalStartHour()
            pushNotificationsInteractor.setDailyStudyReminderNotificationTime(defaultStartHour)
        } else {
            pushNotificationsInteractor.disableDailyStudyReminderNotification()
        }
        result
            .onSuccess {
                onNewMessage(Message.DailyStudyRemindersIsEnabledUpdateResult.Success(action.isEnabled))
            }
            .onFailure {
                onNewMessage(Message.DailyStudyRemindersIsEnabledUpdateResult.Error)
            }
    }

    private suspend fun handleSaveDailyStudyRemindersIntervalStartHour(
        action: Action.SaveDailyStudyRemindersIntervalStartHour
    ) {
        pushNotificationsInteractor.setDailyStudyReminderNotificationTime(action.startHour)
            .onSuccess {
                onNewMessage(Message.DailyStudyRemindersIntervalStartHourSaveResult.Success(action.startHour))
            }
            .onFailure {
                onNewMessage(Message.DailyStudyRemindersIntervalStartHourSaveResult.Error)
            }
    }
}