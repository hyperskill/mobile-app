package org.hyperskill.app.android.challenge.ui

object ChallengeCardPreviewValues {
    const val TITLE = "Advent Streak Challenge"
    const val DATE_TEXT = "6 Oct - 12 Oct"
    const val DESCRIPTION = "Get ready to push your limits! Thrilling daily programming competition designed to test your coding skills and problem-solving abilities. What to Expect"
    const val TIME_TITLE = "Start in"
    const val TIME_SUBTITLE = "6 hrs 27 mins"
    val progress: List<ProgressStatus> = listOf(
        ProgressStatus.COMPLETED,
        ProgressStatus.COMPLETED,
        ProgressStatus.COMPLETED,
        ProgressStatus.MISSED,
        ProgressStatus.ACTIVE,
        ProgressStatus.INACTIVE,
        ProgressStatus.INACTIVE,
        ProgressStatus.INACTIVE,
        ProgressStatus.INACTIVE,
        ProgressStatus.INACTIVE,
        ProgressStatus.INACTIVE,
        ProgressStatus.INACTIVE,
        ProgressStatus.INACTIVE,
        ProgressStatus.INACTIVE
    )
}