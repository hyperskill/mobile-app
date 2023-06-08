package org.hyperskill.app.streak_recovery.presentation

import org.hyperskill.app.analytic.domain.interactor.AnalyticInteractor
import org.hyperskill.app.core.presentation.ActionDispatcherOptions
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature.Action
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature.Message
import org.hyperskill.app.streaks.domain.flow.StreakFlow
import org.hyperskill.app.streaks.domain.interactor.StreaksInteractor
import ru.nobird.app.presentation.redux.dispatcher.CoroutineActionDispatcher

class StreakRecoveryActionDispatcher(
    config: ActionDispatcherOptions,
    private val profileInteractor: ProfileInteractor,
    private val streaksInteractor: StreaksInteractor,
    private val analyticInteractor: AnalyticInteractor,
    private val streakFlow: StreakFlow
) : CoroutineActionDispatcher<Action, Message>(config.createConfig()) {
    override suspend fun doSuspendableAction(action: Action) {
        when (action) {
            StreakRecoveryFeature.InternalAction.FetchStreak -> {
                // TODO: replace with current profile state repository when ALTAPPS-682 will be ready
                val currentProfile = profileInteractor
                    .getCurrentProfile()
                    .getOrElse { return onNewMessage(StreakRecoveryFeature.FetchStreakResult.Error) }

                val streak = streaksInteractor
                    .getUserStreak(currentProfile.id)
                    .getOrElse { return onNewMessage(StreakRecoveryFeature.FetchStreakResult.Error) }

                onNewMessage(
                    if (streak != null) {
                        StreakRecoveryFeature.FetchStreakResult.Success(
                            streak.canBeRecovered,
                            streak.recoveryPrice,
                            streak.previousStreak
                        )
                    } else {
                        StreakRecoveryFeature.FetchStreakResult.Error
                    }
                )
            }
            StreakRecoveryFeature.InternalAction.RecoverStreak -> {
                onNewMessage(
                    streaksInteractor
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
                )
            }
            StreakRecoveryFeature.InternalAction.CancelStreakRecovery -> {
                onNewMessage(
                    streaksInteractor
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
                )
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