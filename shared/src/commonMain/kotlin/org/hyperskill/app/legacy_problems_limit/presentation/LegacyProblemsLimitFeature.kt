package org.hyperskill.app.legacy_problems_limit.presentation

import kotlin.time.Duration
import org.hyperskill.app.subscriptions.domain.model.Subscription

// Used for ios app compatibility
// TODO: ALTAPPS-1226 remove problems limit widget on ios
object LegacyProblemsLimitFeature {

    sealed interface State {
        object Idle : State

        object Loading : State

        data class Content(
            val subscription: Subscription,
            val isFreemiumWrongSubmissionChargeLimitsEnabled: Boolean,
            val updateIn: Duration?,
            internal val isRefreshing: Boolean = false
        ) : State

        object NetworkError : State
    }

    sealed interface ViewState {
        object Idle : ViewState

        object Loading : ViewState

        sealed interface Content : ViewState {
            object Empty : Content

            /**
             * [progress] represent progress from 0.0 to 1.0.
             * 1.0 means the user haven't passed any step.
             * 0.0 means the user passed all the available steps.
             */
            data class Widget(
                val progress: Float,
                val stepsLimitLabel: String,
                val updateInLabel: String?
            ) : Content
        }

        object Error : ViewState
    }

    sealed interface Message {
        object RetryContentLoading : Message
    }

    sealed interface Action {
        sealed interface ViewAction : Action
    }
}