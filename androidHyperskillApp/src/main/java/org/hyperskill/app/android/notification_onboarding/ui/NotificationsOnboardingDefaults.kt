package org.hyperskill.app.android.notification_onboarding.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.android.core.view.ui.widget.compose.centerWithVerticalBias

object NotificationsOnboardingDefaults {

    val HorizontalPadding: Dp = 20.dp
    val TopPadding: Dp = 20.dp
    val BottomPadding: Dp = 32.dp

    val SetLaterBtnPadding: Dp = 8.dp

    val TitleMediumTextStyle: TextStyle
        @Composable
        get() = MaterialTheme.typography.subtitle1.copy(
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        )

    val ContentAlignment: Alignment = Alignment.centerWithVerticalBias(-0.5f)
}