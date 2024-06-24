package org.hyperskill.app.android.topic_completion.ui

import androidx.compose.ui.unit.dp
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

object TopicCompletedDefaults {
    const val ENTER_TRANSITION_DURATION_MILLIS = 600

    val DESCRIPTION_TYPING_DELAY_MILLIS: Duration = (ENTER_TRANSITION_DURATION_MILLIS - 200).milliseconds

    val CONTENT_VERTICAL_PADDING = 17.dp
    val CTA_HORIZONTAL_PADDING = 20.dp
    val CLOSE_BUTTON_PADDING = 14.dp

    val CLOSE_BUTTON_SIZE = 24.dp
    val SPACE_BOT_AVATAR_SIZE = 228.dp
}