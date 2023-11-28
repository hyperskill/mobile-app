package org.hyperskill.app.android.challenge.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

object ChallengeCardDefaults {
    val verticalSpacing: Dp = 16.dp
    val paddingValues: PaddingValues = PaddingValues(20.dp)
    const val PROGRESS_ITEMS_IN_ROW: Int = 7
    val progressItemSize: DpSize = DpSize(width = 32.dp, height = 12.dp)
}