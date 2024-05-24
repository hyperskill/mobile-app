package org.hyperskill.app.android.topic_completion.ui

import androidx.annotation.DrawableRes
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
import org.hyperskill.app.android.topic_completion.model.TopicCompletedModalViewState

@Composable
fun TopicCompleted(
    viewState: TopicCompletedModalViewState,
    modifier: Modifier = Modifier
) {
    Column(modifier.padding(vertical = 17.dp)) {
        CloseButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 14.dp)
        ) {

        }
        Content(
            viewState = viewState,
            modifier = Modifier.weight(1f)
        )
        HyperskillButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            onClick = { /*TODO*/ }
        ) {
            Text(text = viewState.callToActionButtonTitle)
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
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.padding(horizontal = 32.dp)) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            SpaceBotAvatar(
                spacebotAvatarVariantIndex = viewState.spacebotAvatarVariantIndex,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Title(
                text = viewState.title,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Description(
                text = viewState.description,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(32.dp))
            EarnedGemsCount(
                text = viewState.earnedGemsText,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun Title(text: String, modifier: Modifier = Modifier) {
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
fun Description(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle1,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 0.15.sp,
        lineHeight = 24.sp,
        color = colorResource(id = org.hyperskill.app.R.color.color_on_surface_alpha_87),
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
fun SpaceBotAvatar(
    spacebotAvatarVariantIndex: Int,
    modifier: Modifier = Modifier
) {
    val imageRes = remember { getAvatarRes(spacebotAvatarVariantIndex) }
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = modifier
            .requiredSize(228.dp)
            .clip(CircleShape)
    )
}

@Composable
private fun EarnedGemsCount(text: String, modifier: Modifier = Modifier) {
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

@Preview
@Composable
private fun TopicCompletedPreview() {
    HyperskillTheme {
        TopicCompleted(TopicCompletedModalViewState())
    }
}