package org.hyperskill.app.android.notification_onboarding.ui

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.themeadapter.material.MdcTheme
import org.hyperskill.app.R

@Composable
fun NotificationsOnboardingScreen() {
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
            modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)
        )
        NotificationsOnboardingButtons(modifier = Modifier.weight(1f, fill = false))
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
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.notifications_onboarding_subtitle),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun NotificationsOnboardingButtons(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.notifications_onboarding_allow_notifications_button))
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.notifications_onboarding_remind_me_later_button))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotificationsOnboardingScreenLightModePreview() {
    MdcTheme {
        NotificationsOnboardingScreen()
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
    MdcTheme {
        NotificationsOnboardingScreen()
    }
}