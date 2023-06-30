package org.hyperskill.app.android.track_progress.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import org.hyperskill.app.android.R
import org.hyperskill.app.android.core.view.ui.widget.compose.ShimmerLoading

@Composable
fun BlockHeader(
    title: String,
    painter: Painter?,
    modifier: Modifier = Modifier
) {
    BlockHeader(title = title, modifier) {
        if (painter != null) {
            Image(
                painter = painter,
                contentDescription = null
            )
        }
    }
}

@Composable
fun BlockHeader(
    title: String,
    iconSource: String?,
    modifier: Modifier = Modifier
) {
    BlockHeader(title = title, modifier) {
        SubcomposeAsyncImage(
            model = iconSource,
            contentDescription = null,
            loading = { state ->
                val intrinsicSize = state.painter?.intrinsicSize
                if (intrinsicSize != null) {
                    ShimmerLoading(
                        radius = 50.dp,
                        modifier = Modifier.size(
                            width = intrinsicSize.width.dp,
                            height = intrinsicSize.height.dp
                        )
                    )
                }
            },
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.clip(CircleShape)
        )
    }
}

@Composable
private fun BlockHeader(
    title: String,
    modifier: Modifier = Modifier,
    iconContent: @Composable BoxScope.() -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ProgressDefaults.HeaderIconPadding)
    ) {
        Box(
            modifier = Modifier
                .size(ProgressDefaults.HeaderIconSize)
                .border(
                    width = 1.dp,
                    color = colorResource(id = org.hyperskill.app.R.color.color_on_surface_alpha_9),
                    shape = RoundedCornerShape(size = 30.dp)
                )
                .padding(7.dp),
            content = iconContent
        )
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.W500,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun BlockHeaderSkeleton(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(ProgressDefaults.HeaderIconPadding)
    ) {
        ShimmerLoading(
            modifier = Modifier
                .clip(CircleShape)
                .size(
                    width = ProgressDefaults.HeaderIconSize,
                    height = ProgressDefaults.HeaderIconSize
                )
        )
        ShimmerLoading(
            radius = dimensionResource(id = R.dimen.corner_radius),
            modifier = Modifier
                .size(width = 150.dp, height = 20.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
@Preview
fun BlockHeaderPreview() {
    BlockHeader(
        title = "Kotlin for Beginners",
        painter = painterResource(id = R.drawable.ic_toolbar_back)
    )
}

@Composable
@Preview
fun BlockHeaderSkeletonPreview() {
    BlockHeaderSkeleton()
}