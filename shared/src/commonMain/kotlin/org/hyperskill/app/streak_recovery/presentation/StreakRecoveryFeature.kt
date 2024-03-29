package org.hyperskill.app.streak_recovery.presentation

import kotlinx.serialization.Serializable
import org.hyperskill.app.analytic.domain.model.hyperskill.HyperskillAnalyticEvent
import org.hyperskill.app.products.domain.model.Product
import org.hyperskill.app.streaks.domain.model.Streak

object StreakRecoveryFeature {
    @Serializable
    data class State(val streak: Streak? = null)

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
            val streak: Streak,
            val streakFreezeProduct: Product
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
            @Serializable
            data class ShowRecoveryStreakModal(
                val recoveryPriceAmountLabel: String,
                // passed separately from price because price amount is highlighted with bold
                val recoveryPriceGemsLabel: String,
                val modalText: String,
                val isFirstTimeOffer: Boolean,
                val nextRecoveryPriceText: String?
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

        data class RecoverStreak(val streak: Streak) : InternalAction

        object CancelStreakRecovery : InternalAction

        data class CaptureErrorMessage(val message: String) : InternalAction
        data class LogAnalyticEvent(val event: HyperskillAnalyticEvent) : InternalAction
    }
}