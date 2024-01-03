package org.hyperskill.app.android.interview_preparation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.ShimmerLoading
import org.hyperskill.app.android.core.view.ui.widget.compose.WidgetDataLoadingError
import org.hyperskill.app.interview_preparation.presentation.InterviewPreparationWidgetFeature.Message
import org.hyperskill.app.interview_preparation.view.model.InterviewPreparationWidgetViewState

@Composable
fun InterviewPreparationCard(
    viewState: InterviewPreparationWidgetViewState,
    onNewMessage: (Message) -> Unit
) {
    when (viewState) {
        InterviewPreparationWidgetViewState.Idle,
        InterviewPreparationWidgetViewState.Empty -> {
            // no op
        }
        is InterviewPreparationWidgetViewState.Loading -> {
            if (viewState.shouldShowSkeleton) {
                ShimmerLoading(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
            }
        }
        InterviewPreparationWidgetViewState.Error -> {
            WidgetDataLoadingError(
                onRetryClick = {
                    onNewMessage(Message.RetryContentLoading)
                },
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.interview_preparation_widget_network_error_text)
            )
        }
        is InterviewPreparationWidgetViewState.Content -> {
            InterviewPreparationContent(
                content = viewState,
                onClick = {
                    onNewMessage(Message.WidgetClicked)
                }
            )
        }
    }
}