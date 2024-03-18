package org.hyperskill.app.android.welcome.fragment

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.view.ui.navigation.AuthScreen
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentWelcomeBinding
import org.hyperskill.app.android.debug.DebugScreen
import org.hyperskill.app.android.track_selection.list.navigation.TrackSelectionListScreen
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.debug.presentation.DebugFeature
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

    private val buildKonfig: BuildKonfig by lazy(LazyThreadSafetyMode.NONE) {
        HyperskillApp.graph().commonComponent.buildKonfig
    }

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

        val isDebugButtonVisible = DebugFeature.isAvailable(buildKonfig)
        with(viewBinding.welcomeDebugButton) {
            isVisible = isDebugButtonVisible
            if (isDebugButtonVisible) {
                setOnClickListener {
                    requireRouter().navigateTo(DebugScreen(true))
                }
            }
        }


        with (viewBinding.welcomeSignInButton) {
            updateLayoutParams<MarginLayoutParams> {
                bottomMargin = if (isDebugButtonVisible) {
                    resources.getDimensionPixelOffset(R.dimen.auth_button_vertical_margin)
                } else {
                    0
                }
            }
            setOnClickListener {
                welcomeViewModel.onNewMessage(WelcomeFeature.Message.ClickedSignInEventMessage)
                requireRouter().navigateTo(AuthScreen())
            }
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