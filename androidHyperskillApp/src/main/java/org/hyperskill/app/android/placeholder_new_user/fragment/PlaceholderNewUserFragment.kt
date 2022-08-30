package org.hyperskill.app.android.placeholder_new_user.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.auth.view.ui.navigation.AuthScreen
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.databinding.FragmentPlaceholderNewUserScreenBinding
import org.hyperskill.app.auth.domain.model.UserDeauthorized
import org.hyperskill.app.config.BuildKonfig
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserFeature
import org.hyperskill.app.placeholder_new_user.presentation.PlaceholderNewUserViewModel
import org.hyperskill.app.profile.domain.interactor.ProfileInteractor
import ru.nobird.android.view.redux.ui.extension.reduxViewModel
import ru.nobird.app.presentation.redux.container.ReduxView

class PlaceholderNewUserFragment :
    Fragment(R.layout.fragment_placeholder_new_user_screen),
    ReduxView<PlaceholderNewUserFeature.State, PlaceholderNewUserFeature.Action.ViewAction> {
    companion object {
        fun newInstance(): Fragment =
            PlaceholderNewUserFragment()
    }

    private lateinit var profileInteractor: ProfileInteractor
    private lateinit var authSharedFlow: MutableSharedFlow<UserDeauthorized>

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

        profileInteractor = HyperskillApp.graph().authComponent.profileInteractor
        authSharedFlow = HyperskillApp.graph().networkComponent.authorizationFlow

        viewModelFactory = platformPlaceholderNewUserComponent.reduxViewModelFactory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.placeholderContinueToHyperskillButton.setOnClickListener {
            placeholderNewUserViewModel.onNewMessage(PlaceholderNewUserFeature.Message.PlaceholderClickedContinueEventMessage)

            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(BuildKonfig.BASE_URL)
            startActivity(intent)
        }

        viewBinding.placeholderSignInButton.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                profileInteractor.clearCache()
                authSharedFlow.emit(UserDeauthorized)
                requireRouter().newRootScreen(AuthScreen)
            }
        }

        placeholderNewUserViewModel.onNewMessage(PlaceholderNewUserFeature.Message.PlaceholderViewedEventMessage)
    }

    override fun onAction(action: PlaceholderNewUserFeature.Action.ViewAction) {
        // no op
    }

    override fun render(state: PlaceholderNewUserFeature.State) {
        // no op
    }
}