package org.hyperskill.app.android.interview_preparation_onboarding.fragement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.interview_preparation_onboarding.ui.InterviewPreparationOnboardingScreen
import org.hyperskill.app.android.step.view.screen.StepScreen
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingFeature.Action
import org.hyperskill.app.interview_preparation_onboarding.presentation.InterviewPreparationOnboardingViewModel
import org.hyperskill.app.step.domain.model.StepRoute

class InterviewPreparationOnboardingFragment : Fragment() {
    companion object {
        fun newInstance(stepRoute: StepRoute): InterviewPreparationOnboardingFragment =
            InterviewPreparationOnboardingFragment().apply {
                this.stepRoute = stepRoute
            }
    }

    private var stepRoute: StepRoute by argument(StepRoute.serializer())

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val interviewPreparationOnboardingViewModel: InterviewPreparationOnboardingViewModel by viewModels {
        requireNotNull(viewModelFactory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        interviewPreparationOnboardingViewModel.handleActions(
            this,
            onAction = ::onAction
        )
    }

    private fun injectComponent() {
        val platformInterviewPreparationOnboardingComponent =
            HyperskillApp.graph().buildPlatformInterviewPreparationOnboardingComponent(stepRoute)
        viewModelFactory = platformInterviewPreparationOnboardingComponent.reduxViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent {
                HyperskillTheme {
                    InterviewPreparationOnboardingScreen(
                        viewModel = interviewPreparationOnboardingViewModel,
                        onBackClick = { requireRouter().exit() }
                    )
                }
            }
        }

    private fun onAction(action: Action.ViewAction) {
        when (action) {
            is Action.ViewAction.NavigateTo.StepScreen ->
                requireRouter().replaceScreen(StepScreen(action.stepRoute))
        }
    }
}