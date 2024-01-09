package org.hyperskill.app.streak_recovery.presentation

import org.hyperskill.app.SharedResources
import org.hyperskill.app.core.view.mapper.ResourceProvider
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

class StreakRecoveryReducer(
    private val resourceProvider: ResourceProvider
) : StateReducer<State, Message, Action> {
    override fun reduce(state: State, message: Message): ReducerResult =
        when (message) {
            Message.Initialize -> {
                state to setOf(InternalAction.FetchStreak)
            }
            is StreakRecoveryFeature.FetchStreakResult.Success -> {
                handleFetchStreakResultSuccessMessage(state, message)
            }
            StreakRecoveryFeature.FetchStreakResult.Error -> {
                null
            }
            Message.RestoreStreakClicked -> {
                if (state.streak != null) {
                    state to setOf(
                        Action.ViewAction.ShowNetworkRequestStatus.Loading,
                        InternalAction.RecoverStreak(state.streak),
                        InternalAction.LogAnalyticEvent(
                            StreakRecoveryModalClickedRestoreStreakHyperskillAnalyticEvent()
                        )
                    )
                } else {
                    state to setOf(
                        InternalAction.LogAnalyticEvent(
                            StreakRecoveryModalClickedRestoreStreakHyperskillAnalyticEvent()
                        ),
                        InternalAction.CaptureSentryErrorMessage("StreakRecovery: restore streak, no streak found")
                    )
                }
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
            is StreakRecoveryFeature.RecoverStreakResult.Success -> {
                state to setOf(
                    Action.ViewAction.ShowNetworkRequestStatus.Success(message.message),
                    Action.ViewAction.HideStreakRecoveryModal
                )
            }
            is StreakRecoveryFeature.RecoverStreakResult.Error -> {
                state to setOf(
                    Action.ViewAction.ShowNetworkRequestStatus.Error(message.message)
                )
            }
            is StreakRecoveryFeature.CancelStreakRecoveryResult.Success -> {
                state to setOf(
                    Action.ViewAction.ShowNetworkRequestStatus.Success(message.message),
                    Action.ViewAction.HideStreakRecoveryModal
                )
            }
            is StreakRecoveryFeature.CancelStreakRecoveryResult.Error -> {
                state to setOf(
                    Action.ViewAction.ShowNetworkRequestStatus.Error(message.message)
                )
            }
            Message.StreakRecoveryModalShownEventMessage -> {
                state to setOf(
                    InternalAction.LogAnalyticEvent(StreakRecoveryModalShownHyperskillAnalyticEvent())
                )
            }
            Message.StreakRecoveryModalHiddenEventMessage -> {
                State() to setOf(
                    InternalAction.LogAnalyticEvent(StreakRecoveryModalHiddenHyperskillAnalyticEvent())
                )
            }
        } ?: (state to emptySet())

    private fun handleFetchStreakResultSuccessMessage(
        state: State,
        message: StreakRecoveryFeature.FetchStreakResult.Success
    ): ReducerResult? =
        if (message.streak.canBeRecovered) {
            val isFirstTimeOffer = message.streak.recoveryPrice == 0
            val nextRecoveryPriceText = if (isFirstTimeOffer) {
                resourceProvider.getQuantityString(
                    SharedResources.plurals.streak_recovery_next_recovery_description,
                    message.streakFreezeProduct.price,
                    message.streakFreezeProduct.price
                )
            } else {
                null
            }

            state.copy(streak = message.streak) to setOf(
                Action.ViewAction.ShowRecoveryStreakModal(
                    recoveryPriceAmountLabel = message.streak.recoveryPrice.toString(),
                    recoveryPriceGemsLabel = resourceProvider.getQuantityString(
                        SharedResources.plurals.gems_without_count, message.streak.recoveryPrice
                    ),
                    modalText = resourceProvider.getString(
                        SharedResources.strings.streak_recovery_modal_text, message.streak.previousStreak
                    ),
                    isFirstTimeOffer = isFirstTimeOffer,
                    nextRecoveryPriceText = nextRecoveryPriceText
                )
            )
        } else {
            null
        }
}