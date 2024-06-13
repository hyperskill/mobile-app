package org.hyperskill.app.android.welcome_onbaording.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.welcome_onbaording.model.WelcomeOnboardingHost
import org.hyperskill.app.android.welcome_onbaording.navigation.WelcomeOnboardingStartingScreen
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingStep
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingViewModel
import ru.nobird.android.view.navigation.ui.fragment.FlowFragment

class WelcomeOnboardingFragment : FlowFragment(), WelcomeOnboardingHost {
    companion object {
        fun newInstance(): WelcomeOnboardingFragment =
            WelcomeOnboardingFragment()
    }

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val welcomeOnboardingViewModel: WelcomeOnboardingViewModel by viewModels {
        requireNotNull(viewModelFactory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        if (savedInstanceState == null) {
            initNavigation(welcomeOnboardingViewModel.state.value)
        }
    }

    private fun injectComponent() {
        viewModelFactory =
            HyperskillApp
                .graph()
                .buildPlatformWelcomeOnboardingComponent()
                .reduxViewModelFactory
    }

    private fun initNavigation(state: WelcomeOnboardingFeature.State) {
        val screen = when (state.initialStep) {
            WelcomeOnboardingStep.START_SCREEN -> WelcomeOnboardingStartingScreen
            WelcomeOnboardingStep.HOW_DID_YOU_HEAR_ABOUT_HYPERSKILL -> TODO()
            WelcomeOnboardingStep.LEARNING_REASON -> TODO()
            WelcomeOnboardingStep.CODING_BACKGROUND -> TODO()
            WelcomeOnboardingStep.PICK_LANGUAGE -> TODO()
            WelcomeOnboardingStep.CONFIRM_TRACK -> TODO()
            WelcomeOnboardingStep.NOTIFICATION_ONBOARDING -> TODO()
            WelcomeOnboardingStep.FINAL_SCREEN -> TODO()
        }
        router.newRootScreen(screen)
    }

    override fun onStartClick() {
        welcomeOnboardingViewModel.onStartClick()
    }
}