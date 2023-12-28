package org.hyperskill.app.android.interview_preparation_onboarding.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTopAppBar
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingViewModel

@Composable
fun InterviewPreparationOnboardingScreen(
    viewModel: InterviewPreparationOnboardingViewModel,
    onBackClick: () -> Unit
) {
    DisposableEffect(viewModel) {
        viewModel.onNewMessage(InterviewPreparationOnboardingFeature.Message.ViewedEventMessage)
        onDispose {
            // no op
        }
    }
    InterviewPreparationOnboardingScreen(viewModel::onNewMessage, onBackClick)
}

@Composable
fun InterviewPreparationOnboardingScreen(
    onNewMessage: (InterviewPreparationOnboardingFeature.Message) -> Unit,
    onBackClick: () -> Unit
) {
    val currentOnBackClick by rememberUpdatedState(newValue = onBackClick)
    Scaffold(
        topBar = {
            HyperskillTopAppBar(
                title = stringResource(id = R.string.interview_preparation_onboarding_screen_title),
                onNavigationIconClick = currentOnBackClick
            )
        }
    ) { padding ->
        val currentOnNewMessage by rememberUpdatedState(newValue = onNewMessage)
        InterviewPreparationOnboardingContent(
            onNewMessage = currentOnNewMessage,
            padding = padding
        )
    }
}