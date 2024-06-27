package org.hyperskill.app.android.topic_completion.ui

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.ShimmerShotState
import org.hyperskill.app.android.core.view.ui.widget.compose.shimmerShot
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalFeature.ViewState
import org.hyperskill.app.topic_completed_modal.presentation.TopicCompletedModalViewModel

@Composable
fun TopicCompleted(viewModel: TopicCompletedModalViewModel) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    TopicCompleted(
        viewState = viewState,
        onCloseClick = viewModel::onCloseClick,
        onCTAButtonClick = viewModel::onCTAClick
    )
}

@Composable
fun TopicCompleted(
    viewState: ViewState,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCTAButtonClick: () -> Unit
) {
    val enterTransitionState = remember {
        MutableTransitionState(false)
    }
    LaunchedEffect(Unit) {
        enterTransitionState.targetState = true
    }
    val shimmerState = remember { ShimmerShotState() }
    Column(
        modifier
            .padding(vertical = TopicCompletedDefaults.CONTENT_VERTICAL_PADDING)
            .safeDrawingPadding()
    ) {
        TopicCompletedCloseButton(
            onClick = onCloseClick,
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = TopicCompletedDefaults.CLOSE_BUTTON_PADDING)
        )
        Content(
            viewState = viewState,
            enterTransitionState = enterTransitionState,
            modifier = Modifier.weight(1f),
            onDescriptionFullyShowed = {
                shimmerState.runShimmerAnimation()
            }
        )
        TopicCompletedEnterTransition(enterTransitionState) {
            HyperskillButton(
                onClick = onCTAButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(TopicCompletedDefaults.CTA_HORIZONTAL_PADDING)
                    .shimmerShot(shimmerState)
            ) {
                Text(text = viewState.callToActionButtonTitle)
            }
        }
    }
}

@Composable
private fun Content(
    viewState: ViewState,
    enterTransitionState: MutableTransitionState<Boolean>,
    onDescriptionFullyShowed: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 32.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            TopicCompletedEnterTransition(
                visibleState = enterTransitionState,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                TopicCompletedSpaceBotAvatar(
                    spacebotAvatarVariantIndex = viewState.spacebotAvatarVariantIndex
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            TopicCompletedEnterTransition(
                visibleState = enterTransitionState,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                TopicCompletedTitle(
                    text = viewState.title,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            TopicCompletedDescription(
                text = viewState.description,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onDescriptionFullyShowed = onDescriptionFullyShowed
            )
            Spacer(modifier = Modifier.height(32.dp))
            TopicCompletedEnterTransition(
                visibleState = enterTransitionState,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                TopicCompletedEarnedGemsCount(text = viewState.earnedGemsText)
            }
        }
    }
}

@Preview
@Composable
private fun TopicCompletedPreview() {
    HyperskillTheme {
        TopicCompleted(
            viewState = ViewState(
                title = "{Topic name} completed!",
                description = "Learning might be tough, but it brings you knowledge that lasts forever",
                earnedGemsText = "+ 5",
                callToActionButtonTitle = "Continue with next topic",
                spacebotAvatarVariantIndex = 0,
                backgroundAnimationStyle = ViewState.BackgroundAnimationStyle.FIRST
            ),
            onCloseClick = {},
            onCTAButtonClick = {}
        )
    }
}