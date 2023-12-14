package org.hyperskill.app.android.welcome.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.view.ui.navigation.AuthScreen
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentWelcomeBinding
import org.hyperskill.app.android.track_selection.list.navigation.TrackSelectionListScreen
import org.hyperskill.app.track_selection.list.injection.TrackSelectionListParams
import org.hyperskill.app.welcome.presentation.WelcomeFeature
import org.hyperskill.app.welcome.presentation.WelcomeViewModel
import ru.nobird.android.view.base.ui.delegate.ViewStateDelegate
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class WelcomeFragment :
    Fragment(R.layout.fragment_welcome),
    ReduxView<WelcomeFeature.State, WelcomeFeature.Action.ViewAction> {
    companion object {
        fun newInstance(): Fragment =
            WelcomeFragment()
    }

    private val viewBinding: FragmentWelcomeBinding by viewBinding(FragmentWelcomeBinding::bind)

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private val welcomeViewModel: WelcomeViewModel by reduxViewModel(this) { viewModelFactory }

    private var viewStateDelegate: ViewStateDelegate<WelcomeFeature.State>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    private fun injectComponents() {
        val welcomeComponent = HyperskillApp.graph().buildWelcomeComponent()
        val platformWelcomeComponent = HyperskillApp.graph().buildPlatformWelcomeComponent(welcomeComponent)

        viewModelFactory = platformWelcomeComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewStateDelegate()

        viewBinding.welcomeSignInButton.setOnClickListener {
            welcomeViewModel.onNewMessage(WelcomeFeature.Message.ClickedSignInEventMessage)
            requireRouter().navigateTo(AuthScreen())
        }

        viewBinding.welcomeSignUpButton.setOnClickListener {
            welcomeViewModel.onNewMessage(WelcomeFeature.Message.ClickedSignUn)
        }

        welcomeViewModel.onNewMessage(WelcomeFeature.Message.Initialize())
        welcomeViewModel.onNewMessage(WelcomeFeature.Message.ViewedEventMessage)
    }

    private fun initViewStateDelegate() {
        viewStateDelegate = ViewStateDelegate<WelcomeFeature.State>().apply {
            addState<WelcomeFeature.State.Idle>()
            addState<WelcomeFeature.State.Loading>(viewBinding.welcomeProgressBar)
            addState<WelcomeFeature.State.NetworkError>(viewBinding.welcomeError.root)
            addState<WelcomeFeature.State.Content>(viewBinding.welcomeContent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewStateDelegate = null
    }

    override fun onAction(action: WelcomeFeature.Action.ViewAction) {
        when (action) {
            is WelcomeFeature.Action.ViewAction.NavigateTo.AuthScreen ->
                requireRouter().navigateTo(AuthScreen(action.isInSignUpMode))
            is WelcomeFeature.Action.ViewAction.NavigateTo.TrackSelectionListScreen ->
                requireRouter().navigateTo(
                    TrackSelectionListScreen(
                        TrackSelectionListParams(isNewUserMode = true)
                    )
                )
        }
    }

    override fun render(state: WelcomeFeature.State) {
        viewStateDelegate?.switchState(state)
    }
}