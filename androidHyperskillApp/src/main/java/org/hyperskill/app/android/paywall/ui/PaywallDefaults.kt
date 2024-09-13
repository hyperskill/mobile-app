package org.hyperskill.app.android.paywall.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import org.hyperskill.app.R

object PaywallDefaults {
    val ContentPadding = PaddingValues(
        horizontal = 20.dp,
        vertical = 24.dp
    )

    val BackgroundColor: Color
        @Composable
        get() = colorResource(id = R.color.layer_1)
}