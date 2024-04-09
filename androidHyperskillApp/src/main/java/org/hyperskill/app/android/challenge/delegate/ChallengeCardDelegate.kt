package org.hyperskill.app.android.challenge.delegate

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import org.hyperskill.app.R
import org.hyperskill.app.android.challenge.ui.ChallengeCard
import org.hyperskill.app.android.core.extensions.launchUrlInCustomTabs
import org.hyperskill.app.android.core.extensions.openUrl
import org.hyperskill.app.android.core.view.ui.dialog.CreateMagicLinkLoadingProgressDialogFragment
import org.hyperskill.app.android.core.view.ui.dialog.dismissDialogFragmentIfExists
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState
import org.hyperskill.app.challenges.widget.view.model.isLoadingMagicLink
import ru.nobird.android.view.base.ui.extension.showIfNotExists

class ChallengeCardDelegate {
    private val stateFlow: MutableStateFlow<ChallengeWidgetViewState?> = MutableStateFlow(null)

    fun setup(
        composeView: ComposeView,
        viewLifecycleOwner: LifecycleOwner,
        onNewMessage: (ChallengeWidgetFeature.Message) -> Unit
    ) {
        composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent {
                HyperskillTheme {
                    val viewState by stateFlow.collectAsStateWithLifecycle()
                    viewState?.let { actualViewState ->
                        ChallengeCard(viewState = actualViewState, onNewMessage = onNewMessage)
                    }
                }
            }
        }
    }

    fun render(
        fragmentManager: FragmentManager,
        state: ChallengeWidgetViewState
    ) {
        stateFlow.value = state
        if (state is ChallengeWidgetViewState.Content && state.isLoadingMagicLink) {
            CreateMagicLinkLoadingProgressDialogFragment.newInstance()
                .showIfNotExists(fragmentManager, CreateMagicLinkLoadingProgressDialogFragment.TAG)
        } else {
            fragmentManager.dismissDialogFragmentIfExists(CreateMagicLinkLoadingProgressDialogFragment.TAG)
        }
    }

    fun handleAction(
        fragment: Fragment,
        action: ChallengeWidgetFeature.Action.ViewAction,
        logger: Logger
    ) {
        when (action) {
            is ChallengeWidgetFeature.Action.ViewAction.OpenUrl -> {
                if (action.shouldOpenInApp) {
                    fragment.launchUrlInCustomTabs(action.url, logger)
                } else {
                    fragment.requireContext().openUrl(action.url, logger)
                }
            }
            ChallengeWidgetFeature.Action.ViewAction.ShowNetworkError -> {
                Toast
                    .makeText(fragment.requireContext(), R.string.common_error, Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}