package org.hyperskill.app.android.users_questionnaire_onboarding.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.view.ui.navigation.requireAppRouter
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.users_questionnaire_onboarding.ui.UsersQuestionnaireOnboardingScreen
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.users_questionnaire_onboarding.onboarding.presentation.UsersQuestionnaireOnboardingViewModel
import org.hyperskill.app.users_questionnaire_onboarding_legacy.presentation.LegacyUsersQuestionnaireOnboardingFeature.Action.ViewAction

class UsersQuestionnaireOnboardingFragment : Fragment() {
    companion object {
        const val USERS_QUESTIONNAIRE_ONBOARDING_FINISHED = "USERS_QUESTIONNAIRE_ONBOARDING_FINISHED"
        fun newInstance(): UsersQuestionnaireOnboardingFragment =
            UsersQuestionnaireOnboardingFragment()
    }

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val usersQuestionnaireOnboardingViewModel: UsersQuestionnaireOnboardingViewModel by viewModels {
        requireNotNull(viewModelFactory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        usersQuestionnaireOnboardingViewModel.handleActions(this, onAction = ::onAction)
    }

    private fun injectComponent() {
        val platformUsersQuestionnaireOnboardingComponent =
            HyperskillApp.graph().buildPlatformUsersQuestionnaireOnboardingComponent()
        viewModelFactory = platformUsersQuestionnaireOnboardingComponent.reduxViewModelFactory
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
                    UsersQuestionnaireOnboardingScreen(viewModel = usersQuestionnaireOnboardingViewModel)
                }
            }
        }

    private fun onAction(action: ViewAction) {
        when (action) {
            ViewAction.CompleteUsersQuestionnaireOnboarding -> {
                requireAppRouter().sendResult(USERS_QUESTIONNAIRE_ONBOARDING_FINISHED, Any())
            }
            is ViewAction.ShowSendSuccessMessage -> {
                Snackbar.make(requireView(), action.message, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}