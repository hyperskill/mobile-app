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
            val title: String,
            val description: String,
            val formattedDurationOfTime: String,
            val completeInState: CompleteInState,
            val progressStatuses: List<ProgressStatus>
        ) : Content {
            sealed interface CompleteInState {
                object Empty : CompleteInState
                object Deadline : CompleteInState
                data class TimeRemaining(
                    val title: String,
                    val subtitle: String
                ) : CompleteInState
            }

            enum class ProgressStatus {
                INACTIVE,
                ACTIVE,
                COMPLETED,
                MISSED
            }
        }

        data class Completed(
            val title: String,
            val description: String,
            val formattedDurationOfTime: String,
            val collectRewardButtonState: CollectRewardButtonState
        ) : Content

        data class PartiallyCompleted(
            val title: String,
            val description: String,
            val formattedDurationOfTime: String,
            val collectRewardButtonState: CollectRewardButtonState
        ) : Content

        data class Ended(
            val title: String,
            val description: String,
            val formattedDurationOfTime: String
        ) : Content

        sealed interface CollectRewardButtonState {
            object Hidden : CollectRewardButtonState
            data class Visible(val title: String) : CollectRewardButtonState
        }
    }
}