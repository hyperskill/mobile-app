package org.hyperskill.app.android.welcome_onbaording.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.extensions.argument
import org.hyperskill.app.android.welcome_onbaording.model.WelcomeOnboardingHost
import org.hyperskill.app.android.welcome_onbaording.navigation.WelcomeOnboardingEntryPointScreen
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingFeatureParams
import org.hyperskill.app.welcome_onboarding.model.WelcomeOnboardingStartScreen
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingFeature
import org.hyperskill.app.welcome_onboarding.presentation.WelcomeOnboardingViewModel
import ru.nobird.android.view.navigation.ui.fragment.FlowFragment

class WelcomeOnboardingFragment : FlowFragment(), WelcomeOnboardingHost {
    companion object {
        fun newInstance(params: WelcomeOnboardingFeatureParams): WelcomeOnboardingFragment =
            WelcomeOnboardingFragment().apply {
                this.params = params
            }
    }

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val welcomeOnboardingViewModel: WelcomeOnboardingViewModel by viewModels {
        requireNotNull(viewModelFactory)
    }

    private var params: WelcomeOnboardingFeatureParams by argument(WelcomeOnboardingFeatureParams.serializer())

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
                .buildPlatformWelcomeOnboardingComponent(params)
                .reduxViewModelFactory
    }

    private fun initNavigation(state: WelcomeOnboardingFeature.State) {
        val screen = when (state.initialStep) {
            WelcomeOnboardingStartScreen.START_SCREEN -> WelcomeOnboardingEntryPointScreen
            WelcomeOnboardingStartScreen.NOTIFICATION_ONBOARDING -> TODO()
        }
        router.newRootScreen(screen)
    }

    override fun onStartClick() {
        welcomeOnboardingViewModel.onStartClick()
    }
}