package org.hyperskill.app.android.placeholder_new_user.fragment

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.SharedResources
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.view.ui.navigation.AuthScreen
import org.hyperskill.app.android.core.extensions.launchUrl
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentPlaceholderNewUserScreenBinding
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserViewModel
import ru.nobird.android.view.base.ui.extension.snackbar
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class PlaceholderNewUserFragment :
    Fragment(R.layout.fragment_placeholder_new_user_screen),
    ReduxView<PlaceholderNewUserFeature.State, PlaceholderNewUserFeature.Action.ViewAction> {
    companion object {
        fun newInstance(): Fragment =
            PlaceholderNewUserFragment()
    }

    private val viewBinding: FragmentPlaceholderNewUserScreenBinding by viewBinding(
        FragmentPlaceholderNewUserScreenBinding::bind
    )

    private lateinit var viewModelFactory: ViewModelProvider.Factory
    private val placeholderNewUserViewModel: PlaceholderNewUserViewModel by reduxViewModel(this) { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponents()
    }

    private fun injectComponents() {
        val placeholderNewUserComponent = HyperskillApp.graph().buildPlaceholderNewUserComponent()
        val platformPlaceholderNewUserComponent =
            HyperskillApp.graph().buildPlatformPlaceholderNewUserComponent(placeholderNewUserComponent)

        viewModelFactory = platformPlaceholderNewUserComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.placeholderContinueToHyperskillButton.setOnClickListener {
            with(placeholderNewUserViewModel) {
                onNewMessage(PlaceholderNewUserFeature.Message.ClickedContinueEventMessage)
                onNewMessage(PlaceholderNewUserFeature.Message.ClickedContinueOnWeb)
            }
        }

        viewBinding.placeholderSignInButton.setOnClickListener {
            placeholderNewUserViewModel.onNewMessage(PlaceholderNewUserFeature.Message.PlaceholderSignInTappedMessage)
        }

        placeholderNewUserViewModel.onNewMessage(PlaceholderNewUserFeature.Message.ViewedEventMessage)
    }

    override fun onAction(action: PlaceholderNewUserFeature.Action.ViewAction) {
        when (action) {
            PlaceholderNewUserFeature.Action.ViewAction.NavigateTo.AuthScreen ->
                requireRouter().newRootScreen(AuthScreen)
            is PlaceholderNewUserFeature.Action.ViewAction.FollowUrl ->
                requireContext().launchUrl(action.url)
            PlaceholderNewUserFeature.Action.ViewAction.ShowFollowLinkError ->
                viewBinding.root.snackbar(SharedResources.strings.common_error.resourceId)
        }
    }

    override fun render(state: PlaceholderNewUserFeature.State) {
        when (state) {
            is PlaceholderNewUserFeature.State.Content -> {
                viewBinding.placeholderProgress.isVisible = state.isLinkLoadingShown
            }
        }
    }
}