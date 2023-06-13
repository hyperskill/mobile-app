package org.hyperskill.app.streak_recovery.presentation

import org.hyperskill.app.SharedResources
import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.domain.repository.updateState
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.profile.domain.model.copy
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
import org.hyperskill.app.sentry.domain.interactor.SentryInteractor
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature.Action
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature.Message
import org.hyperskill.app.streaks.domain.flow.StreakFlow
import org.hyperskill.app.streaks.domain.interactor.StreaksInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StreakRecoveryActionDispatcher(
    config: ActionDispatcherOptions,
    private val currentProfileStateRepository: CurrentProfileStateRepository,
    private val streaksInteractor: StreaksInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val sentryInteractor: SentryInteractor,
    private val streakFlow: StreakFlow,
    private val resourceProvider: ResourceProvider
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            StreakRecoveryFeature.InternalAction.FetchStreak -> {
                val currentProfile = currentProfileStateRepository
                    .getState()
                    .onFailure { sentryInteractor.captureErrorMessage("StreakRecovery: fetch streak $it") }
                    .getOrElse { return onNewMessage(StreakRecoveryFeature.FetchStreakResult.Error) }

                val streak = streaksInteractor
                    .getUserStreak(currentProfile.id)
                    .onFailure { sentryInteractor.captureErrorMessage("StreakRecovery: fetch streak $it") }
                    .getOrElse { return onNewMessage(StreakRecoveryFeature.FetchStreakResult.Error) }

                val message = if (streak != null) {
                    StreakRecoveryFeature.FetchStreakResult.Success(
                        streak.canBeRecovered,
                        streak.recoveryPrice.toString(),
                        resourceProvider.getQuantityString(
                            SharedResources.plurals.gems_without_count, streak.recoveryPrice
                        ),
                        resourceProvider.getString(
                            SharedResources.strings.streak_recovery_modal_text, streak.previousStreak
                        )
                    )
                } else {
                    StreakRecoveryFeature.FetchStreakResult.Error
                }

                onNewMessage(message)
            }
            StreakRecoveryFeature.InternalAction.RecoverStreak -> {
                val message = streaksInteractor
                    .recoverStreak()
                    .fold(
                        onSuccess = {
                            it.streaks.firstOrNull()?.let { newStreak ->
                                streakFlow.notifyDataChanged(newStreak)

                                currentProfileStateRepository.updateState { currentProfile ->
                                    currentProfile.copy(
                                        hypercoinsBalance = currentProfile.gamification.hypercoinsBalance -
                                            newStreak.recoveryPrice
                                    )
                                }
                            }
                            StreakRecoveryFeature.RecoverStreakResult.Success(
                                resourceProvider.getString(
                                    SharedResources.strings.streak_recovery_modal_recover_streak_success_message
                                )
                            )
                        },
                        onFailure = {
                            sentryInteractor.captureErrorMessage("StreakRecovery: recover streak $it")
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
                            sentryInteractor.captureErrorMessage("StreakRecovery: cancel streak recovery $it")
                            StreakRecoveryFeature.CancelStreakRecoveryResult.Error(
                                resourceProvider.getString(
                                    SharedResources.strings.streak_recovery_modal_cancel_streak_recovery_error_message
                                )
                            )
                        }
                    )

                onNewMessage(message)
            }
            is StreakRecoveryFeature.InternalAction.LogAnalyticEvent -> {
                analyticInteractor.logEvent(action.event)
            }
            else -> {
                // no-op
            }
        }
    }
}