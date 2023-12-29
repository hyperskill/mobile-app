package org.hyperskill.app.android.interview_preparation.delegate

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.interview_preparation.ui.InterviewPreparationCard
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature
import org.hyperskill.app.interview_preparation.view.model.InterviewPreparationWidgetViewState

class InterviewPreparationCardDelegate {
    private val stateFlow: MutableStateFlow<InterviewPreparationWidgetViewState?> = MutableStateFlow(null)
    fun setup(
        composeView: ComposeView,
        viewLifecycleOwner: LifecycleOwner,
        onNewMessage: (InterviewPreparationWidgetFeature.Message) -> Unit
    ) {
        composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent {
                HyperskillTheme {
                    val viewState by stateFlow.collectAsStateWithLifecycle()
                    DisposableEffect(viewLifecycleOwner) {
                        onNewMessage(InterviewPreparationWidgetFeature.Message.ViewedEventMessage)
                        onDispose {
                            // no op
                        }
                    }
                    viewState?.let { actualViewState ->
                        InterviewPreparationCard(
                            viewState = actualViewState,
                            onNewMessage = onNewMessage
                        )
                    }
                }
            }
        }
    }

    fun render(
        viewState: InterviewPreparationWidgetViewState
    ) {
        stateFlow.value = viewState
    }
}