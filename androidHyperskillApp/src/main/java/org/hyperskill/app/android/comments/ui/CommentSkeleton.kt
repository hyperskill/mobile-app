package org.hyperskill.app.android.comments.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.HyperskillTheme
import org.hyperskill.app.android.core.view.ui.widget.compose.infiniteShimmer

private val HeaderTextVerticalSpacing = 8.dp
private val TextHeight = (CommentDefaults.CommentImageSize - HeaderTextVerticalSpacing) / 2
private val TextShape = RoundedCornerShape(4.dp)
private const val TextLinesCount = 3
private const val ReactionsCount = 3

@Composable
fun CommentSkeleton(modifier: Modifier = Modifier) {
    var playShimmer by remember { mutableStateOf(false) }
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(CommentDefaults.CommentImagePadding)
        ) {
            Box(
                modifier = Modifier
                    .requiredSize(CommentDefaults.CommentImageSize)
                    .clip(CircleShape)
                    .skeletonBackground()
                    .infiniteShimmer(playShimmer)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(HeaderTextVerticalSpacing)
            ) {
                Box(
                    modifier = Modifier
                        .size(height = TextHeight, width = 150.dp)
                        .clip(TextShape)
                        .skeletonBackground()
                        .infiniteShimmer(playShimmer)
                )
                Box(
                    modifier = Modifier
                        .size(height = TextHeight, width = 80.dp)
                        .clip(TextShape)
                        .skeletonBackground()
                        .infiniteShimmer(playShimmer)
                )
            }
        }
        Spacer(modifier = Modifier.height(CommentDefaults.CommentContentVerticalPadding))
        CommentTextSkeleton(playShimmer)
        Spacer(modifier = Modifier.height(CommentDefaults.CommentContentVerticalPadding))
        Row(
            horizontalArrangement = Arrangement.spacedBy(CommentDefaults.CommentImagePadding),
            modifier = Modifier.padding(start = CommentDefaults.CommentContentStartPadding)
        ) {
            repeat(ReactionsCount) {
                Box(
                    modifier = Modifier
                        .size(47.dp, 29.dp)
                        .clip(CommentDefaults.ReactionShape)
                        .skeletonBackground()
                        .infiniteShimmer(playShimmer)
                )
            }
        }
    }
    LaunchedEffect(key1 = true) { playShimmer = true }
}

@Composable
private fun CommentTextSkeleton(
    playShimmer: Boolean
) {
    repeat(TextLinesCount) { i ->
        Box(
            modifier = Modifier
                .padding(start = CommentDefaults.CommentContentStartPadding)
                .fillMaxWidth(
                    fraction = when (i) {
                        0 -> .95f
                        1 -> .7f
                        else -> .5f
                    }
                )
                .height(TextHeight)
                .clip(TextShape)
                .skeletonBackground()
                .infiniteShimmer(playShimmer)
        )
        if (i != TextLinesCount - 1) {
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

private fun Modifier.skeletonBackground(): Modifier =
    this.composed {
        background(colorResource(id = R.color.color_on_surface_alpha_12))
    }

@Preview
@Composable
private fun CommentSkeletonPreview() {
    HyperskillTheme {
        CommentSkeleton()
    }
}