package org.hyperskill.app.android.challenge.ui

import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState.Content.HappeningNow.ProgressStatus

object ChallengeCardPreviewValues {
    const val TITLE = "Advent Streak Challenge"
    const val DATE_TEXT = "6 Oct - 12 Oct"
    /*ktlint-disable*/
    const val DESCRIPTION = "Get ready to push your limits! <a href=\"https://developer.android.com/jetpack/androidx/releases/compose\">Thrilling daily programming competition designed to test your coding skills and problem-solving abilities</a>."
    const val TIME_TITLE = "Start in"
    const val TIME_SUBTITLE = DATE_TEXT
    val headerData: ChallengeWidgetViewState.Content.HeaderData =
        ChallengeWidgetViewState.Content.HeaderData(
            title = TITLE,
            description =  DESCRIPTION,
            formattedDurationOfTime = DATE_TEXT
        )
    val statusHeaderData: ChallengeWidgetViewState.Content.HeaderData =
        ChallengeWidgetViewState.Content.HeaderData(
            title = TITLE,
            description = "Well done, challenge completed!",
            formattedDurationOfTime = DATE_TEXT
        )

    val progress: List<ProgressStatus> = listOf(
        ProgressStatus.COMPLETED,
        ProgressStatus.COMPLETED,
        ProgressStatus.COMPLETED,
        ProgressStatus.COMPLETED,
        ProgressStatus.COMPLETED,
        ProgressStatus.COMPLETED,
        ProgressStatus.ACTIVE,
        ProgressStatus.ACTIVE,
        ProgressStatus.ACTIVE,
        ProgressStatus.ACTIVE,
        ProgressStatus.ACTIVE,
        ProgressStatus.ACTIVE,
        ProgressStatus.MISSED,
        ProgressStatus.MISSED,
        ProgressStatus.MISSED,
        ProgressStatus.MISSED,
        ProgressStatus.MISSED,
    )
}