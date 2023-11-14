package org.hyperskill.challenges.widget.view.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.LocalDate
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.challenges.domain.model.Challenge
import org.hyperskill.app.challenges.domain.model.ChallengeStatus
import org.hyperskill.app.challenges.domain.model.ChallengeTargetType
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature
import org.hyperskill.app.challenges.widget.view.mapper.ChallengeWidgetViewStateMapper
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter

class ChallengeWidgetViewStateMapperTest {
    private val viewStateMapper = ChallengeWidgetViewStateMapper(
        dateFormatter = SharedDateFormatter(ResourceProviderStub()),
        resourceProvider = ResourceProviderStub()
    )

    @Test
    fun `Not started challenge is mapped to Announcement in ViewState`() {
        val given = Challenge(
            id = 6,
            title = "QA  ☾⋆",
            description = "The Challenge! Ho-ho-ho!🎅\r\nHurry up and get yor prise!",
            targetTypeValue = ChallengeTargetType.STEP.value,
            startingDate = LocalDate.parse("2023-11-02"),
            intervalDurationDays = 1,
            intervalsCount = 1,
            statusValue = ChallengeStatus.NOT_STARTED.value,
            rewardLink = null,
            progress = listOf(false),
            finishDate = LocalDate.parse("2023-11-03"),
            currentInterval = null
        )

        val expected = ChallengeWidgetViewState.Content.Announcement(
            title = "QA  ☾⋆",
            description = "The Challenge! Ho-ho-ho!🎅\r\nHurry up and get yor prise!",
            formattedDurationOfTime = "2 Nov - 3 Nov",
            startsInState = ChallengeWidgetViewState.Content.Announcement.StartsInState.Deadline
        )
        val actual = viewStateMapper.map(ChallengeWidgetFeature.State.Content(challenges = listOf(given)))

        assertEquals(expected, actual)
    }
}