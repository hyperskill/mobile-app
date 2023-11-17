package org.hyperskill.app.challenges.widget.view.model

sealed interface ChallengeWidgetViewState {
    object Idle : ChallengeWidgetViewState
    data class Loading(val shouldShowSkeleton: Boolean) : ChallengeWidgetViewState
    object Error : ChallengeWidgetViewState
    object Empty : ChallengeWidgetViewState

    sealed interface Content : ChallengeWidgetViewState {
        data class Announcement(
            override val headerData: HeaderData,
            val startsInState: StartsInState
        ) : Content, WithHeaderData {
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
        ) : Content, WithHeaderData {
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
        ) : Content, WithHeaderData

        data class PartiallyCompleted(
            override val headerData: HeaderData,
            val collectRewardButtonState: CollectRewardButtonState,
            val isLoadingMagicLink: Boolean
        ) : Content, WithHeaderData

        data class Ended(
            override val headerData: HeaderData
        ) : Content, WithHeaderData

        data class HeaderData(
            val title: String,
            val description: String,
            val formattedDurationOfTime: String
        )

        interface WithHeaderData {
            val headerData: HeaderData
        }

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

val ChallengeWidgetViewState.Content.headerData: ChallengeWidgetViewState.Content.HeaderData
    get() = when (this) {
        is ChallengeWidgetViewState.Content.Announcement -> headerData
        is ChallengeWidgetViewState.Content.HappeningNow -> headerData
        is ChallengeWidgetViewState.Content.Completed -> headerData
        is ChallengeWidgetViewState.Content.PartiallyCompleted -> headerData
        is ChallengeWidgetViewState.Content.Ended -> headerData
    }