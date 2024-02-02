package org.hyperskill.app.android.paywall.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.view.ui.navigation.requireAppRouter
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.paywall.ui.PaywallScreen
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.paywall.presentation.PaywallFeature.Action.ViewAction
import org.hyperskill.app.paywall.presentation.PaywallViewModel
import ru.nobird.android.view.base.ui.extension.argument

class PaywallFragment : Fragment() {
    companion object {
        const val PAYWALL_COMPLETED = "PAYWALL_COMPLETED"

        fun newInstance(paywallTransitionSource: PaywallTransitionSource): PaywallFragment =
            PaywallFragment().apply {
                this.paywallTransitionSource = paywallTransitionSource
            }
    }

    private var paywallTransitionSource: PaywallTransitionSource by argument()

    private var viewModelFactory: ViewModelProvider.Factory? = null
    private val paywallViewModel: PaywallViewModel by viewModels {
        requireNotNull(viewModelFactory)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        paywallViewModel.handleActions(this, onAction = ::onAction)
    }

    private fun injectComponent() {
        val platformPaywallComponent =
            HyperskillApp.graph().buildPlatformPaywallComponent(paywallTransitionSource)
        viewModelFactory = platformPaywallComponent.reduxViewModelFactory
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
                    PaywallScreen(
                        viewModel = paywallViewModel,
                        onBackClick = ::onBackClick
                    )
                }
            }
        }

    private fun onAction(action: ViewAction) {
        when (action) {
            ViewAction.CompletePaywall -> {
                requireAppRouter().sendResult(PAYWALL_COMPLETED, Any())
            }
            is ViewAction.ShowMessage -> {
                Toast.makeText(
                    requireContext(),
                    getString(action.errorKind.stringRes.resourceId),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun onBackClick() {
        requireRouter().exit()
    }
}