package org.hyperskill.app.android.notification_onboarding.ui

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.core.view.HapticFeedbackConstantsCompat
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton

@Composable
fun NotificationsOnboardingTurnOnButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val view = LocalView.current
    val currentOnClick by rememberUpdatedState {
        view.performHapticFeedback(HapticFeedbackConstantsCompat.CONTEXT_CLICK)
        onClick()
    }
    HyperskillButton(
        onClick = currentOnClick,
        modifier = modifier
    ) {
        Text(text = stringResource(id = R.string.notifications_onboarding_turn_on_btn))
    }
}