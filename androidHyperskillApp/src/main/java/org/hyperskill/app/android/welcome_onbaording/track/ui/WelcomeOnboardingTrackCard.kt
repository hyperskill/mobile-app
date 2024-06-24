package org.hyperskill.app.android.welcome_onbaording.track.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HtmlText
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillCard
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.TypewriterTextEffect
import org.hyperskill.app.android.welcome_onbaording.root.ui.WelcomeOnboardingDefault
import org.hyperskill.app.R as SharedR

@Composable
fun WelcomeOnboardingTrackCard(
    imagePainter: Painter,
    trackTitle: String,
    trackDescription: String,
    modifier: Modifier = Modifier,
    onTitleTypingCompleted: suspend () -> Unit = {}
) {
    HyperskillCard(modifier) {
        Column(modifier = Modifier.padding(bottom = 12.dp)) {
            Image(
                painter = imagePainter,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            TypewriterTextEffect(
                text = trackTitle,
                maxCharacterChunk = 1,
                startTypingDelay = WelcomeOnboardingDefault.startTypingAnimationDelayMillis,
                onEffectCompleted = onTitleTypingCompleted
            ) { text ->
                Text(
                    text = text,
                    style = MaterialTheme.typography.h4,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = colorResource(id = SharedR.color.color_on_surface_alpha_87),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            HtmlText(text = trackDescription) { annotatedString ->
                Text(
                    text = annotatedString,
                    style = MaterialTheme.typography.body1,
                    color = colorResource(id = SharedR.color.color_on_surface_alpha_60),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Suppress("MaxLineLength")
@Composable
@Preview(showSystemUi = true)
private fun WelcomeOnboardingTrackCardPreview() {
    HyperskillTheme {
        WelcomeOnboardingTrackCard(
            imagePainter = painterResource(id = R.drawable.img_onboarding_choose_track_kotlin),
            trackTitle = stringResource(id = SharedR.string.welcome_onboarding_track_details_kotlin_title),
            trackDescription = stringResource(id = SharedR.string.welcome_onboarding_track_details_kotlin_description),
            modifier = Modifier.padding(20.dp)
        )
    }
}