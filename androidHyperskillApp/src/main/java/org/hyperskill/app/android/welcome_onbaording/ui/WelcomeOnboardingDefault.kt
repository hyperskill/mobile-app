package org.hyperskill.app.android.welcome_onbaording.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.R

object WelcomeOnboardingDefault {
    val horizontalPadding: Dp = 20.dp
    val buttonBottomPadding: Dp = 32.dp

    val titleTextStyle: TextStyle
        @Composable
        get() = TextStyle(

            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(id = R.color.text_primary)
        )
}