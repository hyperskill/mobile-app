package org.hyperskill.app.android.users_questionnaire.delegate

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import org.hyperskill.app.android.core.extensions.launchUrlInCustomTabs
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.users_questionnaire.ui.UsersQuestionnaireWidget
import org.hyperskill.app.users_questionnaire.widget.presentation.UsersQuestionnaireWidgetFeature

class UsersQuestionnaireCardDelegate {

    private val stateFlow: MutableStateFlow<UsersQuestionnaireWidgetFeature.State?> = MutableStateFlow(null)

    fun setup(
        composeView: ComposeView,
        viewLifecycleOwner: LifecycleOwner,
        onNewMessage: (UsersQuestionnaireWidgetFeature.Message) -> Unit
    ) {
        composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent {
                HyperskillTheme {
                    val viewState by stateFlow.collectAsStateWithLifecycle()
                    DisposableEffect(viewLifecycleOwner) {
                        onNewMessage(UsersQuestionnaireWidgetFeature.Message.ViewedEventMessage)
                        onDispose {
                            // no op
                        }
                    }
                    viewState?.let { actualViewState ->
                        UsersQuestionnaireWidget(
                            actualViewState,
                            onNewMessage = onNewMessage
                        )
                    }
                }
            }
        }
    }

    fun render(
        state: UsersQuestionnaireWidgetFeature.State,
        composeView: ComposeView
    ) {
        composeView.isVisible = when (state) {
            UsersQuestionnaireWidgetFeature.State.Idle,
            UsersQuestionnaireWidgetFeature.State.Hidden -> false
            UsersQuestionnaireWidgetFeature.State.Loading,
            UsersQuestionnaireWidgetFeature.State.Visible -> true
        }
        stateFlow.value = state
    }

    fun handleActions(
        fragment: Fragment,
        action: UsersQuestionnaireWidgetFeature.Action.ViewAction,
        logger: Logger
    ) {
        when (action) {
            is UsersQuestionnaireWidgetFeature.Action.ViewAction.ShowUsersQuestionnaire -> {
                fragment.launchUrlInCustomTabs(action.url, logger)
            }
        }
    }
}