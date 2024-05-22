package org.hyperskill.app.challenges.widget.view.mapper

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.hyperskill.app.SharedResources
import org.hyperskill.app.challenges.domain.model.Challenge
import org.hyperskill.app.challenges.domain.model.ChallengeStatus
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter

class ChallengeWidgetViewStateMapper(
    private val dateFormatter: SharedDateFormatter,
    private val resourceProvider: ResourceProvider
) {
    fun map(state: ChallengeWidgetFeature.State): ChallengeWidgetViewState =
        when (state) {
            ChallengeWidgetFeature.State.Idle -> ChallengeWidgetViewState.Idle
            ChallengeWidgetFeature.State.Error -> ChallengeWidgetViewState.Error
            is ChallengeWidgetFeature.State.Loading ->
                ChallengeWidgetViewState.Loading(shouldShowSkeleton = !state.isLoadingSilently)
            is ChallengeWidgetFeature.State.Content -> getLoadedWidgetContent(state)
        }

    private fun getLoadedWidgetContent(state: ChallengeWidgetFeature.State.Content): ChallengeWidgetViewState {
        val challenge = state.challenge ?: return ChallengeWidgetViewState.Empty
        val challengeStatus = challenge.status ?: return ChallengeWidgetViewState.Empty

        val headerData = getHeaderData(
            challenge = challenge,
            challengeStatus = challengeStatus
        )

        return when (challengeStatus) {
            ChallengeStatus.NOT_STARTED -> {
                ChallengeWidgetViewState.Content.Announcement(
                    headerData = headerData,
                    startsInState = getStartsInState(challenge)
                )
            }
            ChallengeStatus.STARTED -> {
                ChallengeWidgetViewState.Content.HappeningNow(
                    headerData = headerData,
                    completeInState = getCompleteInState(challenge),
                    progressStatuses = getProgressStatuses(challenge)
                )
            }
            ChallengeStatus.COMPLETED -> {
                ChallengeWidgetViewState.Content.Completed(
                    headerData = headerData,
                    collectRewardButtonState = getCollectRewardButtonState(
                        challengeStatus = challengeStatus,
                        rewardLink = challenge.rewardLink
                    ),
                    isLoadingMagicLink = state.isLoadingMagicLink
                )
            }
            ChallengeStatus.PARTIAL_COMPLETED -> {
                ChallengeWidgetViewState.Content.PartiallyCompleted(
                    headerData = headerData,
                    collectRewardButtonState = getCollectRewardButtonState(
                        challengeStatus = challengeStatus,
                        rewardLink = challenge.rewardLink
                    ),
                    isLoadingMagicLink = state.isLoadingMagicLink
                )
            }
            ChallengeStatus.NOT_COMPLETED -> {
                ChallengeWidgetViewState.Content.Ended(headerData = headerData)
            }
        }
    }

    private fun getHeaderData(
        challenge: Challenge,
        challengeStatus: ChallengeStatus
    ): ChallengeWidgetViewState.Content.HeaderData {
        val description = when (challengeStatus) {
            ChallengeStatus.NOT_STARTED,
            ChallengeStatus.STARTED ->
                challenge.description
            ChallengeStatus.COMPLETED ->
                resourceProvider.getString(SharedResources.strings.challenge_widget_status_completed_title)
            ChallengeStatus.PARTIAL_COMPLETED ->
                resourceProvider.getString(SharedResources.strings.challenge_widget_status_partial_completed_title)
            ChallengeStatus.NOT_COMPLETED ->
                resourceProvider.getString(SharedResources.strings.challenge_widget_status_not_completed_title)
        }

        return ChallengeWidgetViewState.Content.HeaderData(
            title = challenge.title,
            description = description,
            formattedDurationOfTime = getFormattedDurationOfTime(
                start = challenge.start,
                end = challenge.end
            )
        )
    }

    private fun getFormattedDurationOfTime(start: Instant, end: Instant): String {
        val startDate = start.toLocalDateTime(TimeZone.currentSystemDefault()).date
        if (start == end) {
            return dateFormatter.formatDayNumericAndMonthShort(startDate)
        }

        val formattedStartingDate = dateFormatter.formatDayNumericAndMonthShort(startDate)

        val endDate = end.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val formattedFinishDate = dateFormatter.formatDayNumericAndMonthShort(endDate)

        return "$formattedStartingDate - $formattedFinishDate"
    }

    private fun calculateSecondsRemaining(deadline: Instant): Long =
        (deadline - Clock.System.now()).inWholeSeconds

    private fun getStartsInState(challenge: Challenge): ChallengeWidgetViewState.Content.Announcement.StartsInState {
        val timeRemaining = calculateSecondsRemaining(deadline = challenge.start)
        return if (timeRemaining > 0) {
            ChallengeWidgetViewState.Content.Announcement.StartsInState.TimeRemaining(
                title = resourceProvider.getString(SharedResources.strings.challenge_widget_starts_in_text),
                subtitle = dateFormatter.formatDaysWithHoursAndMinutesCount(seconds = timeRemaining)
            )
        } else {
            ChallengeWidgetViewState.Content.Announcement.StartsInState.Deadline
        }
    }

    private fun getCollectRewardButtonState(
        challengeStatus: ChallengeStatus,
        rewardLink: String?
    ): ChallengeWidgetViewState.Content.CollectRewardButtonState =
        if (
            (challengeStatus == ChallengeStatus.COMPLETED || challengeStatus == ChallengeStatus.PARTIAL_COMPLETED) &&
            !rewardLink.isNullOrEmpty()
        ) {
            ChallengeWidgetViewState.Content.CollectRewardButtonState.Visible(
                title = resourceProvider.getString(SharedResources.strings.challenge_widget_collect_reward_button_title)
            )
        } else {
            ChallengeWidgetViewState.Content.CollectRewardButtonState.Hidden
        }

    private fun getCompleteInState(
        challenge: Challenge
    ): ChallengeWidgetViewState.Content.HappeningNow.CompleteInState {
        val nextIntervalTime =
            challenge.nextIntervalTime
                ?: return ChallengeWidgetViewState.Content.HappeningNow.CompleteInState.Empty
        val secondsRemaining = calculateSecondsRemaining(deadline = nextIntervalTime)
        return if (secondsRemaining > 0) {
            ChallengeWidgetViewState.Content.HappeningNow.CompleteInState.TimeRemaining(
                title = resourceProvider.getString(SharedResources.strings.challenge_widget_complete_in_text),
                subtitle = dateFormatter.formatDaysWithHoursAndMinutesCount(seconds = secondsRemaining)
            )
        } else {
            ChallengeWidgetViewState.Content.HappeningNow.CompleteInState.Deadline
        }
    }

    private fun getProgressStatuses(
        challenge: Challenge
    ): List<ChallengeWidgetViewState.Content.HappeningNow.ProgressStatus> {
        if (challenge.currentInterval == null || challenge.intervalsCount != challenge.progress.size) {
            return emptyList()
        }

        return challenge.progress.mapIndexed { index, progressBoolValue ->
            val isPreviousInterval = index + 1 < challenge.currentInterval
            val isCurrentInterval = index + 1 == challenge.currentInterval

            when {
                isPreviousInterval -> if (progressBoolValue) {
                    ChallengeWidgetViewState.Content.HappeningNow.ProgressStatus.COMPLETED
                } else {
                    ChallengeWidgetViewState.Content.HappeningNow.ProgressStatus.MISSED
                }
                isCurrentInterval -> if (progressBoolValue) {
                    ChallengeWidgetViewState.Content.HappeningNow.ProgressStatus.COMPLETED
                } else {
                    ChallengeWidgetViewState.Content.HappeningNow.ProgressStatus.ACTIVE
                }
                else -> ChallengeWidgetViewState.Content.HappeningNow.ProgressStatus.INACTIVE
            }
        }
    }
}