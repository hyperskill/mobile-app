package org.hyperskill.app.android.welcome_onbaording.root.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import org.hyperskill.app.android.R
import org.hyperskill.app.R as SharedR

object WelcomeOnboardingDefault {
    val horizontalPadding: Dp = 20.dp
    val buttonBottomPadding: Dp = 32.dp

    val runActionButtonShimmerDelay: Duration = 400.milliseconds

    /**
     * @see R.anim.slide_in duration
     */
    val startTypingAnimationDelayMillis: Duration
        @Composable
        get() = integerResource(id = android.R.integer.config_mediumAnimTime).milliseconds

    val titleTextStyle: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(id = SharedR.color.text_primary)
        )
}