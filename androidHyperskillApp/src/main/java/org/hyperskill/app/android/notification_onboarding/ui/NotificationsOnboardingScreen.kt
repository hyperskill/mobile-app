package org.hyperskill.app.android.notification_onboarding.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.themeadapter.material.MdcTheme
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTextButton
import org.hyperskill.app.android.core.view.ui.widget.compose.centerWithVerticalBias
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingViewModel

object NotificationsOnboardingDefaults {
    val ContentPadding = PaddingValues(
        start = 20.dp,
        end = 20.dp,
        top = 40.dp,
        bottom = 32.dp
    )
}

@Composable
fun NotificationsOnboardingScreen(
    viewModel: NotificationsOnboardingViewModel
) {
    val viewState: NotificationsOnboardingFeature.ViewState by viewModel.state.collectAsStateWithLifecycle()
    NotificationsOnboardingScreen(
        formattedInterval = viewState.formattedDailyStudyRemindersInterval,
        onNewMessage = viewModel::onNewMessage
    )
}

@Composable
fun NotificationsOnboardingScreen(
    formattedInterval: String,
    onNewMessage: (NotificationsOnboardingFeature.Message) -> Unit
) {
    val onAllowNotificationsClick by rememberUpdatedState {
        onNewMessage(NotificationsOnboardingFeature.Message.AllowNotificationsClicked)
    }
    val onRemindMeLaterClick by rememberUpdatedState {
        onNewMessage(NotificationsOnboardingFeature.Message.NotNowClicked)
    }
    val onTimeClick by rememberUpdatedState {
        onNewMessage(NotificationsOnboardingFeature.Message.DailyStudyRemindsIntervalHourClicked)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.layer_1))
            .padding(NotificationsOnboardingDefaults.ContentPadding)
    ) {
        NotificationsOnboardingHeader(formattedInterval, onTimeClick = onTimeClick)
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Image(
                painter = painterResource(id = org.hyperskill.app.android.R.drawable.img_notifications_onboarding),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.centerWithVerticalBias(-0.5f))
            )
        }
        NotificationsOnboardingButtons(
            onAllowNotificationsClick = onAllowNotificationsClick,
            onRemindMeLaterClick = onRemindMeLaterClick
        )
    }
}

@Composable
private fun NotificationsOnboardingHeader(
    formattedInterval: String,
    modifier: Modifier = Modifier,
    onTimeClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.notifications_onboarding_title_new),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6
        )
        DailyNotificationTime(
            formattedInterval = formattedInterval,
            onTimeClick = onTimeClick
        )
    }
}

@Composable
private fun DailyNotificationTime(
    formattedInterval: String,
    onTimeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(id = org.hyperskill.app.android.R.dimen.corner_radius)))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .clickable(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    indication = rememberRipple(),
                    onClick = onTimeClick
                )
                .padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            Text(text = stringResource(id = R.string.notifications_onboarding_daily_study_reminders_interval_prefix))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = formattedInterval)
            Spacer(modifier = Modifier.width(4.dp))
            Box(modifier = Modifier.size(24.dp)) {
                Image(
                    painter = painterResource(
                        id = org.hyperskill.app.android.R.drawable.ic_notification_onboarding_arrow
                    ),
                    contentDescription = null,
                    modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun NotificationsOnboardingButtons(
    modifier: Modifier = Modifier,
    onAllowNotificationsClick: () -> Unit,
    onRemindMeLaterClick: () -> Unit
) {
    Column(modifier = modifier) {
        HyperskillButton(
            onClick = onAllowNotificationsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.notifications_onboarding_button_allow))
        }
        Spacer(modifier = Modifier.height(8.dp))
        HyperskillTextButton(
            onClick = onRemindMeLaterClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.notifications_onboarding_button_not_now)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NotificationsOnboardingScreenLightModePreview() {
    MdcTheme(
        setTextColors = true,
        setDefaultFontFamily = true
    ) {
        NotificationsOnboardingScreen("12:00 - 13:00") {}
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true,
    showSystemUi = true,
    device = "id:Nexus One"
)
@Composable
private fun NotificationsOnboardingScreenDarkModePreview() {
    MdcTheme(
        setTextColors = true,
        setDefaultFontFamily = true
    ) {
        NotificationsOnboardingScreen("12:00 - 13:00") {}
    }
}