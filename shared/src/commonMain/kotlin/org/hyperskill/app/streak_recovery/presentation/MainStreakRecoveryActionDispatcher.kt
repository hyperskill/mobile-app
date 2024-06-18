package org.hyperskill.app.streak_recovery.presentation

import co.touchlab.kermit.Logger
import co.touchlab.kermit.Severity
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.domain.repository.updateState
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.products.domain.interactor.ProductsInteractor
import org.hyperskill.app.profile.domain.model.copy
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.sentry.domain.model.transaction.HyperskillSentryTransactionBuilder
import org.hyperskill.app.sentry.domain.withTransaction
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature.Action
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature.Message
import org.hyperskill.app.streaks.domain.flow.StreakFlow
import org.hyperskill.app.streaks.domain.interactor.StreaksInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

internal class MainStreakRecoveryActionDispatcher(
    config: ActionDispatcherOptions,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val streaksInteractor: StreaksInteractor,
    private val productsInteractor: ProductsInteractor,
    private val sentryInteractor: SentryInteractor,
    private val logger: Logger,
    private val streakFlow: StreakFlow,
    private val resourceProvider: ResourceProvider
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    companion object {
        private const val LOG_TAG = "StreakRecovery"
    }

    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            StreakRecoveryFeature.InternalAction.FetchStreak -> {
                handleFetchStreakAction(::onNewMessage)
            }
            is StreakRecoveryFeature.InternalAction.RecoverStreak -> {
                val message = streaksInteractor
                    .recoverStreak()
                    .fold(
                        onSuccess = {
                            it.streaks.firstOrNull()?.let { newStreak ->
                                streakFlow.notifyDataChanged(newStreak)

                                currentProfileStateRepository.updateState { currentProfile ->
                                    val newHypercoinsBalance =
                                        currentProfile.gamification.hypercoinsBalance - action.streak.recoveryPrice
                                    currentProfile.copy(hypercoinsBalance = newHypercoinsBalance)
                                }
                            }
                            StreakRecoveryFeature.RecoverStreakResult.Success(
                                resourceProvider.getString(
                                    SharedResources.strings.streak_recovery_modal_recover_streak_success_message
                                )
                            )
                        },
                        onFailure = {
                            logger.log(
                                severity = Severity.Error,
                                tag = LOG_TAG,
                                throwable = null,
                                message = "recover streak $it"
                            )
                            StreakRecoveryFeature.RecoverStreakResult.Error(
                                resourceProvider.getString(
                                    SharedResources.strings.streak_recovery_modal_recover_streak_error_message
                                )
                            )
                        }
                    )

                onNewMessage(message)
            }
            StreakRecoveryFeature.InternalAction.CancelStreakRecovery -> {
                val message = streaksInteractor
                    .cancelStreakRecovery()
                    .fold(
                        onSuccess = {
                            it.streaks.firstOrNull()?.let { newStreak ->
                                streakFlow.notifyDataChanged(newStreak)
                            }
                            StreakRecoveryFeature.CancelStreakRecoveryResult.Success(
                                resourceProvider.getString(
                                    SharedResources.strings.streak_recovery_modal_cancel_streak_recovery_success_message
                                )
                            )
                        },
                        onFailure = {
                            logger.log(
                                severity = Severity.Error,
                                tag = LOG_TAG,
                                throwable = null,
                                message = "cancel streak recovery $it"
                            )
                            StreakRecoveryFeature.CancelStreakRecoveryResult.Error(
                                resourceProvider.getString(
                                    SharedResources.strings.streak_recovery_modal_cancel_streak_recovery_error_message
                                )
                            )
                        }
                    )

                onNewMessage(message)
            }
            is StreakRecoveryFeature.InternalAction.CaptureErrorMessage -> {
                logger.log(
                    severity = Severity.Error,
                    tag = LOG_TAG,
                    throwable = null,
                    message = action.message
                )
            }
            else -> {
                // no-op
            }
        }
    }

    private suspend fun handleFetchStreakAction(onNewMessage: (Message) -> Unit) {
        sentryInteractor.withTransaction(
            HyperskillSentryTransactionBuilder.buildStreakRecoveryFeatureFetchStreak(),
            onError = {
                logger.log(
                    severity = Severity.Error,
                    tag = LOG_TAG,
                    throwable = null,
                    message = "fetch streak $it"
                )
                StreakRecoveryFeature.FetchStreakResult.Error
            }
        ) {
            coroutineScope {
                val currentProfile = currentProfileStateRepository
                    .getState(forceUpdate = false)
                    .getOrThrow()

                val streakResult = async { streaksInteractor.getUserStreak(currentProfile.id) }
                val streakFreezeProductResult = async { productsInteractor.getStreakFreezeProduct() }

                val streak = streakResult.await().getOrThrow()
                val streakFreezeProduct = streakFreezeProductResult.await().getOrThrow()

                if (streak != null) {
                    StreakRecoveryFeature.FetchStreakResult.Success(streak, streakFreezeProduct)
                } else {
                    StreakRecoveryFeature.FetchStreakResult.Error
                }
            }
        }.let(onNewMessage)
    }
}