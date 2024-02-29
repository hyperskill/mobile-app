package org.hyperskill.app.android.manage_subscription.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.extensions.openUrl
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.manage_subscription.ui.ManageSubscriptionScreen
import org.hyperskill.app.android.paywall.navigation.PaywallScreen
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionFeature.Action.ViewAction
import org.hyperskill.app.manage_subscription.presentation.ManageSubscriptionViewModel

class ManageSubscriptionFragment : Fragment() {
    companion object {
        fun newInstance(): ManageSubscriptionFragment =
            ManageSubscriptionFragment()
    }

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val manageSubscriptionViewModel: ManageSubscriptionViewModel by viewModels {
        requireNotNull(viewModelFactory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        manageSubscriptionViewModel.handleActions(this, onAction = ::onAction)
    }

    private fun injectComponent() {
        val platformManageSubscriptionComponent =
            HyperskillApp.graph().buildPlatformManageSubscriptionComponent()
        viewModelFactory = platformManageSubscriptionComponent.reduxViewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent {
                HyperskillTheme {
                    ManageSubscriptionScreen(
                        viewModel = manageSubscriptionViewModel,
                        onBackClick = ::onBackClick
                    )
                }
            }
        }

    private fun onAction(action: ViewAction) {
        when (action) {
            is ViewAction.OpenUrl ->
                requireContext().openUrl(action.url)
            is ViewAction.NavigateTo.Paywall ->
                requireRouter().navigateTo(PaywallScreen(action.paywallTransitionSource))
        }
    }

    private fun onBackClick() {
        requireRouter().exit()
    }
}