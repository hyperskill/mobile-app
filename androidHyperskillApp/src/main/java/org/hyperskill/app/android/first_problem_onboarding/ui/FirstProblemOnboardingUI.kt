package org.hyperskill.app.android.first_problem_onboarding.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyperskill.app.android.core.view.ui.widget.compose.ScreenDataLoadingError
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Message
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.ViewState
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingViewModel

@Composable
fun FirstProblemOnboardingScreen(viewModel: FirstProblemOnboardingViewModel) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    DisposableEffect(viewModel) {
        viewModel.onNewMessage(Message.ViewedEventMessage)
        onDispose {
            // no op
        }
    }
    when (val state = viewState) {
        ViewState.Idle -> {
            // no op
        }
        ViewState.Loading ->
            FirstProblemOnboardingSkeleton()
        is ViewState.Content ->
            FirstProblemOnboardingContent(
                content = state,
                onNewMessage = viewModel::onNewMessage
            )
        ViewState.Error ->
            ScreenDataLoadingError(
                onRetryClick = remember {
                    {
                        viewModel.onNewMessage(Message.RetryContentLoading)
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
    }
}