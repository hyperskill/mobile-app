package org.hyperskill.app.android.topic_completion.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillButton
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.ShimmerState
import org.hyperskill.app.android.core.view.ui.widget.compose.TypewriterTextEffect
import org.hyperskill.app.android.core.view.ui.widget.compose.shimmerShot
import org.hyperskill.app.android.topic_completion.model.TopicCompletedModalViewState

@Composable
fun TopicCompleted(
    viewState: TopicCompletedModalViewState,
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
    val shimmerState = remember { ShimmerState() }
    Column(modifier.padding(vertical = 17.dp)) {
        CloseButton(
            onClick = onCloseClick,
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 14.dp)
        )
        Content(
            viewState = viewState,
            enterTransitionState = enterTransitionState,
            modifier = Modifier.weight(1f),
            onDescriptionFullyShowed = {
                shimmerState.runShimmerAnimation()
            }
        )
        EnterTransition(enterTransitionState) {
            HyperskillButton(
                onClick = onCTAButtonClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .shimmerShot(shimmerState)
            ) {
                Text(text = viewState.callToActionButtonTitle)
            }
        }
    }
}

@Composable
private fun CloseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val actualOnClick by rememberUpdatedState(newValue = onClick)
    Box(
        modifier
            .requiredSize(24.dp)
            .clickable(onClick = actualOnClick)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_close_topic_completed),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun Content(
    viewState: TopicCompletedModalViewState,
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
            EnterTransition(
                visibleState = enterTransitionState,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                SpaceBotAvatar(
                    spacebotAvatarVariantIndex = viewState.spacebotAvatarVariantIndex
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            EnterTransition(
                visibleState = enterTransitionState,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Title(
                    text = viewState.title,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Description(
                text = viewState.description,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onDescriptionFullyShowed = onDescriptionFullyShowed
            )
            Spacer(modifier = Modifier.height(32.dp))
            EnterTransition(
                visibleState = enterTransitionState,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                EarnedGemsCount(text = viewState.earnedGemsText)
            }
        }
    }
}

@Composable
private fun Title(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle1,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = colorResource(id = org.hyperskill.app.R.color.color_on_surface_alpha_87),
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
private fun Description(
    text: String,
    modifier: Modifier = Modifier,
    onDescriptionFullyShowed: () -> Unit = {}
) {
    TypewriterTextEffect(
        text = text,
        startTypingDelayInMillis = TopicCompletedDefaults.DESCRIPTION_TYPING_DELAY_MILLIS,
        onEffectCompleted = onDescriptionFullyShowed
    ) { displayedText ->
        Text(
            text = displayedText,
            style = MaterialTheme.typography.subtitle1,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.15.sp,
            lineHeight = 24.sp,
            color = colorResource(id = org.hyperskill.app.R.color.color_on_surface_alpha_87),
            textAlign = TextAlign.Center,
            minLines = 2,
            modifier = modifier
        )
    }
}

@Composable
private fun SpaceBotAvatar(
    spacebotAvatarVariantIndex: Int,
    modifier: Modifier = Modifier
) {
    @DrawableRes
    val imageRes: Int = remember { getAvatarRes(spacebotAvatarVariantIndex) }
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = modifier
            .requiredSize(228.dp)
            .clip(CircleShape)
    )
}

@Composable
private fun EarnedGemsCount(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(modifier) {
        Text(
            text = text,
            fontSize = 15.sp,
            color = colorResource(id = org.hyperskill.app.R.color.color_on_surface_alpha_60),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_topic_completed_gems),
            contentDescription = null
        )
    }
}

@Suppress("MagicNumber")
@DrawableRes
private fun getAvatarRes(spacebotAvatarVariantIndex: Int): Int =
    when (spacebotAvatarVariantIndex) {
        1 -> R.drawable.topic_completion_spacebot_1
        2 -> R.drawable.topic_completion_spacebot_2
        3 -> R.drawable.topic_completion_spacebot_3
        4 -> R.drawable.topic_completion_spacebot_4
        5 -> R.drawable.topic_completion_spacebot_5
        6 -> R.drawable.topic_completion_spacebot_6
        7 -> R.drawable.topic_completion_spacebot_7
        8 -> R.drawable.topic_completion_spacebot_8
        9 -> R.drawable.topic_completion_spacebot_9
        10 -> R.drawable.topic_completion_spacebot_10
        11 -> R.drawable.topic_completion_spacebot_11
        12 -> R.drawable.topic_completion_spacebot_12
        13 -> R.drawable.topic_completion_spacebot_13
        14 -> R.drawable.topic_completion_spacebot_14
        15 -> R.drawable.topic_completion_spacebot_15
        16 -> R.drawable.topic_completion_spacebot_16
        17 -> R.drawable.topic_completion_spacebot_17
        18 -> R.drawable.topic_completion_spacebot_18
        19 -> R.drawable.topic_completion_spacebot_19
        20 -> R.drawable.topic_completion_spacebot_20
        else -> R.drawable.topic_completion_spacebot_1
    }

@Composable
private fun EnterTransition(
    visibleState: MutableTransitionState<Boolean>,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    AnimatedVisibility(
        visibleState = visibleState,
        enter = fadeIn(tween(durationMillis = TopicCompletedDefaults.ENTER_TRANSITION_DURATION_MILLIS)),
        content = content,
        modifier = modifier
    )
}

private object TopicCompletedDefaults {
    const val ENTER_TRANSITION_DURATION_MILLIS = 600
    const val DESCRIPTION_TYPING_DELAY_MILLIS = ENTER_TRANSITION_DURATION_MILLIS - 200
}

@Preview
@Composable
private fun TopicCompletedPreview() {
    HyperskillTheme {
        TopicCompleted(
            viewState = TopicCompletedModalViewState(),
            onCloseClick = {},
            onCTAButtonClick = {}
        )
    }
}
