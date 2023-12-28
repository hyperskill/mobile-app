package org.hyperskill.app.android.interview_preparation_onboarding.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature

@Composable
fun InterviewPreparationOnboardingContent(
    onNewMessage: (InterviewPreparationOnboardingFeature.Message) -> Unit,
    padding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val onGoToFirstProblemClick by rememberUpdatedState {
        onNewMessage(InterviewPreparationOnboardingFeature.Message.GoToFirstProblemClicked)
    }
    Column(
        modifier = modifier
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 40.dp + padding.calculateTopPadding(),
                bottom = 20.dp + padding.calculateBottomPadding()
            )
    ) {
        Text(
            text = stringResource(id = R.string.interview_preparation_onboarding_title),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.interview_preparation_onboarding_description),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = org.hyperskill.app.android.R.drawable.img_interview_preparation_onboarding),
                contentDescription = null,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
        HyperskillButton(
            onClick = onGoToFirstProblemClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.interview_preparation_onboarding_go_to_first_problem)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InterviewPreparationOnboardingContentPreview() {
    HyperskillTheme {
        InterviewPreparationOnboardingContent(
            onNewMessage = {},
            padding = PaddingValues()
        )
    }
}