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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import co.touchlab.kermit.Logger
import org.hyperskill.app.android.HyperskillApp
import org.hyperskill.app.android.core.extensions.launchUrlInCustomTabs
import org.hyperskill.app.android.core.view.ui.navigation.requireAppRouter
import org.hyperskill.app.android.core.view.ui.navigation.requireRouter
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.main.view.ui.navigation.MainScreen
import org.hyperskill.app.android.main.view.ui.navigation.Tabs
import org.hyperskill.app.android.paywall.ui.PaywallScreen
import org.hyperskill.app.core.view.handleActions
import org.hyperskill.app.paywall.domain.model.PaywallTransitionSource
import org.hyperskill.app.paywall.presentation.PaywallFeature
import org.hyperskill.app.paywall.presentation.PaywallFeature.Action.ViewAction
import org.hyperskill.app.paywall.presentation.PaywallViewModel
import ru.nobird.android.view.base.ui.extension.argument

class PaywallFragment : Fragment() {
    companion object {
        const val PAYWALL_IS_SHOWN_CHANGED = "PAYWALL_IS_SHOWN_CHANGED"
        private const val LOG_TAG = "PaywallFragment"

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

    private val logger: Logger by lazy(LazyThreadSafetyMode.NONE) {
        HyperskillApp.graph().loggerComponent.logger.withTag(LOG_TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        paywallViewModel.handleActions(this, onAction = ::onAction)
        lifecycle.addObserver(
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> paywallViewModel.onNewMessage(PaywallFeature.Message.ScreenShowed)
                    Lifecycle.Event.ON_STOP -> paywallViewModel.onNewMessage(PaywallFeature.Message.ScreenHidden)
                    else -> {
                        // no op
                    }
                }
            }
        )
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
            is ViewAction.NotifyPaywallIsShown -> {
                requireAppRouter().sendResult(PAYWALL_IS_SHOWN_CHANGED, action.isPaywallShown)
            }
            ViewAction.NavigateTo.Back -> {
                requireRouter().exit()
            }
            is ViewAction.ShowMessage -> {
                Toast.makeText(
                    requireContext(),
                    getString(action.messageKind.stringRes.resourceId),
                    Toast.LENGTH_SHORT
                ).show()
            }
            ViewAction.NavigateTo.BackToProfileSettings ->
                requireRouter().backTo(MainScreen(Tabs.PROFILE))
            ViewAction.ClosePaywall ->
                requireRouter().exit()
            is ViewAction.OpenUrl -> {
                launchUrlInCustomTabs(action.url, logger)
            }
        }
    }

    private fun onBackClick() {
        requireRouter().exit()
    }
}