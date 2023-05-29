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
import org.hyperskill.app.android.track_selection.list.navigation.TrackSelectionListScreen
import org.hyperskill.app.onboarding.presentation.OnboardingFeature
import org.hyperskill.app.onboarding.presentation.OnboardingViewModel
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListParams
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
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

    private var viewStateDelegate: ViewStateDelegate<OnboardingFeature.State>? = null

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

        initViewStateDelegate()

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

    private fun initViewStateDelegate() {
        viewStateDelegate = ViewStateDelegate<OnboardingFeature.State>().apply {
            addState<OnboardingFeature.State.Idle>()
            addState<OnboardingFeature.State.Loading>(viewBinding.onboardingProgressBar)
            addState<OnboardingFeature.State.NetworkError>(viewBinding.onboardingError.root)
            addState<OnboardingFeature.State.Content>(viewBinding.onboardingContent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewStateDelegate = null
    }

    override fun onAction(action: OnboardingFeature.Action.ViewAction) {
        when (action) {
            is OnboardingFeature.Action.ViewAction.NavigateTo.AuthScreen ->
                requireRouter().navigateTo(AuthScreen(action.isInSignUpMode))
            is OnboardingFeature.Action.ViewAction.NavigateTo.TrackSelectionListScreen ->
                requireRouter().navigateTo(
                    TrackSelectionListScreen(
                        TrackSelectionListParams(true)
                    )
                )
        }
    }

    override fun render(state: OnboardingFeature.State) {
        viewStateDelegate?.switchState(state)
    }
}