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
            val recoveryPriceAmountLabel: String,
            val recoveryPriceGemsLabel: String,
            val modalText: String
        ) : FetchStreakResult

        object Error : FetchStreakResult
    }

    internal sealed interface RecoverStreakResult : Message {
        data class Success(val message: String) : RecoverStreakResult
        data class Error(val message: String) : RecoverStreakResult
    }

    internal sealed interface CancelStreakRecoveryResult : Message {
        data class Success(val message: String) : CancelStreakRecoveryResult
        data class Error(val message: String) : CancelStreakRecoveryResult
    }

    sealed interface Action {
        sealed interface ViewAction : Action {
            data class ShowRecoveryStreakModal(
                val recoveryPriceAmountLabel: String,
                // passed separately from price because price amount is highlighted with bold
                val recoveryPriceGemsLabel: String,
                val modalText: String
            ) : ViewAction

            object HideStreakRecoveryModal : ViewAction

            sealed interface ShowNetworkRequestStatus : ViewAction {
                object Loading : ShowNetworkRequestStatus
                data class Error(val message: String) : ShowNetworkRequestStatus
                data class Success(val message: String) : ShowNetworkRequestStatus
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