package org.hyperskill.app.streak_recovery.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.repository.CurrentProfileStateRepository
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
    private val streakFlow: StreakFlow
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            StreakRecoveryFeature.InternalAction.FetchStreak -> {
                val currentProfile = currentProfileStateRepository
                    .getState()
                    .getOrElse { return onNewMessage(StreakRecoveryFeature.FetchStreakResult.Error) }

                val streak = streaksInteractor
                    .getUserStreak(currentProfile.id)
                    .getOrElse { return onNewMessage(StreakRecoveryFeature.FetchStreakResult.Error) }

                val message = if (streak != null) {
                    StreakRecoveryFeature.FetchStreakResult.Success(
                        streak.canBeRecovered,
                        streak.recoveryPrice,
                        streak.previousStreak
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
                            }
                            StreakRecoveryFeature.RecoverStreakResult.Success
                        },
                        onFailure = { StreakRecoveryFeature.RecoverStreakResult.Error }
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
                            StreakRecoveryFeature.CancelStreakRecoveryResult.Success
                        },
                        onFailure = { StreakRecoveryFeature.CancelStreakRecoveryResult.Error }
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