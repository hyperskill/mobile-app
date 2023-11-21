package org.hyperskill.app.android.challenge.delegate

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import org.hyperskill.app.android.challenge.ui.ChallengeCard
import org.hyperskill.app.android.core.extensions.openUrl
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState

class ChallengeCardDelegate {
    private val stateFlow: MutableStateFlow<ChallengeWidgetViewState?> = MutableStateFlow(null)

    fun setup(
        composeView: ComposeView,
        onNewMessage: (ChallengeWidgetFeature.Message) -> Unit
    ) {
        composeView.setContent {
            HyperskillTheme {
                val viewState by stateFlow.collectAsStateWithLifecycle()
                viewState?.let { actualViewState ->
                    ChallengeCard(viewState = actualViewState, onNewMessage = onNewMessage)
                }
            }
        }
    }

    fun render(state: ChallengeWidgetViewState) {
        stateFlow.value = state
    }

    fun handleAction(
        context: Context,
        action: ChallengeWidgetFeature.Action.ViewAction
    ) {
        when (action) {
            is ChallengeWidgetFeature.Action.ViewAction.OpenUrl -> {
                context.openUrl(action.url)
            }
            ChallengeWidgetFeature.Action.ViewAction.ShowNetworkError -> TODO()
        }
    }
}