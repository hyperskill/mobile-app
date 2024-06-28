package org.hyperskill.app.android.welcome_onbaording.finish.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.ShimmerShotState
import org.hyperskill.app.android.core.view.ui.widget.compose.TypewriterTextEffect
import org.hyperskill.app.android.core.view.ui.widget.compose.shimmerShot
import org.hyperskill.app.android.welcome_onbaording.root.ui.WelcomeOnboardingDefault
import org.hyperskill.app.welcome_onboarding.finish.view.WelcomeOnboardingFinishViewState

@Composable
fun WelcomeOnboardingFinish(
    viewState: WelcomeOnboardingFinishViewState,
    onFinishClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shimmerShotState = remember { ShimmerShotState() }
    Column(
        modifier = modifier
            .padding(horizontal = WelcomeOnboardingDefault.horizontalPadding)
            .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            Content(
                title = viewState.title,
                description = viewState.description,
                onTitleTypingCompleted = {
                    delay(WelcomeOnboardingDefault.runActionButtonShimmerDelay)
                    shimmerShotState.runShimmerAnimation()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
            )
        }
        HyperskillButton(
            onClick = onFinishClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = WelcomeOnboardingDefault.buttonBottomPadding)
                .shimmerShot(shimmerShotState)
        ) {
            Text(text = viewState.buttonText)
        }
    }
}

@Composable
fun Content(
    title: String,
    description: String,
    onTitleTypingCompleted: suspend () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TypewriterTextEffect(
            text = title,
            startTypingDelay = WelcomeOnboardingDefault.startTypingAnimationDelayMillis,
            onEffectCompleted = onTitleTypingCompleted
        ) { text ->
            Text(
                text = text,
                style = MaterialTheme.typography.h5,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(id = R.color.text_primary),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.body1,
            color = colorResource(id = R.color.color_on_surface_alpha_60),
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Image(
            painter = painterResource(id = org.hyperskill.app.android.R.drawable.img_welcome_onboarding_finish),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
@Preview
private fun WelcomeOnboardingFinishPreview() {
    HyperskillTheme {
        WelcomeOnboardingFinish(
            viewState = WelcomeOnboardingFinishViewState(
                title = "You're all set!",
                description = "Embark on your journey in '{track.title}' right now!",
                buttonText = "Start my journey"
            ),
            onFinishClick = {}
        )
    }
}