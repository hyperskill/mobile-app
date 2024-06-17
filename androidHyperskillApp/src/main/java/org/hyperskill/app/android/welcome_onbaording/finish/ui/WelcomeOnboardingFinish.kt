package org.hyperskill.app.android.welcome_onbaording.finish.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme

@Composable
fun WelcomeOnboardingFinish(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 20.dp)
        ) {
            Text(
                text = "You're all set!",
                style = MaterialTheme.typography.h5,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = colorResource(id = org.hyperskill.app.R.color.text_primary),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Embark on your journey in '{track.title}' right now!",
                style = MaterialTheme.typography.body1,
                color = colorResource(id = org.hyperskill.app.R.color.color_on_surface_alpha_60),
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Image(
                painter = painterResource(id = R.drawable.img_welcome_onboarding_finish),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
@Preview
private fun WelcomeOnboardingFinishPreview() {
    HyperskillTheme {
        WelcomeOnboardingFinish()
    }
}