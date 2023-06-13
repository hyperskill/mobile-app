package org.hyperskill.app.streak_recovery.presentation

import org.hyperskill.app.streak_recovery.domain.analytic.StreakRecoveryModalClickedNoThanksHyperskillAnalyticEvent
import org.hyperskill.app.streak_recovery.domain.analytic.StreakRecoveryModalClickedRestoreStreakHyperskillAnalyticEvent
import org.hyperskill.app.streak_recovery.domain.analytic.StreakRecoveryModalHiddenHyperskillAnalyticEvent
import org.hyperskill.app.streak_recovery.domain.analytic.StreakRecoveryModalShownHyperskillAnalyticEvent
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature.Action
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature.InternalAction
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature.Message
import org.hyperskill.app.streak_recovery.presentation.StreakRecoveryFeature.State
import ru.nobird.app.presentation.redux.reducer.StateReducer

private typealias ReducerResult = Pair<State, Set<Action>>

class StreakRecoveryReducer : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            Message.Initialize -> {
                state to setOf(InternalAction.FetchStreak)
            }
            is StreakRecoveryFeature.FetchStreakResult.Success -> {
                if (message.canRecoveryStreak) {
                    state to setOf(
                        Action.ViewAction.ShowRecoveryStreakModal(
                            message.recoveryPrice,
                            message.recoveryPriceLabel,
                            message.modalText
                        )
                    )
                } else {
                    null
                }
            }
            StreakRecoveryFeature.FetchStreakResult.Error -> {
                null
            }
            Message.RestoreStreakClicked -> {
                state to setOf(
                    Action.ViewAction.ShowNetworkRequestStatus.Loading,
                    InternalAction.RecoverStreak,
                    InternalAction.LogAnalyticEvent(
                        StreakRecoveryModalClickedRestoreStreakHyperskillAnalyticEvent()
                    )
                )
            }
            Message.NoThanksClicked -> {
                state to setOf(
                    Action.ViewAction.ShowNetworkRequestStatus.Loading,
                    InternalAction.CancelStreakRecovery,
                    InternalAction.LogAnalyticEvent(
                        StreakRecoveryModalClickedNoThanksHyperskillAnalyticEvent()
                    )
                )
            }
            StreakRecoveryFeature.RecoverStreakResult.Success -> {
                state to setOf(
                    Action.ViewAction.ShowNetworkRequestStatus.Success,
                    Action.ViewAction.HideStreakRecoveryModal
                )
            }
            StreakRecoveryFeature.RecoverStreakResult.Error -> {
                handleNetworkError(state)
            }
            StreakRecoveryFeature.CancelStreakRecoveryResult.Success -> {
                state to setOf(
                    Action.ViewAction.ShowNetworkRequestStatus.Success,
                    Action.ViewAction.HideStreakRecoveryModal
                )
            }
            StreakRecoveryFeature.CancelStreakRecoveryResult.Error -> {
                handleNetworkError(state)
            }
            Message.StreakRecoveryModalHiddenEventMessage -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(StreakRecoveryModalShownHyperskillAnalyticEvent())
                )
            }
            Message.StreakRecoveryModalShownEventMessage -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(StreakRecoveryModalHiddenHyperskillAnalyticEvent())
                )
            }
        } ?: (state to emptySet())

    private fun handleNetworkError(state: State): ReducerResult =
        state to setOf(Action.ViewAction.ShowNetworkRequestStatus.Error)
}