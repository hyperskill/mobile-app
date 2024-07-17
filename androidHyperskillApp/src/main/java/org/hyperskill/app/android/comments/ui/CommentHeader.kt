package org.hyperskill.app.android.comments.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import org.hyperskill.app.R
import org.hyperskill.app.android.core.view.ui.widget.compose.infiniteShimmer

@Composable
fun CommentHeader(
    authorAvatar: String,
    authorFullName: String,
    formattedTime: String?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(CommentDefaults.CommentImagePadding)
    ) {
        CommentAuthorAvatar(avatarUrl = authorAvatar)
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = authorFullName,
                style = MaterialTheme.typography.subtitle1,
                fontSize = 16.sp,
                color = colorResource(id = R.color.color_on_surface_alpha_60),
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp
            )
            if (formattedTime != null) {
                Text(
                    text = formattedTime,
                    style = MaterialTheme.typography.body1,
                    fontSize = 14.sp,
                    color = colorResource(id = R.color.text_secondary),
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
private fun CommentAuthorAvatar(
    avatarUrl: String,
    modifier: Modifier = Modifier
) {
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(avatarUrl)
            .crossfade(true)
            .build(),
        modifier = modifier
            .requiredSize(CommentDefaults.CommentImageSize)
            .clip(CircleShape),
        loading = {
            CommentAuthorAvatarPlaceholder(playShimmer = true)
        },
        error = {
            CommentAuthorAvatarPlaceholder(playShimmer = false)
        },
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        alignment = Alignment.Center,
        filterQuality = FilterQuality.High
    )
}

@Composable
private fun CommentAuthorAvatarPlaceholder(
    playShimmer: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .requiredSize(CommentDefaults.CommentImageSize)
            .clip(CircleShape)
            .background(colorResource(id = R.color.color_on_surface_alpha_12))
            .infiniteShimmer(play = playShimmer)
    )
}