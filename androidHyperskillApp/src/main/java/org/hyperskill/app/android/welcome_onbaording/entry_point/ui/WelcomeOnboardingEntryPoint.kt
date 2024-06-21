package org.hyperskill.app.android.welcome_onbaording.entry_point.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.welcome_onbaording.root.ui.WelcomeOnboardingDefault
import org.hyperskill.app.R as SharedR

@Composable
fun WelcomeOnboardingEntryPoint(
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = WelcomeOnboardingDefault.horizontalPadding)
    ) {
        Spacer(modifier = Modifier.height(44.dp))
        Column(modifier = Modifier.weight(1f)) {
            Title(modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(16.dp))
            Description(modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.img_welcome_onboarding_entry_point),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        HyperskillButton(
            onClick = onStartClick,
            modifier = Modifier.fillMaxWidth().padding(bottom = WelcomeOnboardingDefault.buttonBottomPadding)
        ) {
            Text(text = stringResource(id = SharedR.string.welcome_onboarding_entry_point_start_btn))
        }
    }
}

@Composable
private fun Title(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = SharedR.string.welcome_onboarding_entry_point_title),
        style = WelcomeOnboardingDefault.titleTextStyle,
        modifier = modifier
    )
}

@Composable
private fun Description(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = SharedR.string.welcome_onboarding_entry_point_description),
        style = MaterialTheme.typography.body1,
        fontSize = 14.sp,
        color = colorResource(id = SharedR.color.text_secondary),
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Preview
@Composable
private fun WelcomeOnboardingEntryPointPreview() {
    HyperskillTheme {
        WelcomeOnboardingEntryPoint {}
    }
}