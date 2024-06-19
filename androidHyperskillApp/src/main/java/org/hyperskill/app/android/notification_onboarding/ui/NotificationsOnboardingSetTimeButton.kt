package org.hyperskill.app.android.notification_onboarding.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotificationsOnboardingSetTimeButton(
    formattedInterval: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val currentOnClick by rememberUpdatedState {
        view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
        onClick()
    }
    Surface(
        onClick = currentOnClick,
        color = colorResource(id = R.color.layer_1),
        shape = RoundedCornerShape(32.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.notifications_onboarding_set_time_text),
                style = NotificationsOnboardingDefaults.TitleMediumTextStyle,
                color = colorResource(id = R.color.text_primary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = formattedInterval,
                style = NotificationsOnboardingDefaults.TitleMediumTextStyle,
                color = colorResource(id = R.color.text_label_focus)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun NotificationsOnboardingSetTimeButtonPreview() {
    HyperskillTheme {
        NotificationsOnboardingSetTimeButton(
            formattedInterval = "12:00 - 13:00",
            onClick = {}
        )
    }
}