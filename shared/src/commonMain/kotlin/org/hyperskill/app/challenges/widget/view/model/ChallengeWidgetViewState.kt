package org.hyperskill.app.challenges.widget.view.model

sealed interface ChallengeWidgetViewState {
    object Idle : ChallengeWidgetViewState
    data class Loading(val shouldShowSkeleton: Boolean) : ChallengeWidgetViewState
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

val ChallengeWidgetViewState.Content.title: String
    get() = when (this) {
        is ChallengeWidgetViewState.Content.Announcement -> title
        is ChallengeWidgetViewState.Content.HappeningNow -> title
        is ChallengeWidgetViewState.Content.Completed -> title
        is ChallengeWidgetViewState.Content.PartiallyCompleted -> title
        is ChallengeWidgetViewState.Content.Ended -> title
    }

val ChallengeWidgetViewState.Content.description: String
    get() = when (this) {
        is ChallengeWidgetViewState.Content.Announcement -> description
        is ChallengeWidgetViewState.Content.HappeningNow -> description
        is ChallengeWidgetViewState.Content.Completed -> description
        is ChallengeWidgetViewState.Content.PartiallyCompleted -> description
        is ChallengeWidgetViewState.Content.Ended -> description
    }

val ChallengeWidgetViewState.Content.formattedDurationOfTime: String
    get() = when (this) {
        is ChallengeWidgetViewState.Content.Announcement -> formattedDurationOfTime
        is ChallengeWidgetViewState.Content.HappeningNow -> formattedDurationOfTime
        is ChallengeWidgetViewState.Content.Completed -> formattedDurationOfTime
        is ChallengeWidgetViewState.Content.PartiallyCompleted -> formattedDurationOfTime
        is ChallengeWidgetViewState.Content.Ended -> formattedDurationOfTime
    }