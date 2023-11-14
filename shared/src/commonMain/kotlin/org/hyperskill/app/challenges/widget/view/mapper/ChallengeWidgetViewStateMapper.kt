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
        val currentChallenge = state.getCurrentChallenge() ?: return ChallengeWidgetViewState.Empty
        val currentChallengeStatus = currentChallenge.status ?: return ChallengeWidgetViewState.Empty

        val title = currentChallenge.title
        val description = currentChallenge.description
        val formattedDurationOfTime = getFormattedDurationOfTime(
            startingDate = currentChallenge.startingDate,
            finishDate = currentChallenge.finishDate
        )

        return when (currentChallengeStatus) {
            ChallengeStatus.NOT_STARTED -> {
                val timeRemaining = calculateTimeRemaining(deadline = currentChallenge.startingDate)
                val startsInState = if (timeRemaining > 0) {
                    ChallengeWidgetViewState.Content.Announcement.StartsInState.TimeRemaining(
                        title = resourceProvider.getString(SharedResources.strings.challenge_widget_starts_in_text),
                        subtitle = dateFormatter.formatHoursWithMinutesCount(seconds = timeRemaining)
                    )
                } else {
                    ChallengeWidgetViewState.Content.Announcement.StartsInState.Deadline
                }

                ChallengeWidgetViewState.Content.Announcement(
                    title = title,
                    description = description,
                    formattedDurationOfTime = formattedDurationOfTime,
                    startsInState = startsInState
                )
            }
            ChallengeStatus.STARTED -> TODO()
            ChallengeStatus.COMPLETED -> TODO()
            ChallengeStatus.PARTIAL_COMPLETED -> TODO()
            ChallengeStatus.NOT_COMPLETED -> TODO()
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