package org.hyperskill.app.android.placeholder_new_user.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.databinding.FragmentPlaceholderNewUserScreenBinding
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserViewModel
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

//        viewBinding.placeholderContinueToHyperskillButton.setOnClickListener {
//            placeholderNewUserViewModel.onNewMessage(PlaceholderNewUserFeature.Message.ClickedContinueOnWeb)
//        }

//        viewBinding.placeholderSignInButton.setOnClickListener {
//            placeholderNewUserViewModel.onNewMessage(PlaceholderNewUserFeature.Message.PlaceholderSignInTappedMessage)
//        }

        placeholderNewUserViewModel.onNewMessage(PlaceholderNewUserFeature.Message.ViewedEventMessage)
    }

    override fun onAction(action: PlaceholderNewUserFeature.Action.ViewAction) {
//        when (action) {
//            PlaceholderNewUserFeature.Action.ViewAction.NavigateTo.AuthScreen ->
//                requireRouter().newRootScreen(AuthScreen)
//            is PlaceholderNewUserFeature.Action.ViewAction.OpenUrl ->
//                requireContext().launchUrl(action.url)
//            PlaceholderNewUserFeature.Action.ViewAction.ShowGetMagicLinkError ->
//                viewBinding.root.snackbar(SharedResources.strings.common_error.resourceId)
//        }
    }

    override fun render(state: PlaceholderNewUserFeature.State) {
        when (state) {
            is PlaceholderNewUserFeature.State.Content -> {
//                if (state.isLoadingMagicLink) {
//                    LoadingProgressDialogFragment.newInstance()
//                        .showIfNotExists(childFragmentManager, LoadingProgressDialogFragment.TAG)
//                } else {
//                    childFragmentManager.dismissDialogFragmentIfExists(LoadingProgressDialogFragment.TAG)
//                }
            }
        }
    }
}