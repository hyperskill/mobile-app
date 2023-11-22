package org.hyperskill.app.android.challenge.ui

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.ShimmerLoading
import org.hyperskill.app.android.core.view.ui.widget.compose.WidgetDataLoadingError
import org.hyperskill.app.challenges.widget.presentation.ChallengeWidgetFeature.Message
import org.hyperskill.app.challenges.widget.view.model.ChallengeWidgetViewState

@Composable
fun ChallengeCard(
    viewState: ChallengeWidgetViewState,
    onNewMessage: (Message) -> Unit
) {
    when (viewState) {
        ChallengeWidgetViewState.Idle, ChallengeWidgetViewState.Empty -> {
            // no op
        }
        is ChallengeWidgetViewState.Loading -> {
            if (viewState.shouldShowSkeleton) {
                ShimmerLoading(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(ratio = 0.5f)
                )
            }
        }
        ChallengeWidgetViewState.Error -> {
            WidgetDataLoadingError(
                onRetryClick = {
                    onNewMessage(Message.RetryContentLoading)
                },
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(id = R.string.challenge_widget_network_error_text)
            )
        }
        is ChallengeWidgetViewState.Content.Announcement -> {
            AnnouncementChallengeCard(
                state = viewState,
                onReloadClick = {
                    onNewMessage(Message.DeadlineReachedReloadClicked)
                },
                onDescriptionLinkClick = {
                    onNewMessage(Message.LinkInTheDescriptionClicked(it))
                }
            )
        }
        is ChallengeWidgetViewState.Content.HappeningNow -> {
            HappeningNowChallengeCard(
                state = viewState,
                onReloadClick = {
                    onNewMessage(Message.DeadlineReachedReloadClicked)
                },
                onDescriptionLinkClick = {
                    onNewMessage(Message.LinkInTheDescriptionClicked(it))
                }
            )
        }
        is ChallengeWidgetViewState.Content.Completed -> {
            CompletedChallengeCard(
                state = viewState,
                onCollectRewardClick = {
                    onNewMessage(Message.CollectRewardClicked)
                }
            )
        }
        is ChallengeWidgetViewState.Content.PartiallyCompleted -> {
            PartiallyCompletedChallengeCard(
                state = viewState,
                onCollectRewardClick = {
                    onNewMessage(Message.CollectRewardClicked)
                }
            )
        }
        is ChallengeWidgetViewState.Content.Ended -> {
            NotCompletedChallengeCard(state = viewState)
        }
    }
}