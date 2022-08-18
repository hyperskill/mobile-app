package org.hyperskill.app.android.onboarding.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.view.ui.navigation.AuthScreen
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentOnboardingBinding
import org.hyperskill.app.android.home.view.ui.screen.PlaceholderNewUserScreen
import org.hyperskill.app.onboarding.presentation.OnboardingFeature
import org.hyperskill.app.onboarding.presentation.OnboardingViewModel
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class OnboardingFragment :
    Fragment(R.layout.fragment_onboarding),
    ReduxView<OnboardingFeature.State, OnboardingFeature.Action.ViewAction> {
    companion object {
        fun newInstance(): Fragment =
            OnboardingFragment()
    }

    private val viewBinding: FragmentOnboardingBinding by viewBinding(FragmentOnboardingBinding::bind)

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private val onboardingViewModel: OnboardingViewModel by reduxViewModel(this) { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    private fun injectComponents() {
        val onboardingComponent = HyperskillApp.graph().buildOnboardingComponent()
        val platformOnboardingComponent = HyperskillApp.graph().buildPlatformOnboardingComponent(onboardingComponent)

        viewModelFactory = platformOnboardingComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.onboardingSignInButton.setOnClickListener {
            requireRouter().navigateTo(AuthScreen)
        }

        viewBinding.onboardingSignUpButton.setOnClickListener {
            requireRouter().navigateTo(PlaceholderNewUserScreen)
        }

        onboardingViewModel.onNewMessage(OnboardingFeature.Message.Init)
    }

    override fun onAction(action: OnboardingFeature.Action.ViewAction) {
        // no op
    }

    override fun render(state: OnboardingFeature.State) {
        // no op
    }
}