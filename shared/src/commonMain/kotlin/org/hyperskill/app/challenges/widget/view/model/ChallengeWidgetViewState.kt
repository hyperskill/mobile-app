package org.hyperskill.app.challenges.widget.view.model

sealed interface ChallengeWidgetViewState {
    object Idle : ChallengeWidgetViewState
    object Loading : ChallengeWidgetViewState
    object Error : ChallengeWidgetViewState
    object Empty : ChallengeWidgetViewState

    sealed interface Content : ChallengeWidgetViewState {
        data class Announcement(
            val title: String,
            val description: String,
            val formattedDurationOfTime: String,
            val startsInState: StartsInState
        ) : Content {
            sealed interface StartsInState {
                object Deadline : StartsInState
                data class TimeRemaining(
                    val title: String,
                    val subtitle: String
                ) : StartsInState
            }
        }

        data class HappeningNow(
            val title: String
        ) : Content

        data class Completed(
            val title: String
        ) : Content

        data class PartiallyCompleted(
            val title: String
        ) : Content

        data class Ended(
            val title: String,
            val description: String,
            val formattedDurationOfTime: String
        ) : Content
    }
}