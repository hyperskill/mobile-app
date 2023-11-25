package org.hyperskill.app.challenges.widget.view.model

sealed interface ChallengeWidgetViewState {
    object Idle : ChallengeWidgetViewState
    data class Loading(val shouldShowSkeleton: Boolean) : ChallengeWidgetViewState
    object Error : ChallengeWidgetViewState
    object Empty : ChallengeWidgetViewState

    interface WithHeaderData {
        val headerData: Content.HeaderData
    }

    sealed interface Content : ChallengeWidgetViewState, WithHeaderData {
        data class Announcement(
            override val headerData: HeaderData,
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
            override val headerData: HeaderData,
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
            override val headerData: HeaderData,
            val collectRewardButtonState: CollectRewardButtonState,
            val isLoadingMagicLink: Boolean
        ) : Content

        data class PartiallyCompleted(
            override val headerData: HeaderData,
            val collectRewardButtonState: CollectRewardButtonState,
            val isLoadingMagicLink: Boolean
        ) : Content

        data class Ended(
            override val headerData: HeaderData
        ) : Content

        data class HeaderData(
            val title: String,
            val description: String,
            val formattedDurationOfTime: String
        )

        sealed interface CollectRewardButtonState {
            object Hidden : CollectRewardButtonState
            data class Visible(val title: String) : CollectRewardButtonState
        }
    }
}

val ChallengeWidgetViewState.Content.isLoadingMagicLink: Boolean
    get() = when (this) {
        is ChallengeWidgetViewState.Content.Completed -> isLoadingMagicLink
        is ChallengeWidgetViewState.Content.PartiallyCompleted -> isLoadingMagicLink
        else -> false
    }