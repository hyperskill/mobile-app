package org.hyperskill.app.android.notification_onboarding.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.HapticFeedbackConstantsCompat
import org.hyperskill.app.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NotificationsOnboardingSetLaterButton(
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
        shape = RoundedCornerShape(32.dp),
        color = Color.Transparent,
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.notifications_onboarding_set_later_btn),
            style = NotificationsOnboardingDefaults.TitleMediumTextStyle,
            color = colorResource(id = R.color.color_on_surface_alpha_60),
            modifier = Modifier.padding(NotificationsOnboardingDefaults.SetLaterBtnPadding)
        )
    }
}