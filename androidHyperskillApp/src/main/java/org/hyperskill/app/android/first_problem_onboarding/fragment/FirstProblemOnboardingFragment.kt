package org.hyperskill.app.android.first_problem_onboarding.fragment

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
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.first_problem_onboarding.ui.FirstProblemOnboardingScreen
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingFeature.Action.ViewAction
import org.hyperskill.app.first_problem_onboarding.presentation.FirstProblemOnboardingViewModel
import ru.nobird.android.view.base.ui.extension.argument

class FirstProblemOnboardingFragment : Fragment() {
    companion object {
        fun newInstance(isNewUserMode: Boolean): FirstProblemOnboardingFragment =
            FirstProblemOnboardingFragment().apply {
                this.isNewUserMode = isNewUserMode
            }
    }

    private var isNewUserMode: Boolean by argument()

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val firstProblemOnboardingViewModel: FirstProblemOnboardingViewModel by viewModels {
        requireNotNull(viewModelFactory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        firstProblemOnboardingViewModel.handleActions(this, block = ::onAction)
    }

    private fun injectComponent() {
        val platformFirstProblemOnboardingComponent =
            HyperskillApp.graph().buildPlatformFirstProblemOnboardingComponent(isNewUserMode)
        viewModelFactory = platformFirstProblemOnboardingComponent.reduxViewModelFactory
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
                    FirstProblemOnboardingScreen(viewModel = firstProblemOnboardingViewModel)
                }
            }
        }

    private fun onAction(action: ViewAction) {
        when (action) {
            is ViewAction.CompleteFirstProblemOnboarding -> TODO()
            ViewAction.ShowNetworkError -> TODO()
        }
    }
}