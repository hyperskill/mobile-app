package org.hyperskill.app.android.notification_onboarding.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.themeadapter.material.MdcTheme
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.textButton
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingViewModel

@Composable
fun NotificationsOnboardingScreen(
    viewModel: NotificationsOnboardingViewModel
) {
    NotificationsOnboardingScreen(viewModel::onNewMessage)
}

@Composable
fun NotificationsOnboardingScreen(
    onNewMessage: (NotificationsOnboardingFeature.Message) -> Unit
) {
    val onAllowNotificationsClick by rememberUpdatedState {
        onNewMessage(NotificationsOnboardingFeature.Message.AllowNotificationClicked)
    }
    val onRemindMeLaterClick by rememberUpdatedState {
        onNewMessage(NotificationsOnboardingFeature.Message.RemindMeLaterClicked)
    }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 24.dp,
                bottom = 32.dp
            ),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        NotificationsOnboardingHeader()
        Image(
            painter = painterResource(id = org.hyperskill.app.android.R.drawable.img_notifications_onboarding),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
        )
        NotificationsOnboardingButtons(
            modifier = Modifier.weight(1f, fill = false),
            onAllowNotificationsClick = onAllowNotificationsClick,
            onRemindMeLaterClick = onRemindMeLaterClick
        )
    }
}

@Composable
private fun NotificationsOnboardingHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.notifications_onboarding_title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.notifications_onboarding_subtitle),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            // Can't use color_text_secondary in TextAppearance.AppTheme.Body1
            // because it is used in many other components
            color = colorResource(id = R.color.color_text_secondary)
        )
    }
}

@Composable
fun NotificationsOnboardingButtons(
    modifier: Modifier = Modifier,
    onAllowNotificationsClick: () -> Unit,
    onRemindMeLaterClick: () -> Unit
) {
    Column(modifier = modifier) {
        Button(
            onClick = onAllowNotificationsClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = colorResource(id = R.color.color_button_primary)
            ),
            contentPadding = PaddingValues(
                vertical = 14.dp,
                horizontal = 16.dp
            )
        ) {
            Text(text = stringResource(id = R.string.notifications_onboarding_allow_notifications_button))
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            onClick = onRemindMeLaterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.notifications_onboarding_remind_me_later_button),
                style = MaterialTheme.typography.textButton
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotificationsOnboardingScreenLightModePreview() {
    MdcTheme(
        setTextColors = true,
        setDefaultFontFamily = true
    ) {
        NotificationsOnboardingScreen {}
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true,
    showSystemUi = true,
    device = "id:Nexus One"
)
@Composable
fun NotificationsOnboardingScreenDarkModePreview() {
    MdcTheme(
        setTextColors = true,
        setDefaultFontFamily = true
    ) {
        NotificationsOnboardingScreen {}
    }
}