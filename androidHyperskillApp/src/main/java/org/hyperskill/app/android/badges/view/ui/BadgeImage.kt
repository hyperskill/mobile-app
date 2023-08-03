package org.hyperskill.app.android.badges.view.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import org.hyperskill.app.badges.domain.model.BadgeKind
import org.hyperskill.app.profile.view.BadgesViewState

@Composable
fun BadgeImage(
    badge: BadgesViewState.Badge,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        val imageModifier = remember {
            Modifier
                .padding(vertical = BadgeDefaults.badgeImageVerticalPadding)
                .size(BadgeDefaults.badgeImageSize)
                .align(Alignment.Center)
        }
        when (val image = badge.image) {
            BadgesViewState.BadgeImage.Locked -> {
                LockedBadgeImage(badgeKind = badge.kind, modifier = imageModifier)
            }
            is BadgesViewState.BadgeImage.Remote -> {
                AsyncBadgeImage(imageSource = image.fullSource, modifier = imageModifier)
            }
        }
    }
}

@Composable
private fun LockedBadgeImage(
    badgeKind: BadgeKind,
    modifier: Modifier
) {
    val lockedPainter = badgeKind.getLockedPainter()
    if (lockedPainter != null) {
        Image(
            painter = lockedPainter,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = modifier
        )
    }
}

@Composable
private fun AsyncBadgeImage(
    imageSource: String,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageSource)
            .crossfade(true)
            .build(),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        alignment = Alignment.Center,
        filterQuality = FilterQuality.High,
        modifier = modifier.fillMaxHeight()
    )
}