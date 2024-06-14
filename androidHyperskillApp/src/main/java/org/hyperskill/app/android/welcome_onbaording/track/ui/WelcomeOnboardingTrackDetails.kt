package org.hyperskill.app.android.welcome_onbaording.track.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.shimmer
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingTrack
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsViewModel

@Composable
fun WelcomeOnboardingTrackDetails(
    viewModel: WelcomeOnboardingTrackDetailsViewModel,
    modifier: Modifier = Modifier
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    WelcomeOnboardingTrackDetails(
        viewState = viewState,
        onSelectClick = viewModel::onContinueClick,
        modifier = modifier
    )
}

@Composable
fun WelcomeOnboardingTrackDetails(
    viewState: WelcomeOnboardingTrackDetailsFeature.ViewState,
    onSelectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(WelcomeOnboardingTrackDetailsDefaults.padding)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = viewState.title,
                style = MaterialTheme.typography.h5,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(id = R.color.text_primary),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Box(modifier = Modifier.weight(1f)) {
                Column(modifier = Modifier.align(Alignment.Center)) {
                    WelcomeOnboardingTrackCard(
                        imagePainter = viewState.track.imagePainter,
                        trackTitle = viewState.trackTitle,
                        trackDescription = viewState.trackDescriptionHtml
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = viewState.changeText,
                        style = MaterialTheme.typography.body2,
                        color = colorResource(id = R.color.text_secondary),
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
        HyperskillButton(
            onClick = onSelectClick,
            enabled = !viewState.isLoadingShowed,
            modifier = Modifier
                .fillMaxWidth()
                .shimmer(isLoading = viewState.isLoadingShowed)
        ) {
            Text(text = viewState.buttonText)
        }
    }
}

@Suppress("MaxLineLength")
@Composable
@Preview(showSystemUi = true)
private fun WelcomeOnboardingTrackDetailsPreview() {
    HyperskillTheme {
        WelcomeOnboardingTrackDetails(
            WelcomeOnboardingTrackDetailsFeature.ViewState(
                track = WelcomeOnboardingTrack.KOTLIN,
                title = stringResource(id = R.string.welcome_onboarding_track_details_title),
                trackTitle = stringResource(id = R.string.welcome_onboarding_track_details_kotlin_title),
                trackDescriptionHtml = stringResource(id = R.string.welcome_onboarding_track_details_kotlin_description),
                changeText = stringResource(id = R.string.welcome_onboarding_track_details_change_text),
                buttonText = stringResource(id = R.string.welcome_onboarding_track_details_continue_btn),
                isLoadingShowed = false
            ),
            onSelectClick = {}
        )
    }
}