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
import org.hyperskill.app.android.placeholder_new_user.navigation.PlaceholderNewUserScreen
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
            onboardingViewModel.onNewMessage(OnboardingFeature.Message.ClickedSignInEventMessage)
            requireRouter().navigateTo(AuthScreen())
        }

        viewBinding.onboardingSignUpButton.setOnClickListener {
            onboardingViewModel.onNewMessage(OnboardingFeature.Message.ClickedSignUn)
        }

        onboardingViewModel.onNewMessage(OnboardingFeature.Message.Initialize())
        onboardingViewModel.onNewMessage(OnboardingFeature.Message.ViewedEventMessage)
    }

    override fun onAction(action: OnboardingFeature.Action.ViewAction) {
        when (action) {
            is OnboardingFeature.Action.ViewAction.NavigateTo.AuthScreen ->
                requireRouter().navigateTo(AuthScreen(action.isInSignUpMode))
            is OnboardingFeature.Action.ViewAction.NavigateTo.NewUserScreen ->
                requireRouter().navigateTo(PlaceholderNewUserScreen)
        }
    }

    // TODO: read for more info https://vyahhi.myjetbrains.com/youtrack/issue/ALTAPPS-505
    // Support all state OnboardingFeature.State's
    // show loading, error and etc
    override fun render(state: OnboardingFeature.State) {
        // no op
    }
}