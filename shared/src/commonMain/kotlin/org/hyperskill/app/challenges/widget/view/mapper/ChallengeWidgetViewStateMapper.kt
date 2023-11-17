package org.hyperskill.app.challenges.widget.view.mapper

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.hyperskill.app.SharedResources
import org.hyperskill.app.challenges.domain.model.Challenge
import org.hyperskill.app.challenges.domain.model.ChallengeStatus
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature
import org.hyperskill.app.challenges.widget.presentation.getCurrentChallenge
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState
import org.hyperskill.app.core.view.mapper.ResourceProvider
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter

class ChallengeWidgetViewStateMapper(
    private val dateFormatter: SharedDateFormatter,
    private val resourceProvider: ResourceProvider
) {
    companion object {
        private val TIME_ZONE_NYC = TimeZone.of("America/New_York")
    }

    fun map(state: ChallengeWidgetFeature.State): ChallengeWidgetViewState =
        when (state) {
            ChallengeWidgetFeature.State.Idle -> ChallengeWidgetViewState.Idle
            ChallengeWidgetFeature.State.Error -> ChallengeWidgetViewState.Error
            is ChallengeWidgetFeature.State.Loading ->
                ChallengeWidgetViewState.Loading(shouldShowSkeleton = !state.isLoadingSilently)
            is ChallengeWidgetFeature.State.Content -> getLoadedWidgetContent(state)
        }

    private fun getLoadedWidgetContent(state: ChallengeWidgetFeature.State.Content): ChallengeWidgetViewState {
        val challenge = state.getCurrentChallenge() ?: return ChallengeWidgetViewState.Empty
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
                startingDate = challenge.startingDate,
                finishDate = challenge.finishDate
            )
        )
    }

    private fun getFormattedDurationOfTime(startingDate: LocalDate, finishDate: LocalDate): String {
        if (startingDate == finishDate) {
            return dateFormatter.formatDayNumericAndMonthShort(startingDate)
        }

        val formattedStartingDate = dateFormatter.formatDayNumericAndMonthShort(startingDate)
        val formattedFinishDate = dateFormatter.formatDayNumericAndMonthShort(finishDate)

        return "$formattedStartingDate - $formattedFinishDate"
    }

    private fun calculateTimeRemaining(deadline: LocalDate): Long {
        val deadlineInstant = LocalDateTime(
            date = deadline,
            time = LocalTime(0, 0, 0, 0)
        ).toInstant(TIME_ZONE_NYC)

        val nowInNewYork = Clock.System.now()
            .toLocalDateTime(TIME_ZONE_NYC)
            .toInstant(TIME_ZONE_NYC)

        return (deadlineInstant - nowInNewYork).inWholeSeconds
    }

    private fun getStartsInState(challenge: Challenge): ChallengeWidgetViewState.Content.Announcement.StartsInState {
        val timeRemaining = calculateTimeRemaining(deadline = challenge.startingDate)
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
        if (challenge.currentInterval == null) {
            return ChallengeWidgetViewState.Content.HappeningNow.CompleteInState.Empty
        }

        val nextDeadline =
            challenge.startingDate.plus(DatePeriod(days = challenge.currentInterval * challenge.intervalDurationDays))
        val timeRemaining = calculateTimeRemaining(deadline = nextDeadline)

        return if (timeRemaining > 0) {
            ChallengeWidgetViewState.Content.HappeningNow.CompleteInState.TimeRemaining(
                title = resourceProvider.getString(SharedResources.strings.challenge_widget_complete_in_text),
                subtitle = dateFormatter.formatDaysWithHoursAndMinutesCount(seconds = timeRemaining)
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