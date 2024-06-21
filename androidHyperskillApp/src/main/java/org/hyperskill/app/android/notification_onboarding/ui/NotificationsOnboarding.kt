package org.hyperskill.app.android.notification_onboarding.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.ShimmerShotState
import org.hyperskill.app.android.welcome_onbaording.root.ui.WelcomeOnboardingDefault
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingFeature
import org.hyperskill.app.notifications_onboarding.presentation.NotificationsOnboardingViewModel
import org.hyperskill.app.R as SharedR

@Composable
fun NotificationsOnboarding(
    viewModel: NotificationsOnboardingViewModel
) {
    val viewState: NotificationsOnboardingFeature.ViewState by viewModel.state.collectAsStateWithLifecycle()
    NotificationsOnboarding(
        formattedInterval = viewState.formattedDailyStudyRemindersInterval,
        onAllowNotificationsClick = viewModel::onAllowNotificationsClicked,
        onSetLaterClick = viewModel::onSetLaterClicked,
        onTimeClick = viewModel::onTimeClicked
    )
}

@Composable
fun NotificationsOnboarding(
    formattedInterval: String,
    onAllowNotificationsClick: () -> Unit,
    onSetLaterClick: () -> Unit,
    onTimeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var actionButtonHeight: Int by remember { mutableStateOf(0) }
    val shimmerShotState = remember { ShimmerShotState() }
    LaunchedEffect(key1 = Unit) {
        delay(WelcomeOnboardingDefault.runActionButtonShimmerDelay)
        shimmerShotState.runShimmerAnimation()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = NotificationsOnboardingDefaults.HorizontalPadding)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Spacer(modifier = Modifier.height(NotificationsOnboardingDefaults.TopPadding))
        NotificationsOnboardingSetLaterButton(
            onClick = onSetLaterClick,
            modifier = Modifier
                .align(Alignment.End)
                .offset(x = NotificationsOnboardingDefaults.SetLaterBtnPadding)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Content(
                formattedInterval = formattedInterval,
                actionButtonHeight = actionButtonHeight,
                onTimeClick = onTimeClick,
                modifier = Modifier
                    .align(NotificationsOnboardingDefaults.ContentAlignment)
                    .fillMaxHeight()
            )
        }
        NotificationsOnboardingTurnOnButton(
            shimmerShotState = shimmerShotState,
            onClick = onAllowNotificationsClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = NotificationsOnboardingDefaults.BottomPadding)
                .onSizeChanged {
                    actionButtonHeight = it.height
                }
        )
    }
}

@Composable
private fun Content(
    formattedInterval: String,
    actionButtonHeight: Int,
    onTimeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(17.dp))
        Title(modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(id = R.drawable.img_notifications_onboarding),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        NotificationsOnboardingSetTimeButton(
            formattedInterval = formattedInterval,
            onClick = onTimeClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = SharedR.string.notifications_onboarding_set_later_description),
            color = colorResource(id = SharedR.color.text_secondary),
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(
            modifier = Modifier.height(
                18.dp + NotificationsOnboardingDefaults.BottomPadding +
                    with(LocalDensity.current) {
                        actionButtonHeight.toDp()
                    }
            )
        )
    }
}

@Composable
private fun Title(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = SharedR.string.notifications_onboarding_title),
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(id = SharedR.color.text_primary),
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NotificationsOnboardingLightModePreview() {
    HyperskillTheme {
        NotificationsOnboarding(
            formattedInterval = "12:00 - 13:00",
            onAllowNotificationsClick = {},
            onSetLaterClick = {},
            onTimeClick = {}
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    showBackground = true,
    showSystemUi = true,
    device = "id:pixel"
)
@Composable
private fun NotificationsOnboardingDarkModePreview() {
    HyperskillTheme {
        NotificationsOnboarding(
            formattedInterval = "12:00 - 13:00",
            onAllowNotificationsClick = {},
            onSetLaterClick = {},
            onTimeClick = {}
        )
    }
}