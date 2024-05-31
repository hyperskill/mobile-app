package org.hyperskill.app.android.topic_completion.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import org.hyperskill.app.android.R

@Composable
fun TopicCompletedSpaceBotAvatar(
    spacebotAvatarVariantIndex: Int,
    modifier: Modifier = Modifier
) {
    @DrawableRes
    val imageRes: Int = remember { getTopicCompletedSpaceBotAvatar(spacebotAvatarVariantIndex) }
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = modifier
            .requiredSize(TopicCompletedDefaults.SPACE_BOT_AVATAR_SIZE)
            .clip(CircleShape)
    )
}

@Suppress("MagicNumber")
@DrawableRes
private fun getTopicCompletedSpaceBotAvatar(spacebotAvatarVariantIndex: Int): Int =
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