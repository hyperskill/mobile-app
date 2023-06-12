package org.hyperskill.app.streak_recovery.presentation

import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent

object StreakRecoveryFeature {
    object State

    sealed interface Message {
        object Initialize : Message

        object RestoreStreakClicked : Message

        object NoThanksClicked : Message

        /**
         * Analytic
         */
        object StreakRecoveryModalShownEventMessage : Message
        object StreakRecoveryModalHiddenEventMessage : Message
    }

    internal sealed interface FetchStreakResult : Message {
        data class Success(
            val canRecoveryStreak: Boolean,
            val recoveryPrice: Int,
            val previousStreak: Int
        ) : FetchStreakResult

        object Error : FetchStreakResult
    }

    internal sealed interface RecoverStreakResult : Message {
        object Success : RecoverStreakResult
        object Error : RecoverStreakResult
    }

    internal sealed interface CancelStreakRecoveryResult : Message {
        object Success : CancelStreakRecoveryResult
        object Error : CancelStreakRecoveryResult
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            data class ShowRecoveryStreakModal(
                val recoveryPrice: Int,
                val previousStreak: Int
            ) : ViewAction

            object HideStreakRecoveryModal : ViewAction

            sealed interface ShowNetworkRequestStatus : ViewAction {
                object Loading : ShowNetworkRequestStatus
                object Error : ShowNetworkRequestStatus
                object Success : ShowNetworkRequestStatus
            }
        }
    }

    internal sealed interface InternalAction : Action {
        object FetchStreak : InternalAction

        object RecoverStreak : InternalAction

        object CancelStreakRecovery : InternalAction

        data class LogAnalyticEvent(val event: HyperskillAnalyticEvent) : InternalAction
    }
}