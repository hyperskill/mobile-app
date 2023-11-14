package org.hyperskill.app.challenges.widget.view.mapper

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.hyperskill.app.SharedResources
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
            ChallengeWidgetFeature.State.Loading -> ChallengeWidgetViewState.Loading
            ChallengeWidgetFeature.State.Error -> ChallengeWidgetViewState.Error
            is ChallengeWidgetFeature.State.Content -> getLoadedWidgetContent(state)
        }

    private fun getLoadedWidgetContent(state: ChallengeWidgetFeature.State.Content): ChallengeWidgetViewState {
        val challenge = state.getCurrentChallenge() ?: return ChallengeWidgetViewState.Empty
        val challengeStatus = challenge.status ?: return ChallengeWidgetViewState.Empty

        val challengeTitle = challenge.title
        val challengeDescription = challenge.description
        val formattedDurationOfTime = getFormattedDurationOfTime(
            startingDate = challenge.startingDate,
            finishDate = challenge.finishDate
        )
        val isRewardLinkAvailable = challenge.rewardLink.isNullOrEmpty().not()

        return when (challengeStatus) {
            ChallengeStatus.NOT_STARTED -> {
                val timeRemaining = calculateTimeRemaining(deadline = challenge.startingDate)
                val startsInState = if (timeRemaining > 0) {
                    ChallengeWidgetViewState.Content.Announcement.StartsInState.TimeRemaining(
                        title = resourceProvider.getString(SharedResources.strings.challenge_widget_starts_in_text),
                        subtitle = dateFormatter.formatHoursWithMinutesCount(seconds = timeRemaining)
                    )
                } else {
                    ChallengeWidgetViewState.Content.Announcement.StartsInState.Deadline
                }

                ChallengeWidgetViewState.Content.Announcement(
                    title = challengeTitle,
                    description = challengeDescription,
                    formattedDurationOfTime = formattedDurationOfTime,
                    startsInState = startsInState
                )
            }
            ChallengeStatus.STARTED -> TODO()
            ChallengeStatus.COMPLETED -> TODO()
            ChallengeStatus.PARTIAL_COMPLETED -> {
                ChallengeWidgetViewState.Content.PartiallyCompleted(
                    title = challengeTitle,
                    description = challengeStatus.getTitle(resourceProvider),
                    formattedDurationOfTime = formattedDurationOfTime,
                    collectRewardButtonState = if (isRewardLinkAvailable) {
                        ChallengeWidgetViewState.Content.CollectRewardButtonState.Visible(
                            title = resourceProvider.getString(
                                SharedResources.strings.challenge_widget_collect_reward_button_title
                            )
                        )
                    } else {
                        ChallengeWidgetViewState.Content.CollectRewardButtonState.Hidden
                    }
                )
            }
            ChallengeStatus.NOT_COMPLETED -> {
                ChallengeWidgetViewState.Content.Ended(
                    title = challengeTitle,
                    description = challengeStatus.getTitle(resourceProvider),
                    formattedDurationOfTime = formattedDurationOfTime
                )
            }
        }
    }

    private fun getFormattedDurationOfTime(startingDate: LocalDate, finishDate: LocalDate): String {
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
}

private fun ChallengeStatus.getTitle(resourceProvider: ResourceProvider): String =
    when (this) {
        ChallengeStatus.NOT_STARTED ->
            resourceProvider.getString(SharedResources.strings.challenge_widget_status_not_started_title)
        ChallengeStatus.STARTED ->
            resourceProvider.getString(SharedResources.strings.challenge_widget_status_started_title)
        ChallengeStatus.NOT_COMPLETED ->
            resourceProvider.getString(SharedResources.strings.challenge_widget_status_not_completed_title)
        ChallengeStatus.PARTIAL_COMPLETED ->
            resourceProvider.getString(SharedResources.strings.challenge_widget_status_partial_completed_title)
        ChallengeStatus.COMPLETED ->
            resourceProvider.getString(SharedResources.strings.challenge_widget_status_completed_title)
    }