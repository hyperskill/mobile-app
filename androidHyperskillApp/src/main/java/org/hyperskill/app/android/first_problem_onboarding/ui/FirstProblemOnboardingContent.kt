package org.hyperskill.app.android.first_problem_onboarding.ui

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.sp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature

@Composable
fun FirstProblemOnboardingContent(content: FirstProblemOnboardingFeature.ViewState.Content) {
    FirstProblemOnboardingContent(
        title = content.title,
        subtitle = content.subtitle,
        image = painterResource(id = getImage(content.isNewUserMode)),
        buttonText = content.buttonText
    )
}

@Composable
fun FirstProblemOnboardingContent(
    title: String,
    subtitle: String,
    image: Painter,
    buttonText: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.layer_1))
            .padding(FirstProblemOnboardingDefaults.ContentPadding),
        verticalArrangement = Arrangement.spacedBy(FirstProblemOnboardingDefaults.ImageBottomPadding)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(FirstProblemOnboardingDefaults.ImageTopPadding),
            modifier = Modifier.weight(1f)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(FirstProblemOnboardingDefaults.HeaderBottomPadding)
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = subtitle,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.body1,
                    color = colorResource(id = R.color.text_secondary),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Image(
                painter = image,
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
        HyperskillButton(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = buttonText)
        }
    }
}

private fun getImage(isNewUserMode: Boolean) =
    if (isNewUserMode) {
        org.hyperskill.app.android.R.drawable.img_first_problem_onboarding_new_user
    } else {
        org.hyperskill.app.android.R.drawable.img_first_problem_onboarding_experienced_user
    }

private data class FirstProblemOnboardingData(
    val title: String,
    val subtitle: String,
    val buttonText: String,
    @DrawableRes val imageRes: Int
)

private class SampleFirstProblemOnboardingPreviewDataProvider :
    PreviewParameterProvider<FirstProblemOnboardingData> {
    override val values: Sequence<FirstProblemOnboardingData> = sequenceOf(
        FirstProblemOnboardingData(
            title = "Let's keep going",
            subtitle = "It seems you've already made progress. Continue learning on '{project(or track).title}'!",
            buttonText = "Keep learning",
            imageRes = getImage(isNewUserMode = false)
        ),
        FirstProblemOnboardingData(
            title = "Great choice!",
            subtitle = "Embark on your journey in '{project(or track).title}' right now!",
            buttonText = "Start learning",
            imageRes = getImage(isNewUserMode = true)
        )
    )
}

@Preview(
    group = "Light theme",
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun FirstProblemOnboardingScreenLightThemePreview(
    @PreviewParameter(SampleFirstProblemOnboardingPreviewDataProvider::class)
    onboardingData: FirstProblemOnboardingData
) {
    FirstProblemOnboardingScreenPreview(onboardingData)
}

@Preview(
    group = "Dart theme",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun FirstProblemOnboardingScreenDarkModePreview(
    @PreviewParameter(SampleFirstProblemOnboardingPreviewDataProvider::class)
    onboardingData: FirstProblemOnboardingData
) {
    FirstProblemOnboardingScreenPreview(onboardingData)
}

@Preview(
    group = "Small device",
    showBackground = true,
    showSystemUi = true,
    device = "spec:parent=Nexus S"
)
@Composable
private fun FirstProblemOnboardingScreenSmallDevicePreview(
    @PreviewParameter(SampleFirstProblemOnboardingPreviewDataProvider::class)
    onboardingData: FirstProblemOnboardingData
) {
    FirstProblemOnboardingScreenPreview(onboardingData)
}

@Composable
private fun FirstProblemOnboardingScreenPreview(
    onboardingData: FirstProblemOnboardingData
) {
    HyperskillTheme {
        FirstProblemOnboardingContent(
            title = onboardingData.title,
            subtitle = onboardingData.subtitle,
            buttonText = onboardingData.buttonText,
            image = painterResource(id = onboardingData.imageRes)
        )
    }
}