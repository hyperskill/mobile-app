package org.hyperskill.app.android.welcome_onbaording.track.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.OnComposableShownFirstTime
import org.hyperskill.app.android.core.view.ui.widget.compose.ShimmerShotState
import org.hyperskill.app.android.core.view.ui.widget.compose.infiniteShimmer
import org.hyperskill.app.android.core.view.ui.widget.compose.shimmerShot
import org.hyperskill.app.android.welcome_onbaording.root.ui.WelcomeOnboardingDefault
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsFeature
import org.hyperskill.app.welcome_onboarding.track_details.presentation.WelcomeOnboardingTrackDetailsViewModel

@Composable
fun WelcomeOnboardingTrackDetails(
    viewModel: WelcomeOnboardingTrackDetailsViewModel,
    modifier: Modifier = Modifier
) {
    OnComposableShownFirstTime(key = Unit, block = viewModel::onShow)
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
    val shimmerShotState = remember { ShimmerShotState() }
    Column(
        modifier = modifier
            .padding(WelcomeOnboardingTrackDetailsDefaults.padding)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.align(Alignment.Center)) {
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
                WelcomeOnboardingTrackCard(
                    imagePainter = viewState.track.imagePainter,
                    trackTitle = viewState.trackTitle,
                    trackDescription = viewState.trackDescriptionHtml,
                    onTitleTypingCompleted = {
                        delay(WelcomeOnboardingDefault.runActionButtonShimmerDelay)
                        if (!viewState.isLoadingShowed) {
                            shimmerShotState.runShimmerAnimation()
                        }
                    }
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
        HyperskillButton(
            onClick = onSelectClick,
            enabled = !viewState.isLoadingShowed,
            modifier = Modifier
                .fillMaxWidth()
                .infiniteShimmer(play = viewState.isLoadingShowed)
                .shimmerShot(shimmerShotState)
        ) {
            Text(text = viewState.buttonText)
        }
    }
}

@Composable
@Preview(showSystemUi = true)
private fun WelcomeOnboardingTrackDetailsPreview() {
    HyperskillTheme {
        WelcomeOnboardingTrackDetails(
            WelcomeOnboardingTrackDetailsPreviewData.PreviewViewState,
            onSelectClick = {}
        )
    }
}

@Composable
@Preview(showSystemUi = true, device = "id:pixel_2")
private fun WelcomeOnboardingTrackDetailsSmallScreenPreview() {
    HyperskillTheme {
        WelcomeOnboardingTrackDetails(
            WelcomeOnboardingTrackDetailsPreviewData.PreviewViewState,
            onSelectClick = {}
        )
    }
}