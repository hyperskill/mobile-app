package org.hyperskill.app.android.welcome_onbaording.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.welcome_onbaording.navigation.WelcomeOnboardingStartingScreen
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingViewModel
import ru.nobird.android.view.navigation.ui.fragment.FlowFragment

class WelcomeOnboardingFragment : FlowFragment() {
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
            initNavigation()
        }
    }

    private fun injectComponent() {
        viewModelFactory =
            HyperskillApp
                .graph()
                .buildPlatformWelcomeOnboardingComponent()
                .reduxViewModelFactory
    }

    private fun initNavigation() {
        router.newRootScreen(WelcomeOnboardingStartingScreen)
    }
}