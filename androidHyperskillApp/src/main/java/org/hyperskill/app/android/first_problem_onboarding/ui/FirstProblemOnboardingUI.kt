package org.hyperskill.app.android.first_problem_onboarding.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.ViewState
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingViewModel

@Composable
fun FirstProblemOnboardingScreen(viewModel: FirstProblemOnboardingViewModel) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    when (val state = viewState) {
        ViewState.Idle -> {
            // no op
        }
        ViewState.Loading -> TODO()
        ViewState.Error -> TODO()
        is ViewState.Content -> FirstProblemOnboardingContent(state)
    }
}