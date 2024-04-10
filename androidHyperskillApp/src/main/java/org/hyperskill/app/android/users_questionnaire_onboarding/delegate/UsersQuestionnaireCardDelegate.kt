package org.hyperskill.app.android.users_questionnaire_onboarding.delegate

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import org.hyperskill.app.android.core.extensions.setHyperskillColors
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
        context: Context,
        activity: Activity,
        action: UsersInterviewWidgetFeature.Action.ViewAction
    ) {
        when (action) {
            is UsersInterviewWidgetFeature.Action.ViewAction.ShowUsersInterview -> {
                val intent = CustomTabsIntent.Builder()
                    .setHyperskillColors(context)
                    .build()
                intent.launchUrl(activity, Uri.parse(action.url))
            }
        }
    }
}