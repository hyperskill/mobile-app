package org.hyperskill.app.profile.presentation

import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.DataSourceType
import org.hyperskill.app.core.domain.url.HyperskillUrlPath
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.magic_links.domain.interactor.UrlPathProcessor
import org.hyperskill.app.products.domain.interactor.ProductsInteractor
import org.hyperskill.app.products.domain.model.Product
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
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
    private val profileInteractor: ProfileInteractor,
    private val streaksInteractor: StreaksInteractor,
    private val productsInteractor: ProductsInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val urlPathProcessor: UrlPathProcessor,
    private val streakFlow: StreakFlow
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {

    init {
        profileInteractor.solvedStepsSharedFlow
            .onEach { onNewMessage(Message.StepQuizSolved) }
            .launchIn(actionScope)

        profileInteractor.observeHypercoinsBalance()
            .onEach { hypercoinsBalance ->
                onNewMessage(Message.HypercoinsBalanceChanged(hypercoinsBalance))
            }
            .launchIn(actionScope)

        streakFlow.observe()
            .onEach { streak ->
                onNewMessage(Message.StreakChanged(streak))
            }
            .launchIn(actionScope)
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            is Action.FetchCurrentProfile -> {
                val sentryTransaction = HyperskillSentryTransactionBuilder.buildProfileScreenRemoteDataLoading()
                sentryInteractor.startTransaction(sentryTransaction)

                val currentProfile = profileInteractor
                    .getCurrentProfile(sourceType = DataSourceType.REMOTE)
                    .getOrElse {
                        sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                        return onNewMessage(Message.ProfileLoaded.Error)
                    }

                val streakResult = actionScope.async { streaksInteractor.getUserStreak(currentProfile.id) }
                val streakFreezeProductResult = actionScope.async { productsInteractor.getStreakFreezeProduct() }

                val streak = streakResult.await().getOrElse {
                    sentryInteractor.finishTransaction(sentryTransaction, throwable = it)
                    return onNewMessage(Message.ProfileLoaded.Error)
                }
                val streakFreezeProduct = streakFreezeProductResult.await().getOrNull()

                sentryInteractor.finishTransaction(sentryTransaction)

                streakFlow.notifyDataChanged(streak)

                onNewMessage(
                    Message.ProfileLoaded.Success(
                        profile = currentProfile,
                        streak = streak,
                        streakFreezeState = getStreakFreezeState(streakFreezeProduct, streak)
                    )
                )
            }
            is Action.BuyStreakFreeze -> {
                productsInteractor.buyStreakFreeze(action.streakFreezeProductId)
                    .getOrElse {
                        return onNewMessage(Message.StreakFreezeBought.Error)
                    }

                profileInteractor
                    .getCurrentProfile()
                    .getOrNull()
                    ?.gamification?.hypercoinsBalance?.let { oldBalance ->
                        profileInteractor.notifyHypercoinsBalanceChanged(oldBalance - action.streakFreezePrice)
                    }

                onNewMessage(Message.StreakFreezeBought.Success)
            }
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

    private fun getStreakFreezeState(streakFreezeProduct: Product?, streak: Streak?): ProfileFeature.StreakFreezeState? =
        when {
            streakFreezeProduct == null || streak == null -> null
            streak.canFreeze -> ProfileFeature.StreakFreezeState.AlreadyHave
            streak.canBuyFreeze -> ProfileFeature.StreakFreezeState.CanBuy(streakFreezeProduct.id, streakFreezeProduct.price)
            else -> ProfileFeature.StreakFreezeState.NotEnoughGems(streakFreezeProduct.price)
        }
}