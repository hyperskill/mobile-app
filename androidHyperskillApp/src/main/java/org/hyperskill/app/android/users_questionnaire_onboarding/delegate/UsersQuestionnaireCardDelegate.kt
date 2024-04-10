package org.hyperskill.app.android.users_questionnaire_onboarding.delegate

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
import org.hyperskill.app.android.users_questionnaire_onboarding.ui.UsersQuestionnaireWidget
import org.hyperskill.app.users_interview_widget.presentation.UsersInterviewWidgetFeature

// TODO: ALTAPPS-1217 refactor to users interview widget
class UsersQuestionnaireCardDelegate {

    private val stateFlow: MutableStateFlow<UsersInterviewWidgetFeature.State?> = MutableStateFlow(null)

    fun setup(
        composeView: ComposeView,
        viewLifecycleOwner: LifecycleOwner,
        onNewMessage: (UsersInterviewWidgetFeature.Message) -> Unit
    ) {
        composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnLifecycleDestroyed(viewLifecycleOwner))
            setContent {
                HyperskillTheme {
                    val viewState by stateFlow.collectAsStateWithLifecycle()
                    DisposableEffect(viewLifecycleOwner) {
                        onNewMessage(UsersInterviewWidgetFeature.Message.ViewedEventMessage)
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
        state: UsersInterviewWidgetFeature.State,
        composeView: ComposeView
    ) {
        composeView.isVisible = when (state) {
            UsersInterviewWidgetFeature.State.Idle,
            UsersInterviewWidgetFeature.State.Hidden -> false
            UsersInterviewWidgetFeature.State.Loading,
            UsersInterviewWidgetFeature.State.Visible -> true
        }
        stateFlow.value = state
    }

    fun handleActions(
        fragment: Fragment,
        action: UsersInterviewWidgetFeature.Action.ViewAction,
        logger: Logger
    ) {
        when (action) {
            is UsersInterviewWidgetFeature.Action.ViewAction.ShowUsersInterview -> {
                fragment.launchUrlInCustomTabs(action.url, logger)
            }
        }
    }
}