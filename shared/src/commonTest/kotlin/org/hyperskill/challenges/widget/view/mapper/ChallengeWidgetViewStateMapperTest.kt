package org.hyperskill.challenges.widget.view.mapper

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.datetime.Instant
import org.hyperskill.ResourceProviderStub
import org.hyperskill.app.challenges.domain.model.Challenge
import org.hyperskill.app.challenges.domain.model.ChallengeStatus
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature
import org.hyperskill.app.challenges.widget.view.mapper.ChallengeWidgetViewStateMapper
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState
import org.hyperskill.app.content_type.domain.model.ContentType
import org.hyperskill.app.core.view.mapper.date.SharedDateFormatter

class ChallengeWidgetViewStateMapperTest {

    companion object {
        private const val TITLE = "QA  ☾⋆"
        private const val DESCRIPTION = "The Challenge! Ho-ho-ho!🎅\nHurry up and get yor prise!"
    }

    private val viewStateMapper = ChallengeWidgetViewStateMapper(
        dateFormatter = SharedDateFormatter(ResourceProviderStub()),
        resourceProvider = ResourceProviderStub()
    )

    @Test
    fun `Not started challenge is mapped to Announcement in ViewState`() {
        val given = Challenge(
            id = 6,
            title = TITLE,
            description = DESCRIPTION,
            targetType = ContentType.STEP,
            start = Instant.parse("2023-11-02T04:00:00Z"),
            end = Instant.parse("2023-11-03T04:00:00Z"),
            intervalsCount = 1,
            statusValue = ChallengeStatus.NOT_STARTED.value,
            rewardLink = null,
            progress = listOf(false),
            currentInterval = null,
            nextIntervalTime = Instant.parse("2023-11-03T04:00:00Z")
        )

        val expected = ChallengeWidgetViewState.Content.Announcement(
            headerData = ChallengeWidgetViewState.Content.HeaderData(
                title = TITLE,
                description = DESCRIPTION,
                formattedDurationOfTime = "2 Nov - 3 Nov"
            ),
            startsInState = ChallengeWidgetViewState.Content.Announcement.StartsInState.Deadline
        )
        val actual = viewStateMapper.map(ChallengeWidgetFeature.State.Content(challenge = given))

        assertEquals(expected, actual)
    }
}